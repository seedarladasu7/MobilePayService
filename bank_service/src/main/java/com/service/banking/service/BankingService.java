package com.service.banking.service;

import java.util.List;

import com.service.banking.dto.AccountDTO;
import com.service.banking.dto.CustomerDTO;
import com.service.banking.dto.FundTransferDTO;
import com.service.banking.dto.TransactionDetails;

public interface BankingService {

	public String registerCustomer(CustomerDTO userDTO);
	
	public String createAccountForCustomer(AccountDTO accDTO);
	
	public String transferFunds(FundTransferDTO ftDTO);
	
	public AccountDTO findAccountByAccountNumber(String acctNumber);
	
	public AccountDTO findAccountById(int acctId);
	
	public List<AccountDTO> findAccountByMobileNumber(String mobileNum);
	
	public List<List<TransactionDetails>> retrieveCustomerBankStatement(int custId, String txnMode);

}
