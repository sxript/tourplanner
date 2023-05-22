package at.technikum.api.controller;

import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.map.MapQuestLookupService;
import at.technikum.api.model.Tour;
import at.technikum.api.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourControllerTest {
    @Mock
    private TourService tourService;
    @Mock
    private MapQuestLookupService mapQuestLookupService;
    private TourController tourController;

    @BeforeEach
    void setup() {
        tourController = new TourController(tourService, mapQuestLookupService);
    }

    @Nested
    @DisplayName("GET /api/v1/tours")
    class GetAllTours {
        @BeforeEach
        void setup() {
            tourController = new TourController(tourService, mapQuestLookupService);
        }

        @Test
        void shouldReturnAllToursWithSearchQueryEmpty() {
            List<Tour> tours = new ArrayList<>();
            tours.add(new Tour("Tour 1", "From 1", "To 1", "Transport Type 1", "Description 1", 1.0, 1, null));
            tours.add(new Tour("Tour 2", "From 2", "To 2", "Transport Type 2", "Description 2", 1.0, 2, null));
            when(tourService.getAllTours(null)).thenReturn(tours);

            ResponseEntity<List<Tour>> responseEntity = tourController.getToursBySearchQuery(null);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(tours, responseEntity.getBody());

            verify(tourService, times(1)).getAllTours(null);
            verifyNoMoreInteractions(tourService);
        }


        @Test
        void shouldReturnEmptyListWhenNoToursFound() {
            when(tourService.getAllTours(null)).thenReturn(new ArrayList<>());

            ResponseEntity<List<Tour>> responseEntity = tourController.getToursBySearchQuery(null);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(Collections.emptyList(), responseEntity.getBody());

            verify(tourService, times(1)).getAllTours(null);
            verifyNoMoreInteractions(tourService);
        }
    }
    @Nested
    @DisplayName("GET /api/v1/tours/{id}")
    class GetTourById{
        @Test
        public void shouldReturnsTour() {
            Long id = 1L;
            Tour tour = new Tour("Tour 1", "From 1", "To 1", "Transport Type 1", "Description 1", 1.0, 1, null);
            when(tourService.getTourById(id)).thenReturn(Optional.of(tour));

            ResponseEntity<Tour> response = tourController.getTourById(id);

            verify(tourService, times(1)).getTourById(id);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(tour, response.getBody());
        }

        @Test
        public void shouldThrowResourceNotFoundException() {
            Long id = 1L;
            when(tourService.getTourById(id)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> tourController.getTourById(id));
            verify(tourService, times(1)).getTourById(id);
        }
    }

    @Nested
    @DisplayName("Delete /api/v1/tours/{id}")
    class DeleteTour{
        @Test
        public void shouldReturnNoContentResponseForExistingTourId() {
            Long id = 1L;
            when(tourService.deleteTour(id)).thenReturn(1);

            ResponseEntity<Tour> response = tourController.deleteTour(id);

            verify(tourService, times(1)).deleteTour(id);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertEquals(null, response.getBody());
        }


        @Test
        public void shouldReturnNoContentResponseForNonExistingTourId() {
            Long id = 1L;
            when(tourService.deleteTour(id)).thenReturn(0);

            assertThrows(ResourceNotFoundException.class, () -> tourController.deleteTour(id));

            verify(tourService, times(1)).deleteTour(id);
        }

    }

}