package atm_service_api.atm_service.model;

import atm_service_api.atm_service.exception.IncorrectAmountException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;


@Data
@AllArgsConstructor
public class Card {

    private volatile AtomicInteger balance;

    public synchronized void withdrawMoney(Integer amount) {
        System.out.println("Сумма для снятия " + amount);
        checkAmount(amount);

        if (amount <= 0) {
            throw new IncorrectAmountException("There are not enough funds in the account");
        }

        int currentBalance = getBalance();

        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance.addAndGet(-amount);


        System.out.println("Баланс после снятия " + getBalance());
        System.out.println();
    }

    public synchronized void putMoney(Integer amount) {
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
