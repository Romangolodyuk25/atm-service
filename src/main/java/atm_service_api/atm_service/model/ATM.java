package atm_service_api.atm_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ATM {

    private final Card card;

    public Integer withDrawMoney(Integer amount) {
        synchronized (card) {
            return card.withdrawMoney(amount);
        }
    }

    public void putMoney(Integer amount) {
        synchronized (card) {
        card.putMoney(amount);
        }
    }
}
