package atm_service_api.atm_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ATM {

    private final Card card;

    public void withDrawMoney(Integer amount) {
        synchronized (card) {
            card.withdrawMoney(amount);
        }
    }

    public void putMoney(Integer amount) {
        synchronized (card) {
            card.putMoney(amount);
        }
    }
}
