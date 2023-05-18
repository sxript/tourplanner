package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.annotations.RouterOperation;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.any;
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

    @Test
    public void testAddNewTour_WithInvalidValidFields() {
        Tour tour = new Tour();
        Mono<Boolean> result = newTourViewModel.addNewTour();
        assertFalse(result.block());
        verify(tourListViewModel, never()).addItem(tour);
    }


    @Test
    public void testAddNewTour_WithValidFields() {

        Tour tour = new Tour().builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();


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

    }

    @Test
    public void testAddNewTour_OnErrorResume() {

        Tour tour = new Tour().builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();

        newTourViewModel.getNameProperty().set("TestName");
        newTourViewModel.getFromProperty().set("Deinleingasse");
        newTourViewModel.getToProperty().set("Hauptbahnhof");
        newTourViewModel.getDescriptionProperty().set("Description");
        newTourViewModel.getTransportTypeProperty().set("Walking");


        when(tourService.saveTour(tour))
                .thenReturn(Mono.error(new RuntimeException("Some error message")));

        Mono<Boolean> result = newTourViewModel.addNewTour();

        assertFalse(result.block());
        assertEquals("Error while creating new tour",newTourViewModel.getFeedbackProperty().getValue());



    }


}
