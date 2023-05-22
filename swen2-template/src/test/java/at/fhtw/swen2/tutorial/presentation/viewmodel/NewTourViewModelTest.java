package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewTourViewModelTest {

    @Mock
    TourService tourService;
    @Mock
    TourListViewModel tourListViewModel;

    @InjectMocks
    NewTourViewModel newTourViewModel;

    @BeforeAll
    static void initJfxRuntime() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            // Platform already started; ignore
        }
    }


    @Nested
    @DisplayName("AddNewTour")
    class AddNewTour {
        @Test
        public void shouldReturnFalseWhenEmptyFields() { //Add tour
            Tour tour = new Tour();
            Mono<Boolean> result = newTourViewModel.addNewTour();

            assertFalse(result.block());
            verify(tourListViewModel, never()).addItem(tour);
            verify(tourService, never()).saveTour(tour);
            verifyNoInteractions(tourService);
        }


        @Test
        public void shouldAddNewTour() {

            new Tour();
            Tour tour = Tour.builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();


            newTourViewModel.getNameProperty().set("TestName");
            newTourViewModel.getFromProperty().set("Deinleingasse");
            newTourViewModel.getToProperty().set("Hauptbahnhof");
            newTourViewModel.getDescriptionProperty().set("Description");
            newTourViewModel.getTransportTypeProperty().set("Walking");

            when(tourService.saveTour(tour))
                    .thenReturn(Mono.just(tour));

            Mono<Boolean> result = newTourViewModel.addNewTour();
            assertTrue(result.block());

            verify(tourListViewModel, times(1)).addItem(tour);
            verify(tourService, times(1)).saveTour(tour);
            verifyNoMoreInteractions(tourService);


        }

        @Test
        public void shouldResumeOnErrorWhenAddingNewTour() {

            new Tour();
            Tour tour = Tour.builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();

            newTourViewModel.getNameProperty().set("TestName");
            newTourViewModel.getFromProperty().set("Deinleingasse");
            newTourViewModel.getToProperty().set("Hauptbahnhof");
            newTourViewModel.getDescriptionProperty().set("Description");
            newTourViewModel.getTransportTypeProperty().set("Walking");

            when(tourService.saveTour(tour))
                    .thenReturn(Mono.error(new RuntimeException("Some error message")));
            Mono<Boolean> result = newTourViewModel.addNewTour();

            assertFalse(result.block());
            verify(tourListViewModel, never()).addItem(tour);
            verify(tourService, times(1)).saveTour(tour);
            verifyNoMoreInteractions(tourService);

        }

    }


}
