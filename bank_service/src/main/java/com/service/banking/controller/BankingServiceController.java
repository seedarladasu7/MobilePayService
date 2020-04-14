package com.service.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.banking.dto.AccountDTO;
import com.service.banking.dto.CustomerDTO;
import com.service.banking.dto.FundTransferDTO;
import com.service.banking.service.BankingService;

@RestController
@RequestMapping("/customer")
public class BankingServiceController {

	@Autowired
	BankingService bankingService;

	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody CustomerDTO userDTO) {
		return new ResponseEntity<>(bankingService.registerCustomer(userDTO), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/account")
	public ResponseEntity<String> createAccount(@RequestBody AccountDTO accDTO) {
		return new ResponseEntity<>(bankingService.createAccountForCustomer(accDTO), HttpStatus.ACCEPTED);		
	}
	
	@GetMapping("/account/{accId}")
	public ResponseEntity<AccountDTO> findAccountById(@PathVariable("accId") Integer accId) {
		return new ResponseEntity<>(bankingService.findAccountById(accId), HttpStatus.OK);
	}
	
	@GetMapping("/account/byAcctNum/{acctNum}")
	public ResponseEntity<AccountDTO> findAccountByAccNumber(@PathVariable("acctNum") String acctNum) {
		return new ResponseEntity<>(bankingService.findAccountByAccountNumber(acctNum), HttpStatus.OK);
	}
	
	@GetMapping("/account/byMobileNum/{mobileNum}")
	public ResponseEntity<List<AccountDTO>> findAccountByMobileNumber(@PathVariable("mobileNum") String mobileNum) {
		return new ResponseEntity<>(bankingService.findAccountByMobileNumber(mobileNum), HttpStatus.OK);
	}
	
	
	@PostMapping("/fundTransfer")
	public ResponseEntity<String> transferFunds(@RequestBody FundTransferDTO ftDTO) {
		return new ResponseEntity<>(bankingService.transferFunds(ftDTO), HttpStatus.OK);		
	}

}
