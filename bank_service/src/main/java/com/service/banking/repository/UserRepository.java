package com.service.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.banking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

}
