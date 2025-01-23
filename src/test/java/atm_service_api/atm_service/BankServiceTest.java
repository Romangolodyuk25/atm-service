package atm_service_api.atm_service;

import atm_service_api.atm_service.exception.CardOperationException;
import atm_service_api.atm_service.model.ATM;
import atm_service_api.atm_service.model.Card;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class BankServiceTest {

    private final Random random = new Random();

    @Test
    public void shouldReturnAmount1000() {
        var card = new Card(new AtomicInteger(1000));
        var amount = 1000;
        var atmsValue = random.nextInt(100) + 1;

        var executorService = Executors.newFixedThreadPool(atmsValue);
        var atms = createAtms(atmsValue, card);

        for (int i = 0; i < atmsValue; i++) {
            var atm = atms.get(i);
            executorService.submit(() -> atm.putMoney(amount));
            executorService.submit(() -> atm.withDrawMoney(amount));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThat(card.getBalance()).isEqualTo(1000);
    }


    @Test
    public void shouldThrowExceptionWhenTransactionIsActive() {
        var card = new Card(new AtomicInteger(1000));
        var atm = new ATM();

        atm.insertCard(card);
        var executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> atm.withDrawMoney(500));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        assertThatThrownBy(() -> atm.pullCard())
                .isInstanceOf(CardOperationException.class)
                .hasMessage("We are cannot pull card while a transaction is active");

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Test interrupted", ex);
        }
    }

    @Test
    public void shouldThrowExceptionWhenTryingPullCardButCardAlreadyInsert() {
        var card = new Card(new AtomicInteger(1000));
        var atm = new ATM();

        atm.insertCard(card);

        assertThatThrownBy(() -> atm.insertCard(card))
                .isInstanceOf(CardOperationException.class)
                .hasMessage("Card already inserted or there is a transaction in progress");
    }

    private List<ATM> createAtms(int value, Card card) {
        List<ATM> atmList = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            ATM atm = new ATM();
            atm.insertCard(card);
            atmList.add(atm);
        }

        return atmList;
    }
}
