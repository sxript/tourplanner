package at.technikum.api.controller;

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
        @DisplayName("Should return all tours when search query is null")
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
        @DisplayName("Should return empty list when no tours found")
        void shouldReturnEmptyListWhenNoToursFound() {
            when(tourService.getAllTours(null)).thenReturn(new ArrayList<>());

            ResponseEntity<List<Tour>> responseEntity = tourController.getToursBySearchQuery(null);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(Collections.emptyList(), responseEntity.getBody());

            verify(tourService, times(1)).getAllTours(null);
            verifyNoMoreInteractions(tourService);
        }
    }

//    @Test
//    void getTourById() {
//        tours.add(new Tour("Tour 1", "From 1", "To 1", "Transport Type 1", "Description 1", 1.0, 1, null));
//
//        when(tourService.getTourById(1L)).thenReturn(java.util.Optional.of(tour));
//
//        tourController = new TourController(tourService, mapQuestLookupService);
//        ResponseEntity<Tour> responseEntity = tourController.getTourById(1L);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(tour);
//    }
//
//    @Test
//    void getTourById_NotFound() {
//        when(tourService.getTourById(1L)).thenReturn(java.util.Optional.empty());
//
//        tourController = new TourController(tourService, mapQuestLookupService);
//        assertThrows(ResourceNotFoundException.class, () -> tourController.getTourById(1L));
//    }
//
//    @Test
//    void createTour() {
//        Tour newTour = new Tour(null, "New Tour", "From", "To", "Transport Type");
//        Tour createdTour = new Tour(1L, "New Tour", "From", "To", "Transport Type");
//
//        when(mapQuestLookupService.getRouteDirections(any(), any(), any())).thenReturn(Mono.just(new MapReque()));
//        when(mapQuestLookupService.getStaticMap(any(), any())).thenReturn(Mono.just(new byte[0]));
//        when(tourService.createTour(any())).thenReturn(createdTour);
//
//        tourController = new TourController(tourService, mapQuestLookupService);
//        ResponseEntity<Tour> responseEntity = tourController.createTour(newTour).block();
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(responseEntity.getBody()).isEqualTo(createdTour);
//    }
}