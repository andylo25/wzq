package com.andy.gomoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableTransactionManagement
public class GomokuApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GomokuApplication.class, args);
	}
}
