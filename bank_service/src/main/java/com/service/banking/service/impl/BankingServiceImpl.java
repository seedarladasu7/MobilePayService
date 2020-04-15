package com.service.banking.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.banking.dto.AccountDTO;
import com.service.banking.dto.CustomerDTO;
import com.service.banking.dto.FundTransferDTO;
import com.service.banking.dto.TransactionDetails;
import com.service.banking.entity.Account;
import com.service.banking.entity.Customer;
import com.service.banking.entity.Transaction;
import com.service.banking.repository.AccountRepository;
import com.service.banking.repository.CustomerRepository;
import com.service.banking.repository.TransactionRepository;
import com.service.banking.service.BankingService;

@Service
public class BankingServiceImpl implements BankingService {

	SimpleDateFormat simpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	CustomerRepository custRepository;

	@Autowired
	AccountRepository accRepository;

	@Autowired
	TransactionRepository txnRepository;

	@Override
	public String registerCustomer(CustomerDTO custDTO) {
		Customer customer = new Customer();
		if (custDTO.getCustId() > 0) {
			customer.setCustId(custDTO.getCustId());
		}
		customer.setAge(custDTO.getAge());
		customer.setCustName(custDTO.getCustName());
		customer.setGender(custDTO.getGender());
		customer.setLocation(custDTO.getLocation());
		custRepository.save(customer);
		return "Customer Registered Successfully...";
	}

	@Override
	public String createAccountForCustomer(AccountDTO accDTO) {
		Optional<Customer> custOpt = custRepository.findById(accDTO.getCustId());
		if (custOpt.isPresent()) {
			Customer cust = custOpt.get();
			Account account = new Account();
			account.setAccNumber(accDTO.getAccNumber());
			account.setBankName(accDTO.getBankName());
			account.setAccOpeningOn(java.sql.Timestamp.valueOf(simpDate.format(new Date())));
			account.setMobileNumber(accDTO.getMobileNumber());
			account.setBalance(accDTO.getBalance());
			account.setStatus("open");
			account.setCustomer(cust);
			accRepository.save(account);
			return "Account has been created successfully...";
		}
		return "Account creation failed.";
	}

	@Override
	public AccountDTO findAccountById(int acctId) {
		Optional<Account> acctOptnl = accRepository.findById(acctId);
		if (acctOptnl.isPresent()) {
			Account acct = acctOptnl.get();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			return objectMapper.convertValue(acct, AccountDTO.class);
		}
		return null;
	}

	@Override
	public AccountDTO findAccountByAccountNumber(String acctNumber) {
		Account acctOptnl = accRepository.findByAccNumber(acctNumber);
		if (acctOptnl != null) {
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			return objectMapper.convertValue(acctOptnl, AccountDTO.class);
		}
		return null;
	}

	@Override
	public List<AccountDTO> findAccountByMobileNumber(String mobileNum) {
		Optional<List<Account>> acctsListOptnl = accRepository.findByMobileNumber(mobileNum);

		if (acctsListOptnl.isPresent()) {
			List<Account> acctList = acctsListOptnl.get();
			if (!acctList.isEmpty()) {
				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				return acctList.stream().map(acct -> objectMapper.convertValue(acct, AccountDTO.class))
						.collect(Collectors.toList());
			}
		}
		return new ArrayList<>();
	}

	@Override
	public String transferFunds(FundTransferDTO ftDTO) {

		String message = "";

		Account fromAcc = accRepository.findByAccNumber(ftDTO.getFromAccount());

		Account toAcc = accRepository.findByAccNumber(ftDTO.getToAccount());

		float txnAmount = ftDTO.getTxnAmount();

		if (fromAcc == null || toAcc == null) {
			message += "Invalid Bank details provided...";
		}

		if (StringUtils.isEmpty(ftDTO.getTxnMode())) {
			ftDTO.setTxnMode("web");
		}

		if (StringUtils.isNotEmpty(message)) {
			return message;
		} else {
			if (fromAcc != null) {
				float accBalance = fromAcc.getBalance();

				if (accBalance < ftDTO.getTxnAmount()) {
					message += "Insufficient balance...";
				} else {
					fromAcc.setBalance(fromAcc.getBalance() - ftDTO.getTxnAmount());
					accRepository.save(fromAcc);

					Transaction dtTxn = new Transaction();
					dtTxn.setAccount(fromAcc);
					dtTxn.setTxnType("Debit");
					dtTxn.setTxnDate(java.sql.Timestamp.valueOf(simpDate.format(new Date())));
					dtTxn.setFromAccount(ftDTO.getFromAccount());
					dtTxn.setToAccount(ftDTO.getToAccount());
					dtTxn.setTxnAmount(txnAmount);
					dtTxn.setTxnMode(ftDTO.getTxnMode());
					txnRepository.save(dtTxn);
				}
			}

			if (toAcc != null) {
				toAcc.setBalance(toAcc.getBalance() + ftDTO.getTxnAmount());
				accRepository.save(toAcc);

				Transaction crTxn = new Transaction();
				crTxn.setAccount(toAcc);
				crTxn.setTxnType("Credit");
				crTxn.setTxnDate(java.sql.Timestamp.valueOf(simpDate.format(new Date())));
				crTxn.setFromAccount(ftDTO.getFromAccount());
				crTxn.setToAccount(ftDTO.getToAccount());
				crTxn.setTxnAmount(txnAmount);
				crTxn.setTxnMode(ftDTO.getTxnMode());
				txnRepository.save(crTxn);
			}
			message += "Transaction has been done successfully...";
		}
		return message;
	}

	@Override
	public List<List<TransactionDetails>> retrieveCustomerBankStatement(int custId, String txnMode) {

		String message = "";

		Optional<Customer> custOptnl = custRepository.findById(custId);

		if (custOptnl.isPresent()) {
			Customer customer = custOptnl.get();
			Optional<List<Account>> accOptnl = accRepository.findByCustomer(customer);

			if (accOptnl.isPresent()) {
				List<Account> acctList = accOptnl.get();
				List<List<TransactionDetails>> lists = new ArrayList<>();
				acctList.forEach(account -> {
					Optional<List<Transaction>> txnListOptnl = null;

					if (StringUtils.isEmpty(txnMode)) {
						txnListOptnl = txnRepository.findByAccount(account);
					} else {
						txnListOptnl = txnRepository.findByAccountAndTxnMode(account, txnMode);
					}

					if (txnListOptnl.isPresent()) {
						List<Transaction> txnList = txnListOptnl.get();
						objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
						lists.add(txnList.stream().map(txn -> objectMapper.convertValue(txn, TransactionDetails.class))
								.collect(Collectors.toList()));
					}
				});
				return lists;
			} else {
				message += "Invalid Account for customer: " + customer.getCustName();
			}

		} else {
			message += "Invalid customer with customer id: " + custId;
		}

		return new ArrayList<>();
	}

}
