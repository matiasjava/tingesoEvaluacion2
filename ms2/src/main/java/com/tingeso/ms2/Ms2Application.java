package com.tingeso.ms2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Ms2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ms2Application.class, args);
	}

}
