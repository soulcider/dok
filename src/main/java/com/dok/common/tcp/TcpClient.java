package com.dok.common.tcp;

import java.nio.charset.Charset;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class TcpClient {

    private EventLoopGroup group     = null;
    private Bootstrap      bootstrap = null;


    public TcpClient(String host, int port, String chset) {

        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group)
                 .remoteAddress(host, port)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("encoder", new StringEncoder(Charset.forName(chset)));
                        pipeline.addLast("decoder", new StringDecoder(Charset.forName(chset)));
                        pipeline.addLast("handler", new TcpClientHandler());
                    }
                  });
    }

    public void sendMsg(String msg) throws Exception {
        try {
            Channel channel = bootstrap.connect().sync().channel();
            if(channel != null) {
                channel.writeAndFlush(msg).sync();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            TcpClient client = new TcpClient("127.0.0.1", 80, "UTF-8");
            for (int i = 0; i < 100000; i++) {
                client.sendMsg(i + "번-요청");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}