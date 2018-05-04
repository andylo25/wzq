package com.andy.gomoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.andy.gomoku.ai.WineAI;


@SpringBootApplication
//@EnableTransactionManagement
public class GomokuApplication {
	
	public static void main(String[] args) {
		new WineAI();
		SpringApplication.run(GomokuApplication.class, args);
	}
}
