package com.andy.gomoku;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.andy.gomoku.websocket.GomokuServer;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


@SpringBootApplication
//@EnableTransactionManagement
public class GomokuApplication implements CommandLineRunner {
	
	@Value("${game.server.port}")
	private int port;
	
	public static void main(String[] args) {
		SpringApplication.run(GomokuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		GomokuServer gomokuServer = new GomokuServer();
		InetSocketAddress address = new InetSocketAddress(port);
		ChannelFuture future = gomokuServer.start(address);

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				gomokuServer.destroy();
			}
		});

		future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> paramF) throws Exception {
				System.out.println("服务器关闭");
			}
			
		});
	}
}
