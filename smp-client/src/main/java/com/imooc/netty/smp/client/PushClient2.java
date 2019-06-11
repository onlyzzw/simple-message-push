package com.imooc.netty.smp.client;

import com.imooc.netty.smp.client.handler.PushMessageHandler;
import com.imooc.netty.smp.client.handler.codec.PushAckEncoder;
import com.imooc.netty.smp.client.handler.codec.PushMessageDecoder;
import com.imooc.netty.smp.common.handler.spliter.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushClient2 {
    private static final Logger logger = LoggerFactory.getLogger(PushClient2.class);

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
        PushClient2 pushClient = new PushClient2();
        pushClient.start();
    }
//start
    private void start() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
   
        //revert
//        System.out.printf(bootstrap.toString());
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            protected void initChannel(NioSocketChannel ch) {
//                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                ch.pipeline().addLast(new Spliter());
                ch.pipeline().addLast(new PushMessageDecoder());
                ch.pipeline().addLast(new PushAckEncoder());
                ch.pipeline().addLast(PushMessageHandler.INSTANCE);
            }
        });

        while (!Thread.interrupted()) {
            ChannelFuture channelFuture = bootstrap.connect(HOST, PORT);
            channelFuture.get();
            if (!channelFuture.isSuccess()) {
                logger.error("something has wrong, bye!");
                break;
            }

            Thread.sleep(50);
        }

    }
}
