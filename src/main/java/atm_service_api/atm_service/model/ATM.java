package atm_service_api.atm_service.model;

import atm_service_api.atm_service.exception.CardOperationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATM {

    private final ReentrantLock lock = new ReentrantLock();
    private boolean isTransactionActive = false;
    private Card card;

    public void withDrawMoney(Integer amount) {
        lock.lock();
        try {
            isTransactionActive = true;
            card.withdrawMoney(amount);
        } finally {
            lock.unlock();
            isTransactionActive = false;
        }
    }

    public void putMoney(Integer amount) {
        lock.lock();
        try {
            isTransactionActive = true;
            card.putMoney(amount);
        } finally {
            lock.unlock();
            isTransactionActive = true;
        }
    }

    public void insertCard(Card inputCard) {
        lock.lock();
        try {
            if (Objects.isNull(inputCard)) {
                throw new IllegalArgumentException("input card cannot be null");
            }
                if (!Objects.isNull(card)) {
                throw new CardOperationException("Card already inserted");
            }
            card = inputCard;
        } finally {
            lock.unlock();
        }

    }

    public Card pullCard() {
        lock.lock();
        try {
            if (isTransactionActive) {
                throw new CardOperationException("We are cannot pull card while a transaction is active");
            }
            if (Objects.isNull(card)) {
                throw new CardOperationException("Card didn't inserted");
            }
            var cardForResponse = card;
            card = null;
            return cardForResponse;
        } finally {
            lock.unlock();
        }
    }

    public int getBalanceCard() {
        if (Objects.isNull(card)) {
            throw new CardOperationException("Card didn't inserted");
        }
        return card.getBalance();
    }
}
