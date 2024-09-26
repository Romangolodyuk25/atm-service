package atm_service_api.atm_service.service;

import atm_service_api.atm_service.model.ATM;
import atm_service_api.atm_service.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class BankService {

    public Integer withAndPutDrawMoney(Card card, Integer amount, Integer atmsValue) {
        log.debug("Card balance: {} ", card.getBalance());
        log.debug("Input amount: {}, input atmsValue: {}", amount, atmsValue);

        List<ATM> atmList = createAtms(atmsValue, card);
        ExecutorService executor = Executors.newFixedThreadPool(atmList.size());

        for (ATM atm : atmList) {
                Thread thread1 = new Thread(() -> atm.putMoney(amount));
            executor.submit(thread1);

            try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Thread thread = new Thread(() -> atm.withDrawMoney(amount));
                executor.submit(thread);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            System.out.println("Задачи не завершились");
        }
        return card.getBalance().get();
    }

    public List<ATM> createAtms(int value, Card card) {
        List<ATM> atmList = new ArrayList<>();
        for (int i = 0; i <= value; i++) {
            atmList.add(new ATM(card));
        }

        return atmList;
    }
}
