package at.technikum.api.controller;
import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.Tour;
import at.technikum.api.model.TourLog;
import at.technikum.api.service.TourLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TourLogControllerTest {
    @Mock
    private TourLogsService tourLogsService;

    private TourLogController tourLogController;

    @BeforeEach
    void setup() {
        tourLogController = new TourLogController(tourLogsService);
    }
    
    @Test
    public void createTourLog_ValidTourLog_ReturnsOkResponse() {

        Long tourId = 1L;
        TourLog tourLog = new TourLog();
        when(tourLogsService.createTourLog(tourId, tourLog)).thenReturn(tourLog);

        ResponseEntity<TourLog> response = tourLogController.createTourLog(tourId, tourLog);

        verify(tourLogsService, times(1)).createTourLog(tourId, tourLog);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tourLog, response.getBody());
    }

    @Test
    public void updateTourLog_ValidTourLog_ReturnsOkResponse() {

        Long id = 1L;
        TourLog tourLog = new TourLog();
        when(tourLogsService.updateTourLog(id, tourLog)).thenReturn(tourLog);

        ResponseEntity<TourLog> response = tourLogController.updateTourLog(id, tourLog);

        verify(tourLogsService, times(1)).updateTourLog(id, tourLog);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tourLog, response.getBody());
    }
}




