package atm_service_api.atm_service.model;

import atm_service_api.atm_service.exception.IncorrectAmountException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class Card {

    private final AtomicInteger balance;

    public Integer withdrawMoney(Integer amount) {
        System.out.println("Сумма для снятия " + amount);
        checkAmount(amount);
        var currentBalance = balance.get();
        var newBalance = currentBalance - amount;

        if (newBalance <= 0) {
            throw new IncorrectAmountException("There are not enough funds in the account");
        }

        while (!balance.compareAndSet(currentBalance, newBalance)) {
            currentBalance = balance.get();
            newBalance = currentBalance - amount;
        }

        System.out.println("Баланс после снятия " + getBalance());
        System.out.println();
        return balance.get();
    }

    public void putMoney(Integer amount) {
        System.out.println("Сумма для пополнения " + amount);
        checkAmount(amount);
        var currentBalance = balance.get();
        var newBalance = currentBalance + amount;

        while (!balance.compareAndSet(currentBalance, newBalance)) {
            currentBalance = balance.get();
            newBalance = currentBalance + amount;
        }

        System.out.println("Баланс после пополнения " + getBalance());
        System.out.println();
    }

    private void checkAmount(Integer amount) {
        if (amount <= 0) {
            throw new IncorrectAmountException("Incorrect input amount");
        }
    }

}
