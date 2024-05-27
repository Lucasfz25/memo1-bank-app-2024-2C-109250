package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transaction transaction = new Transaction(cbu, sum * -1);

        account.setBalance(account.getBalance() - sum);
        transactionRepository.save(transaction);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }




        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + sum);

        Transaction transaction = new Transaction(cbu, sum);

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return account;
    }

    public Collection<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Collection<Transaction>> findTransactionByCbu(Long cbu) {
        return transactionRepository.findTransactionsByAccountCbu(cbu);
    }

    public Optional<Transaction> findTransactionById(Long number) {
        return transactionRepository.findById(number);
    }

    public void deleteTransactionById(Long number) {
        Transaction transaction = transactionRepository.findTransactionByNumber(number);
        Account account = accountRepository.findAccountByCbu(transaction.accountCbu);

        account.setBalance(account.getBalance() - transaction.amount);

        accountRepository.save(account);
        transactionRepository.deleteById(number);
    }
}
