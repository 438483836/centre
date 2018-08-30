package com.stylefeng.guns.netty.complementInfoUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ComplementInfoInHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ComplementInfoInHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String receive = (String) msg;
        InetSocketAddress socketAddress=(InetSocketAddress)ctx.channel().remoteAddress();
        String remoteIp=socketAddress.getHostString();
        int port = socketAddress.getPort();
        logger.info(remoteIp+":"+port);
    }

    @Override
    public boolean isSharable() {
        System.out.println("==============ComplementInfoInHandler handler-sharable==============");
        return super.isSharable();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============ComplementInfoInHandler channel-register==============");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============ComplementInfoInHandler channel-unregister==============");
    }
    //新客户端接入
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============ComplementInfoInHandler channel-Active==============");
    }
    //客户端断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("==============ComplementInfoInHandler channel-inactive==============");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============ComplementInfoInHandler channel-read-complete==============");
        ctx.flush();
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        logger.error(cause.getMessage());
        //关闭通道
        ctx.close();
    }
}
