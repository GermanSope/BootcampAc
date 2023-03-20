package com.mindhub.homebanking.demo;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


/*Las anotaciones @DataJpaTest y @AutoConfigureTestDatabase(replace = NONE) indican a Spring que debe escanear en busca de clases
@Entity y configurar los repositorios JPA. Además hace que las operaciones realizadas en la base de datos sean por defecto transaccionales
para que luego de ejecutarlas sean revertidas y no afecten los datos reales fuera de las pruebas, así como también indicar que se quiere conectar
a una base de datos real y no a una embebida en la aplicación H2.*/

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {


    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //verifica q existan loans
    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    //verifica si existe el prestamo con el nombre "Personal".
    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existXXLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("XX"))));
    }

    @Test
    public void containEmail() {
        List<Client> clients = clientRepository.findAll();
        for (Client cli : clients) {
            assertThat(cli.getEmail(), containsString("@"));
        }
    }

//    @Test
//    public void nameWhithOutSpace() {
//        List<Client> clients = clientRepository.findAll();
//        for (Client cli : clients) {
//            assertThat(cli.getFirstName(), equalToIgnoringWhiteSpace(cli.getFirstName()));
//        }
//    }

    @Test
    public void passwordLength(){
        List<Client> clients = clientRepository.findAll();
        for (Client cli : clients) {
            assertThat(cli.getPassword().length(), greaterThanOrEqualTo(8));
        }
    }

    @Test
    public void cardNumberLength(){
        List<Card> cards = cardRepository.findAll();
        for(Card cs: cards){
            assertThat(cs.getNumber().length(), comparesEqualTo(19));
        }
    }

    @Test
    public void FromDateIsTodayOrPriorToToday(){
        List<Card> cards = cardRepository.findAll();
        for (Card card: cards){
            assertThat(card.getFromDate(), lessThanOrEqualTo(LocalDate.now()));
        }
    }

//    @Test
//    public void FromDateIsTodayOrPriorToToday(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, everyItem(hasProperty("fromDate", lessThanOrEqualTo(LocalDate.now()))));
//    }




}
