package atm_service_api.atm_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ATM {

    private final Card card;

    public synchronized void withDrawMoney(Integer amount) {
        card.withdrawMoney(amount);
    }

    public synchronized void putMoney(Integer amount) {
        card.putMoney(amount);
    }
}
