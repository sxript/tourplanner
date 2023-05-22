package at.technikum.api.controller;

import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.TourLog;
import at.technikum.api.service.TourLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TourLogControllerTest {
    @Mock
    private TourLogsService tourLogsService;

    @InjectMocks
    private TourLogController tourLogController;

    @Test
    public void shouldCreateTourLog() {

        Long tourId = 1L;
        TourLog tourLog = new TourLog();
        when(tourLogsService.createTourLog(tourId, tourLog)).thenReturn(tourLog);

        ResponseEntity<TourLog> response = tourLogController.createTourLog(tourId, tourLog);

        verify(tourLogsService, times(1)).createTourLog(tourId, tourLog);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tourLog, response.getBody());
    }

    @Test
    public void shouldUpdateTourLog() {

        Long id = 1L;
        TourLog tourLog = new TourLog();
        when(tourLogsService.updateTourLog(id, tourLog)).thenReturn(tourLog);

        ResponseEntity<TourLog> response = tourLogController.updateTourLog(id, tourLog);

        verify(tourLogsService, times(1)).updateTourLog(id, tourLog);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tourLog, response.getBody());
    }


    @Nested
    @DisplayName("Get /api/v1/logs/{id}")
    class GetTourLogById {
        @Test
        void shouldReturnTourLogForExistingID() {
            TourLog tourLog = new TourLog();
            when(tourLogsService.findTourLogById(anyLong()))
                    .thenReturn(Optional.of(tourLog));
            ResponseEntity<TourLog> response = tourLogController.getTourLogById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(tourLog, response.getBody());

            verify(tourLogsService, times(1)).findTourLogById(eq(1L));
        }

        @Test
        void shouldThrowsResourceNotFoundExceptionForNonExistingId() {
            // Mock the tourLogsService.findTourLogById method
            when(tourLogsService.findTourLogById(anyLong()))
                    .thenReturn(Optional.empty());

            // Call the getTourLogById method
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> tourLogController.getTourLogById(1L));

            assertEquals("No Tour with Id: 1", exception.getMessage());

            // Verify that tourLogsService.findTourLogById was called with the correct id
            verify(tourLogsService, times(1)).findTourLogById(eq(1L));
        }
    }


    @Nested
    @DisplayName("Delete /api/v1/logs/{id}")
    class DeleteTourLog{
        @Test
        void shouldDeleteTourLogForExistingId() {
            when(tourLogsService.deleteTourLogById(anyLong()))
                    .thenReturn(1);

            ResponseEntity<Integer> response = tourLogController.deleteTourLog(1L);

            assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
            assertNull(response.getBody());

            verify(tourLogsService, times(1)).deleteTourLogById(eq(1L));
        }

        @Test
        void shouldThrowsResourceNotFoundExceptionForNonExistingId() {
            when(tourLogsService.deleteTourLogById(anyLong()))
                    .thenReturn(0);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> tourLogController.deleteTourLog(1L));

            assertEquals("No TourLog with Id: 1",exception.getMessage());

            verify(tourLogsService, times(1)).deleteTourLogById(eq(1L));
        }

    }
}




