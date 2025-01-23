package atm_service_api.atm_service.exception;

public class IncorrectAmountException extends RuntimeException {
    private String message;

    public IncorrectAmountException(String message) {
        super(message);
    }
}
