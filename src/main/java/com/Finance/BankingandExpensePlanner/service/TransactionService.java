package com.Finance.BankingandExpensePlanner.service;


import com.Finance.BankingandExpensePlanner.model.Account;
import com.Finance.BankingandExpensePlanner.model.Transactions;
import com.Finance.BankingandExpensePlanner.model.User;
import com.Finance.BankingandExpensePlanner.repository.AccountRepository;
import com.Finance.BankingandExpensePlanner.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetService budgetService;

    public Transactions saveTransaction(Transactions transaction) {
        Account account = transaction.getAccount();
        boolean isDebit = "EXPENSE".equals(transaction.getType());
        if ("INCOME".equals(transaction.getType())) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        } else if (isDebit) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        }
        accountRepository.save(account);
        Transactions saved = transactionRepository.save(transaction);

        // Update budget spent if category is present and not empty
        if (transaction.getCategory() != null && !transaction.getCategory().isEmpty()) {
            budgetService.updateSpent(
                    transaction.getUser(),
                    transaction.getCategory(),
                    transaction.getAmount(),
                    isDebit,
                    transaction.getDate()
            );
        }
        return saved;
    }

    public List<Transactions> getTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    public List<Transactions> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public Optional<Transactions> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransaction(Long id) {
        Optional<Transactions> transactionOpt = transactionRepository.findById(id);
        if (transactionOpt.isPresent()) {
            Transactions transaction = transactionOpt.get();
            Account account = transaction.getAccount();
            boolean isDebit = "EXPENSE".equals(transaction.getType());

            // Reverse transaction effect on account
            if ("INCOME".equals(transaction.getType())) {
                account.setBalance(account.getBalance() - transaction.getAmount());
            } else if (isDebit) {
                account.setBalance(account.getBalance() + transaction.getAmount());
            }
            accountRepository.save(account);

            // Reverse budget spent if category is present and not empty
            if (transaction.getCategory() != null && !transaction.getCategory().isEmpty()) {
                budgetService.updateSpent(
                        transaction.getUser(),
                        transaction.getCategory(),
                        transaction.getAmount(),
                        !isDebit, // Reverse the debit/credit
                        transaction.getDate()
                );
            }
            transactionRepository.deleteById(id);
        }
    }
}
