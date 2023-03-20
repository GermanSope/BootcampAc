package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import utils.CardUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    public Card() {
    }

    public Card(CardType type, CardColor color, Client client) {
        this.cardholder = client.getFirstName() + " " + client.getLastName();
        this.type = type;
        this.color = color;
        this.number = CardUtils.createNumber(4) + "-"+ CardUtils.createNumber(4) + "-" + CardUtils.createNumber(4) + "-" + CardUtils.createNumber(4);
        this.cvv = CardUtils.createNumber(3);
        this.fromDate = LocalDate.now();
        this.thruDate = fromDate.plusYears(5);
        this.client = client;
    }

    public Long getId() {
        return id;
    }


    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }



}
