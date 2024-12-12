package com.fifteen.eureka.vpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
public class VpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpoApplication.class, args);
	}

}
