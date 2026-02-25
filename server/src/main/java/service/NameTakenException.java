package service;

public class NameTakenException extends RuntimeException {
    public NameTakenException(String message) {
        super(message);
    }
}
