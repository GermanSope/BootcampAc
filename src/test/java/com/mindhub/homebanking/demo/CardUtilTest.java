package com.mindhub.homebanking.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import utils.CardUtils;

@SpringBootTest
public class CardUtilTest {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.createNumber(5);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
}
