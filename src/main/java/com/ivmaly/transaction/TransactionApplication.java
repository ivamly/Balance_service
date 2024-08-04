package com.ivmaly.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionApplication {

	public static void main(String[] args) {
		System.out.println("Starting application...");
		SpringApplication.run(TransactionApplication.class, args);
		System.out.println("Application started.");
	}


}
