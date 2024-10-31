package atm_service_api.atm_service.model;

import atm_service_api.atm_service.exception.IncorrectAmountException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;


@Data
@AllArgsConstructor
public class Card {

    private final AtomicInteger balance;

    public void withdrawMoney(Integer amount) {
        System.out.println("Сумма для снятия " + amount);
        checkAmount(amount);

        int currentBalance = balance.get();

        if (currentBalance < amount || currentBalance == 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }

        balance.addAndGet(-amount);
    }

    public void putMoney(Integer amount) {
        System.out.println("Сумма для пополнения " + amount);
        checkAmount(amount);

        balance.addAndGet(amount);
        System.out.println("Баланс после пополнения " + getBalance());
        System.out.println();
    }

    private void checkAmount(Integer amount) {
        if (amount <= 0) {
            throw new IncorrectAmountException("Incorrect input amount");
        }
    }

    public int getBalance() {
        return balance.get();
    }
}
