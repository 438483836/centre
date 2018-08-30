package com.stylefeng.guns.netty.supplyPlatUtil;

import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 扫描信息数据至PLC
 * Created by Vincent on 2018-08-21.
 */
public class SupplyPlatClient {

    private static final Logger logger = LoggerFactory.getLogger(SupplyPlatClient.class);

    private static final long LockTimeoutMillis = 3000;

    private ApplicationContext applicationContext;

    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    private final Bootstrap bootstrap = new Bootstrap();

    private final Lock lockChannelTables = new ReentrantLock();

    private final ConcurrentHashMap<String /* addr */, ChannelWrapper> channelTables =
            new ConcurrentHashMap<String, ChannelWrapper>();

    public SupplyPlatClient(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;}

    public void start(){
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(4, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
            }
        });

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap handler = this.bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE, false) //连接会测试链接的状态
                .option(ChannelOption.SO_SNDBUF, 65535)  //用于操作接收缓冲区和发送缓冲区
                .option(ChannelOption.SO_RCVBUF, 65535)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                new SupplyPlatEncoder(),
                                new LineBasedFrameDecoder(1024), //解决TCP粘包问题
                                new StringDecoder(), //
                                new IdleStateHandler(0, 0, 60*10),
                                new NettyConnectManageHandler(),
                                new NettyClientHandler()
                                );
                    }
                });

    }


    class ChannelWrapper {
        //ChannelFuture提供有关I / O操作的结果或状态的信息
        private final ChannelFuture channelFuture;
        public ChannelWrapper(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }
        public boolean isOK() {
            return (this.channelFuture.channel() != null && this.channelFuture.channel().isActive());
        }
        private Channel getChannel() {
            return this.channelFuture.channel();
        }
        public ChannelFuture getChannelFuture() {
            return channelFuture;
        }
    }

    //使用netty的ChannelDuplexHandler 来接收、下发硬件数据
    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
            final String local = localAddress == null ? "UNKNOW" : localAddress.toString();
            final String remote = remoteAddress == null ? "UNKNOW" : remoteAddress.toString();
            logger.info("NETTY CLIENT PIPELINE: CONNECT  {" + local + "} => {" + remote + "}");
            super.connect(ctx, remoteAddress, localAddress, promise);
        }


        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("NETTY CLIENT PIPELINE: DISCONNECT {"+remoteAddress+"}");
            closeChannel(ctx.channel());
            super.disconnect(ctx, promise);
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("NETTY CLIENT PIPELINE: CLOSE {"+remoteAddress+"}");
            closeChannel(ctx.channel());
            super.close(ctx, promise);
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            logger.info("NETTY CLIENT PIPELINE: exceptionCaught {"+remoteAddress+"}");
            logger.info("NETTY CLIENT PIPELINE: exceptionCaught exception:"+cause);
            closeChannel(ctx.channel());
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent evnet = (IdleStateEvent) evt;
                if (evnet.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    logger.info("NETTY CLIENT PIPELINE: IDLE exception [{" + remoteAddress + "}]");
                    closeChannel(ctx.channel());
                }
            }

            ctx.fireUserEventTriggered(evt);
        }

    }

    class NettyClientHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        }
    }

    public Channel getAndCreateChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }
        return this.createChannel(addr);
    }

    private Channel createChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        if (this.lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)) {
            try{

                boolean createNewConnection = false;
                cw = this.channelTables.get(addr);
                if (cw != null) {
                    if (cw.isOK()) {
                        return cw.getChannel();
                    }
                    //正在连接，退出锁等待
                    else if(!cw.getChannelFuture().isDone()){
                        createNewConnection = false;
                    }
                    //说明连接不成功
                    else {
                        this.channelTables.remove(addr);
                        createNewConnection = true;
                    }
                }
                else {
                    createNewConnection = true;
                }
                if (createNewConnection){
                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(addr));
                    logger.info("createChannel: begin to connect remote host[{}] asynchronously", addr);
                    cw = new ChannelWrapper(channelFuture);
                    this.channelTables.put(addr,cw);
                }

            }finally {
                this.lockChannelTables.unlock();
            }

            if (cw != null){
                ChannelFuture channelFuture = cw.getChannelFuture();
                if (channelFuture.awaitUninterruptibly(3000)) {
                    if (cw.isOK()) {
                        return cw.getChannel();
                    }else {
                        logger.warn("createChannel: connect remote host[" + addr + "] failed, " + channelFuture.toString(), channelFuture.cause());
                    }
                }
            }

        }
        return null;
    }

    public void closeChannel(final Channel channel){
        if (null == channel)
            return;

        try {
            if (this.lockChannelTables.tryLock(LockTimeoutMillis, TimeUnit.MILLISECONDS)){
                boolean removeItemFromTable = true;
                ChannelWrapper prevCW = null;
                String addrRemote = null;
                for(String key : channelTables.keySet()){
                    ChannelWrapper prev = this.channelTables.get(key);
                    if (prev.getChannel() != null){
                        if (prev.getChannel() == channel){
                            prevCW = prev;
                            addrRemote = key;
                            break;
                        }
                    }
                }

                if (null == prevCW){
                    removeItemFromTable = false;
                }
                if (removeItemFromTable){
                    this.channelTables.remove(addrRemote);
                    RemotingUtil.closeChannel(channel);
                }
            }
        } catch (InterruptedException e) {
                logger.error("closeChannel exception", e.getMessage());
        } finally {
            this.lockChannelTables.unlock();
        }

    }

    public void sendSyncImpl(String ip, int port, String data) throws InterruptedException {
        this.sendSyncImpl(ip + ":" + port, data);
    }

    public void sendSyncImpl(String addr, String data) throws InterruptedException {
        Channel channel = this.getAndCreateChannel(addr);

        channel.writeAndFlush(data).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("SendRequestOK");
                    return;
                }  else {
                    logger.info("SendRequestFalse");
                }
            }
        });
    }

}
