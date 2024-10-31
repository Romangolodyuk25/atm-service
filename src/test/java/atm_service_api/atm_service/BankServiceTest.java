package atm_service_api.atm_service;

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

@SpringBootTest
public class BankServiceTest {

    private final Random random = new Random();

    @Test
    public void shouldReturnAmount1000() {
        var card = new Card(new AtomicInteger(1000));
        var amount = 1000;
        var atmsValue = random.nextInt(50) + 1;

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

    private List<ATM> createAtms(int value, Card card) {
        List<ATM> atmList = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            atmList.add(new ATM(card));
        }

        return atmList;
    }
}
