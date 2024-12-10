package ca.gbc.orderservice.exception;

public class InventoryServiceUnavailable extends RuntimeException {
    public InventoryServiceUnavailable(String message) {
        super(message);
    }
}
