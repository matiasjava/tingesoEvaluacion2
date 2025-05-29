package com.tingeso.ms7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Ms7Application {

	public static void main(String[] args) {
		SpringApplication.run(Ms7Application.class, args);
	}

}
