package com.Finance.BankingandExpensePlanner.service;


import com.Finance.BankingandExpensePlanner.model.Account;
import com.Finance.BankingandExpensePlanner.model.Transactions;
import com.Finance.BankingandExpensePlanner.model.Transfer;
import com.Finance.BankingandExpensePlanner.model.User;
import com.Finance.BankingandExpensePlanner.repository.AccountRepository;
import com.Finance.BankingandExpensePlanner.repository.TransactionRepository;
import com.Finance.BankingandExpensePlanner.repository.TransferRepository;
import com.Finance.BankingandExpensePlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Validates the sender's PIN.
     */
    public boolean validatePin(User sender, String pin) {
        if (sender == null || pin == null) return false;
        String storedPinHash = sender.getPin();
        if (storedPinHash == null) return false;
        return passwordEncoder.matches(pin, storedPinHash);
    }

    /**
     * Returns transfers sent by a user.
     */
    public List<Transfer> getTransfersBySender(User sender) {
        return transferRepository.findBySender(sender);
    }

    /**
     * Returns transfers received by a user.
     */
    public List<Transfer> getTransfersByReceiver(User receiver) {
        return transferRepository.findByReceiver(receiver);
    }

    /**
     * Records a transfer as two transactions:
     * - Sender: category 'transfer_send', type 'expense'
     * - Receiver: category 'transfer_received', type 'income'
     * Also records the transfer in the Transfer table.
     */
    @Transactional
    public void recordTransferTransactions(User sender, Account senderAccount, String receiverEmail, Double amount) {
        if (sender == null || senderAccount == null || receiverEmail == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid transfer data.");
        }

        // Find receiver by email
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found."));

        // Find receiver's account (first, or add logic for default)
        List<Account> receiverAccounts = accountRepository.findByUser(receiver);
        if (receiverAccounts.isEmpty()) {
            throw new IllegalArgumentException("Receiver has no account.");
        }
        Account receiverAccount = receiverAccounts.get(0);

        // 1. Record transfer in Transfer table
        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setSenderAccount(senderAccount);
        transfer.setReceiver(receiver);
        transfer.setReceiverAccount(receiverAccount);
        transfer.setAmount(amount);
        transfer.setCompleted(true);
        transferRepository.save(transfer);

        LocalDate today = LocalDate.now();

        // 2. Create transaction for sender (expense)
        Transactions senderTransaction = new Transactions();
        senderTransaction.setAccount(senderAccount);
        senderTransaction.setUser(sender);
        senderTransaction.setAmount(amount);
        senderTransaction.setCategory("transfer_send");
        senderTransaction.setType("EXPENSE");
        senderTransaction.setDescription("Transfer sent to " + receiver.getEmail());
        senderTransaction.setDate(today);
        transactionRepository.save(senderTransaction);

        // 3. Create transaction for receiver (income)
        Transactions receiverTransaction = new Transactions();
        receiverTransaction.setAccount(receiverAccount);
        receiverTransaction.setUser(receiver);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setCategory("transfer_received");
        receiverTransaction.setType("INCOME");
        receiverTransaction.setDescription("Transfer received from " + sender.getEmail());
        receiverTransaction.setDate(today);
        transactionRepository.save(receiverTransaction);

        // NO BALANCE UPDATES!
    }
}
