package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    List<ClientLoan> findByClient_id(Long client_id);

    List<ClientLoan> findByAmountGreaterThan (double amountMin);

    List<ClientLoan> findByClient_idAndAmountLessThan(Long client_id, double amountMax);
}
