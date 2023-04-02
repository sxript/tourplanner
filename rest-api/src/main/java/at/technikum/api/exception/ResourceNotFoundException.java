package at.technikum.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Could not find resource with " + id);
    }

    public ResourceNotFoundException(String message, Long id) {
        super(message + id);
    }
}
