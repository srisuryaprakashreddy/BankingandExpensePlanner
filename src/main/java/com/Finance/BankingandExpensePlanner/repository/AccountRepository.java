package com.Finance.BankingandExpensePlanner.repository;


import com.Finance.BankingandExpensePlanner.model.Account;
import com.Finance.BankingandExpensePlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
}
