package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class PracticaBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticaBootApplication.class, args);
	}

	/*@Autowired
	PasswordEncoder passwordEncoder;
	*/




	/*@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {


			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
			clientRepository.save(cliente1);
			Client cliente2 = new Client("German", "Sopeña","gsope@mindhub.com", passwordEncoder.encode("123456"));
			clientRepository.save(cliente2);

			Account account1 = new Account("VIN001", LocalDateTime.now(),5000.00,cliente1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1),7500.00,cliente1);
			accountRepository.save(account2);

			Account account3 = new Account(  500.00, cliente2);
			accountRepository.save(account3);
			Account account4 = new Account( 2000.00, cliente2);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT,700.00,"prueba 1",LocalDateTime.now(),account1);
			transactionRepository.save(transaction1);

			account1.setBalance(account1.refreshBalance(transaction1.getType(),transaction1.getAmount()));

			Transaction transaction2 = new Transaction(TransactionType.DEBIT,300.00,"prueba 2",LocalDateTime.now().minusDays(1),account1);
			transactionRepository.save(transaction2);

			account1.setBalance(account1.refreshBalance(transaction2.getType(),transaction2.getAmount()));

			Transaction transaction3 = new Transaction(TransactionType.CREDIT,1200.00,"prueba 3",LocalDateTime.now().plusDays(2),account2);
			transactionRepository.save(transaction3);

			account2.setBalance(account2.refreshBalance(transaction3.getType(),transaction3.getAmount()));

			Transaction transaction4 = new Transaction(TransactionType.DEBIT,500.00,"prueba 4",LocalDateTime.now(),account2);
			transactionRepository.save(transaction4);

			account2.setBalance(account2.refreshBalance(transaction4.getType(),transaction4.getAmount()));

			accountRepository.save(account1);
			accountRepository.save(account2);

			Loan loan1=new Loan("Hipotecario", 500000.00, List.of(12,24,36,48,60));
			loanRepository.save(loan1);
			Loan loan2=new Loan("Personal", 100000.00, List.of(6, 12, 24),);
			loanRepository.save(loan2);
			Loan loan3=new Loan("Automotriz", 300000.00, List.of(6, 12,24,36));
			loanRepository.save(loan3);

			ClientLoan clientloan1 = new ClientLoan(60, 400000.00,cliente1,loan1);
			clientLoanRepository.save(clientloan1);

			ClientLoan clientloan2 = new ClientLoan(6, 50000.00,cliente1,loan2);
			clientLoanRepository.save(clientloan2);

			ClientLoan clientloan3 = new ClientLoan(24, 100000.00,cliente2,loan2);
			clientLoanRepository.save(clientloan3);

			ClientLoan clientloan4 = new ClientLoan(36, 200000.00,cliente2,loan3);
			clientLoanRepository.save(clientloan4);

			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD,cliente1);
			cardRepository.save(card1);

			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM,cliente1);
			cardRepository.save(card2);

			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER,cliente2);
			cardRepository.save(card3);


		};

	}*/
}