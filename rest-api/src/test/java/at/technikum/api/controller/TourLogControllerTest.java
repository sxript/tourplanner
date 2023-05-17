package at.technikum.api.controller;
import at.technikum.api.service.TourLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TourLogControllerTest {
    @Mock
    private TourLogsService tourLogsService;

    private TourLogController tourLogController;

    @BeforeEach
    void setup() {
        tourLogController = new TourLogController(tourLogsService);
    }





}
