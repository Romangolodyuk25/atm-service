package atm_service_api.atm_service.model;

import atm_service_api.atm_service.exception.CardOperationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATM {

    private final ReentrantLock lock = new ReentrantLock();
    private boolean isTransactionActive = false;
    private Card card;

    public void withDrawMoney(Integer amount) {
        checkingCardExisting(card);
        lock.lock();
        try {
            isTransactionActive = true;
            callSleepWithRandomTime();
            card.withdrawMoney(amount);
        } finally {
            isTransactionActive = false;
            lock.unlock();
        }
    }

    public void putMoney(Integer amount) {
        checkingCardExisting(card);
        lock.lock();
        try {
            isTransactionActive = true;
            callSleepWithRandomTime();
            card.putMoney(amount);
        } finally {
            isTransactionActive = false;
            lock.unlock();
        }
    }

    public void insertCard(Card inputCard) {
        checkingCardExisting(inputCard);
        if (!Objects.isNull(card) || isTransactionActive) {
            throw new CardOperationException("Card already inserted or there is a transaction in progress");
        }
        lock.lock();
        try {
            card = inputCard;
        } finally {
            lock.unlock();
        }

    }

    public Card pullCard() {
        checkingCardExisting(card);
        if (isTransactionActive) {
            throw new CardOperationException("We are cannot pull card while a transaction is active");
        }
        lock.lock();
        try {
            var cardForResponse = card;
            card = null;
            return cardForResponse;
        } finally {
            lock.unlock();
        }
    }

    public int getBalanceCard() {
        checkingCardExisting(card);
        lock.lock();
        try {
            return card.getBalance();
        } finally {
            lock.unlock();
        }
    }

    private void checkingCardExisting(Card card) {
        if (Objects.isNull(card)) {
            throw new CardOperationException("Card didn't inserted");
        }
    }

    private void callSleepWithRandomTime() {
        var sleepTime = ThreadLocalRandom.current().nextInt(10, 1001);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); //лучше восстановить потом статус прервыания для текущего потока?
            throw new RuntimeException("Thread was interrupted during transaction processing");
        }
    }
}
