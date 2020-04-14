package com.service.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.service.mobile.config.BankingRibbonConfiguration;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RibbonClient(name = "mobilepayclient", configuration = BankingRibbonConfiguration.class)
public class MobileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileServiceApplication.class, args);
	}

}
