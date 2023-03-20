package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @GetMapping("/clientloans")
    public List<ClientLoanDTO> getClientLoans() {
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(toList());
    };

    @GetMapping("/clientloans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable Long id){
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    };

    @GetMapping("/clientloans/client/{id}")
    public List<ClientLoanDTO> getClientLoansClient(@PathVariable Long id) {
        return clientLoanRepository.findByClient_id(id).stream().map(ClientLoanDTO::new).collect(toList());
    };

    @GetMapping("/clientloans/amountGreater/{amountMin}")
    public List<ClientLoanDTO> getClientLoansAmountGreater(@PathVariable double amountMin) {
        return clientLoanRepository.findByAmountGreaterThan(amountMin).stream().map(ClientLoanDTO::new).collect(toList());
    };

    @GetMapping("/clientloans/clientAmount/{id}{amountMax}")
    public List<ClientLoanDTO> getClientLoansClientAmount(@PathVariable Long id, @PathVariable double amountMax) {
        return clientLoanRepository.findByClient_idAndAmountLessThan(id, amountMax).stream().map(ClientLoanDTO::new).collect(toList());
    };


}
