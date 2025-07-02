package com.Finance.BankingandExpensePlanner.repository;


import com.Finance.BankingandExpensePlanner.model.Account;
import com.Finance.BankingandExpensePlanner.model.Transactions;
import com.Finance.BankingandExpensePlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUser(User user);
    List<Transactions> findByAccount(Account account);
}
