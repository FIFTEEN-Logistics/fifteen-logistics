package com.fifteen.eureka.vpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.fifteen.eureka.vpo",
		"com.fifteen.eureka.common"
})
@EnableFeignClients
public class VpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpoApplication.class, args);
	}

}
