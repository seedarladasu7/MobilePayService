package com.service.banking.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "http://mobile-service/MobileBankingServices/orders")
public interface MobileBankingServiceClient {
	
	/*@GetMapping("")
	public List<Order> getAll();
	
	@GetMapping("/info")
	public String getPort();
	
	@GetMapping("/{userId}")
	public List<Order> getUserOrdersById(@PathVariable("userId") String userId);
	
	@GetMapping("/byReqParam")
	public List<Order> getUserOrdersByReqParam(@RequestParam("userId") String userId);
	
	@PostMapping("/postParam")
	public List<Order> testPostWithParam(@RequestParam("userId") String userId);
	
	@GetMapping("/byBody")
	public Order testReqBody(Order order);*/

}
