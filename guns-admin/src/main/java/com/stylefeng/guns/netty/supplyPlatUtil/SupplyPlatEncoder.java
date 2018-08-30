package com.stylefeng.guns.netty.supplyPlatUtil;

import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MessageToByteEncoder用处是将读取的byte数据转化为用户自己定义的数据类型
 * Created by Vincent on 2018-08-21.
 */
public class SupplyPlatEncoder extends MessageToByteEncoder<String>  {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        try {
            out.writeBytes(msg.getBytes());
        }catch (Exception e){
            RemotingUtil.closeChannel(ctx.channel());
        }
    }
}
