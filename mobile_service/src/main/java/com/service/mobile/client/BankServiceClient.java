package com.service.mobile.client;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.service.mobile.dto.AccountDTO;
import com.service.mobile.dto.FundTransferDTO;
import com.service.mobile.dto.TransactionDetails;

@FeignClient(name = "http://bank-service/BankingService/customer")
public interface BankServiceClient {

	@GetMapping("/appPort")
	public String getAppPort();

	@GetMapping("/account/byAcctNum/{acctNum}")
	ResponseEntity<AccountDTO> findAccountByAccNumber(@PathVariable("acctNum") String acctNum);

	@GetMapping("/account/byMobileNum/{mobileNum}")
	public ResponseEntity<List<AccountDTO>> findAccountByMobileNumber(@PathVariable("mobileNum") String mobileNum);

	@PostMapping("/fundTransfer")
	public ResponseEntity<String> transferFunds(@RequestBody FundTransferDTO ftDTO);

	@GetMapping("/{custId}/{txnMode}/statement")
	public ResponseEntity<List<List<TransactionDetails>>> getStatement(@PathVariable("custId") Integer custId,
			@PathVariable("txnMode") String txnMode);

}
