package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    };

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    };

    @GetMapping("/accounts/balance/{balance}")
    public List<AccountDTO> getAccountBalanceGreater(@PathVariable double balance) {
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(toList());
    };

    @GetMapping("/accounts/creationDate/{creationDate}")
    public List<AccountDTO> getAccountCreationLessThan(@PathVariable LocalDateTime creationDate) {
        return accountRepository.findByCreationDateLessThan(creationDate).stream().map(AccountDTO::new).collect(toList());
    };

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAcounts (Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).get().getAccounts().stream().map(AccountDTO::new).collect(toList());
    }


    //Current y autentication hace referencia al cliente autenticado en el momento.
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        //autentication.getName trae el cliente logueado por el usuario que esta usando(en este caso, el mail)
        Client client = clientRepository.findByEmail(authentication.getName()).get();
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
        }
        //verifico que el q este logueado, tengo el rol de cliente
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Account account = new Account(0.0, client);
            try {
                accountRepository.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>("User dont exist", HttpStatus.FORBIDDEN);
        }
    };

    @DeleteMapping("/deleteAccount/{id}")
    public void deleteAcoount(@PathVariable Long id){
        Optional<Account> account = accountRepository.findById(id);
        transactionRepository.deleteAll(account.get().getTransactions());
        accountRepository.deleteById(id);
    }
}
