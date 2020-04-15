package com.service.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.mobile.client.BankServiceClient;
import com.service.mobile.dto.AccountDTO;
import com.service.mobile.dto.FundTransferDTO;
import com.service.mobile.dto.TransactionDetails;

@RestController
@RequestMapping("/mobile")
public class MobileServiceController {

	@Autowired
	BankServiceClient bankServiceClient;

	@GetMapping("/appPort")
	public String getBankApplicationPort() {
		return bankServiceClient.getAppPort();
	}

	@GetMapping("/account/byAcctNum/{acctNum}")
	public ResponseEntity<AccountDTO> findAccountByAccNumber(@PathVariable("acctNum") String acctNum) {
		return bankServiceClient.findAccountByAccNumber(acctNum);
	}

	@GetMapping("/account/byMobileNum/{mobileNum}")
	public ResponseEntity<List<AccountDTO>> findAccountByMobileNumber(@PathVariable("mobileNum") String mobileNum) {
		return bankServiceClient.findAccountByMobileNumber(mobileNum);
	}

	@PostMapping("/fundTransfer")
	public ResponseEntity<String> transferFunds(@RequestBody FundTransferDTO ftDTO) {
		return bankServiceClient.transferFunds(ftDTO);
	}

	@GetMapping("/{custId}/statement")
	public ResponseEntity<List<List<TransactionDetails>>> getStatement(@PathVariable("custId") Integer custId,
			@PathVariable("txnMode") String txnMode) {
		return bankServiceClient.getStatement(custId, txnMode);
	}

}
