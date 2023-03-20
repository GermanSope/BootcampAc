package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    ;

    @GetMapping("/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id) {
        return loanRepository.findById(id).map(LoanDTO::new).orElse(null);
    }

    ;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createClientLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {



        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Optional<Loan> loanFound = loanRepository.findById(loanApplicationDTO.getLoanId());
        Optional<Account> accountFound = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        if(loanFound.isEmpty()){
            return new ResponseEntity<>("No existe el prestamo", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loanFound.get().getMaxAmount()) {
            return new ResponseEntity<>("Monto excedido", HttpStatus.FORBIDDEN);
        }

        if (!loanFound.get().getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("La cantidad de cuotas no es correcta", HttpStatus.FORBIDDEN);
        }

        if(accountFound.isEmpty()){
            return new ResponseEntity<>("No existe la cuenta", HttpStatus.FORBIDDEN);
        }

        if (!clientRepository.findByEmail(authentication.getName()).get().getAccounts().contains(accountFound.get())) {
            return new ResponseEntity<>("La cuenta no te pertenece", HttpStatus.FORBIDDEN);
        }

        double amountTotal = loanApplicationDTO.getAmount() * 1.20;
        ClientLoan loanRequested = new ClientLoan(loanApplicationDTO.getPayments(),amountTotal,clientRepository.findByEmail(authentication.getName()).get(),loanFound.get());
        clientLoanRepository.save(loanRequested);
        transactionRepository.save(new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),"loan approved", LocalDateTime.now(),accountFound.get()));
        accountFound.get().setBalance(accountFound.get().getBalance()+loanApplicationDTO.getAmount());
        accountRepository.save(accountFound.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
