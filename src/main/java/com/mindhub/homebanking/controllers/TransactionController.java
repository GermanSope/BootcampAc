package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());
    }

    ;

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    ;

    @GetMapping("/transactions/betweenDate/{date1}{date2}")
    public List<TransactionDTO> getTransactionsBetweenDates(@PathVariable LocalDateTime date1, @PathVariable LocalDateTime date2) {
        return transactionRepository.findByDateBetween(date1, date2).stream().map(TransactionDTO::new).collect(toList());
    }

    ;

    @GetMapping("/transactions/betweenAmount/{max}{min}")
    public List<TransactionDTO> getTransactionsBetweenAmount(@PathVariable double max, @PathVariable double min) {
        return transactionRepository.findByAmountBetween(max, min).stream().map(TransactionDTO::new).collect(toList());
    }

    ;

    @GetMapping("/transactions/type/{type}")
    public List<TransactionDTO> getTransactionsType(@PathVariable TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(toList());
    }

    ;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam Double amount, @RequestParam String description,
                                                    @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                    Authentication authentication) {

        if (amount.isNaN() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (amount<=0 ){
            return new ResponseEntity<>("No se puede transferir numero negativo", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Son la misma cuenta", HttpStatus.FORBIDDEN);
        }

        Optional<Account> account1 = accountRepository.findByNumber(fromAccountNumber);
        Optional<Account> account2 = accountRepository.findByNumber(toAccountNumber);


        if (account1.isEmpty()) {
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }
        if (account2.isEmpty()) {
            return new ResponseEntity<>("No existe la cuenta", HttpStatus.FORBIDDEN);
        }

        if (!clientRepository.findByEmail(authentication.getName()).get().getAccounts().contains(account1.get())) {
            return new ResponseEntity<>("La cuenta no te pertenece", HttpStatus.FORBIDDEN);
        }

        if (account1.get().getBalance() < amount) {
            return new ResponseEntity<>("No tiene saldo suficiente", HttpStatus.FORBIDDEN);
        }


        Transaction transaction1 = new Transaction(TransactionType.DEBIT, amount, description + " - " + account2.get().getNumber(), LocalDateTime.now(), account1.get());
        Transaction transaction2 = new Transaction(TransactionType.CREDIT, amount, description + " - " + account1.get().getNumber(), LocalDateTime.now(), account2.get());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        account1.get().setBalance(account1.get().getBalance() - amount);
        account2.get().setBalance(account2.get().getBalance() + amount);
        accountRepository.save(account1.get());
        accountRepository.save(account2.get());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
