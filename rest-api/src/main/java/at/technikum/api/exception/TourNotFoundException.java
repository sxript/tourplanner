package at.technikum.api.exception;

public class TourNotFoundException extends RuntimeException {
    public TourNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
