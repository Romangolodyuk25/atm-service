package atm_service_api.atm_service.exception;

public class CardOperationException extends RuntimeException {
    private String message;

    public CardOperationException(String message) {
        super(message);
    }
}
