package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import org.springframework.security.core.Authentication;
import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    ;

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    ;

    @GetMapping("/clients/{id}/cards")
    public List<CardDTO> getCards(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get().getCards().stream().map(CardDTO::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }


  /*  @PostMapping("/clients/{clientId}/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             @RequestParam String number, @RequestParam String cvv,
                                             @PathVariable Long clientId) {

        //Ojo! El cliente no puede tener mas de tres Cards del mismo cardType
        //Si tiene mas de tres
        //return new ResponseEntity<>("You already have 3 " + cardType + " cards", HttpStatus.FORBIDDEN
        if (Objects.isNull(type) || Objects.isNull(color) || number.isEmpty() || cvv.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        //client.get().getCards().stream().filter(card -> card.getType() == cardType).count() >= 3
        //alternativa con lamda obtengo las cards del cliente y las filtro por el type y despues las cuento.

        if (type == CardType.CREDIT && clientRepository.findById(clientId).get().creditCardAmount() >= 3) {
            return new ResponseEntity<>("You already have 3 " + type + " cards", HttpStatus.FORBIDDEN);
        }

        if (type == CardType.DEBIT && clientRepository.findById(clientId).get().debitCardAmount() >= 3) {
            return new ResponseEntity<>("You already have 3 " + type + " cards", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findById(clientId).isPresent()) {
            Card card = new Card(type, color, number, cvv, LocalDate.now(), clientRepository.findById(clientId).get());
            try {
                cardRepository.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("User dont exist", HttpStatus.FORBIDDEN);
        }

    } */

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, CardColor cardColor, Authentication authentication) {

        if (Objects.isNull(cardType) || Objects.isNull(cardColor)) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if (cardType == CardType.CREDIT && clientRepository.findByEmail(authentication.getName()).get().creditCardAmount() >= 3) {
            return new ResponseEntity<>("You already have 3 " + cardType + " cards", HttpStatus.FORBIDDEN);
        }

        if (cardType == CardType.DEBIT && clientRepository.findByEmail(authentication.getName()).get().debitCardAmount() >= 3) {
            return new ResponseEntity<>("You already have 3 " + cardType + " cards", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(authentication.getName()).isPresent()) {
            Card card = new Card(cardType, cardColor, clientRepository.findByEmail(authentication.getName()).get());
            try {
                cardRepository.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("User dont exist", HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id){
        cardRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
