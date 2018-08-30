package com.stylefeng.guns.netty.zto.httpSwtichUtil;

import com.stylefeng.guns.netty.CommunicationConfig;
import com.stylefeng.guns.netty.HttpRequestFormat;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**

* @Description:    请求中通总部开关流水线

* @Author:         sipengfei

* @CreateDate:     2018/8/17 11:17

* @UpdateUser:     sipengfei

* @UpdateDate:     2018/8/17 11:17

* @UpdateRemark:   修改内容

* @Version:        1.0

*/
public class HttpSwitchClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpSwitchClient.class);

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    private Bootstrap bootstrap = new Bootstrap();

    private static URI uri;
    static {
        try {
            uri = new URI(CommunicationConfig.ztoUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                ch.pipeline().addLast(new HttpResponseDecoder());
                // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                ch.pipeline().addLast(new HttpRequestEncoder());
                ch.pipeline().addLast(new HttpSwitchClientHandler());
            }
        });

    }

    public void sendImpl(String msg){
        try {
            channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),
                Unpooled.wrappedBuffer(msg.getBytes()));
        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, uri.getHost());
        request.headers().set(HttpHeaders.Names.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=utf-8");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                request.content().readableBytes());
        // 发送http请求
        channelFuture.channel().writeAndFlush(request).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()){
                logger.info("send switch-line request to zto success. msg:[{}]",msg);
            }else {
                logger.info("send switch-line request to zto fail. msg:[{}]",msg);
            }
        });
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        HttpSwitchClient client = new HttpSwitchClient();
        client.start();
        client.sendImpl(HttpRequestFormat.switchLine("start"));
    }
}
