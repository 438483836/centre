package com.stylefeng.guns.netty.cameraInfoUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.nio.charset.Charset;

/**
 * Created by Vincent on 2018-08-23.
 */
public class CameraInfoServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CameraInfoServer.class);
    private ApplicationContext applicationContext;

    private int port;

    public CameraInfoServer(ApplicationContext applicationContext, int port) {
        this.applicationContext = applicationContext;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            startService();
        } catch (Exception e) {
            logger.error("CameraInfo Server Start err[{}]",e.getMessage());
        }
    }

    public void startService() throws Exception {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                    pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                    pipeline.addLast(new CameraInfoInHandler());
                }
            });

            bootstrap.option(ChannelOption.SO_BACKLOG,1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.childOption(ChannelOption.TCP_NODELAY,true);
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("CameraInfo Server Start Success");
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {

            logger.info("CameraInfo Server Start err[{}]",e.getMessage());

        }finally {

            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
}
