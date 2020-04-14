package com.service.banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.banking.entity.Account;
import com.service.banking.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Account findByAccNumber(String accNumber);

	Optional<List<Account>> findByUser(User user);

}
