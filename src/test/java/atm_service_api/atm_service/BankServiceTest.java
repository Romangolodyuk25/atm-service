package atm_service_api.atm_service;

import atm_service_api.atm_service.model.Card;
import atm_service_api.atm_service.service.BankService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class BankServiceTest {

    @Autowired
    private BankService bankService;

    private final Random random = new Random();

    @BeforeEach

    @Test
    public void shouldReturnAmount1000() {
        var card = new Card(new AtomicInteger(1000));
        var amount = 1000;
        var atmsValue = random.nextInt(50);

        var executorService = Executors.newFixedThreadPool(atmsValue);

        for (int i = 0; i < atmsValue; i++) {
            executorService.submit(() -> bankService.withAndPutDrawMoney(card, amount, atmsValue));
        }
        Assertions.assertThat(card.getBalance()).isEqualTo(1000);
    }
}
