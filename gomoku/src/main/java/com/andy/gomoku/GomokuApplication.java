package com.andy.gomoku;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.andy.gomoku.dao.DbBatch;
import com.andy.gomoku.websocket.GomokuServer;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


@SpringBootApplication
//@EnableTransactionManagement
public class GomokuApplication implements CommandLineRunner {
	
	@Value("${game.server.port:0}")
	private int port;
	private static boolean gameServerEnable;
	private static GomokuServer gomokuServer = null;
	
	public static void main(String[] args) {
		SpringApplication.run(GomokuApplication.class, args);
	}
	
	public static boolean gameServerEnable(){
		return gameServerEnable;
	}

	@Override
	public void run(String... args) throws Exception {
		gameServerEnable = port > 0;
		if(!gameServerEnable())return;
		gomokuServer = new GomokuServer();
		InetSocketAddress address = new InetSocketAddress(port);
		ChannelFuture future = gomokuServer.start(address);
		future.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> paramF) throws Exception {
				System.out.println("服务器启动完成");
			}
			
		});

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				if(gomokuServer != null){
					gomokuServer.destroy();
				}
			}
		});

		future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> paramF) throws Exception {
				System.out.println("服务器已关闭");
				DbBatch.stopBatch();
				System.out.println("数据落地完成");
				if(gomokuServer == null){
					// 说明手动关闭
					System.exit(0);
				}
			}
			
		});
	}
	
	public static void destroy(){
		if(gomokuServer != null){
			gomokuServer.destroy();
			gomokuServer = null;
		}
	}
	
}
