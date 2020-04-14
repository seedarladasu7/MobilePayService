package com.service.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.service.banking.config.MobilepayRibbonConfiguration;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RibbonClient(name = "mobilepayclient", configuration = MobilepayRibbonConfiguration.class)
public class BankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}

}
