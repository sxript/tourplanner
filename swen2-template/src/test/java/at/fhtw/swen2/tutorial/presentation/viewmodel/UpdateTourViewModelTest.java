package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
public class UpdateTourViewModelTest {
    @Mock
    TourService tourService;
    @Mock
    TourListViewModel tourListViewModel;

    @InjectMocks
    UpdateTourViewModel updateTourViewModel;

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
    @DisplayName("UpdateTour")
   class UpdateTour{
       @Test
       public void shouldUpdateTourWithValidFields() {
           Tour tour = Tour.builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();
           Tour updatedTour = Tour.builder().id(2L).build();

           updateTourViewModel.getNameProperty().set("TestName");
           updateTourViewModel.getFromProperty().set("Deinleingasse");
           updateTourViewModel.getToProperty().set("Hauptbahnhof");
           updateTourViewModel.getDescriptionProperty().set("Description");
           updateTourViewModel.getTransportTypeProperty().set("Walking");

           when(tourListViewModel.getSelectedTour())
                   .thenReturn(new SimpleObjectProperty<>(tour));
           when(tourService.updateTour(tour))
                   .thenReturn(Mono.just(updatedTour));

           Mono<Boolean> result = updateTourViewModel.updateTour();

           assertTrue(result.block());
           assertEquals("Tour updated",updateTourViewModel.getFeedbackProperty().getValue());

           verify(tourListViewModel, times(1)).getSelectedTour();
           verify(tourService, times(1)).updateTour(tour);
           verify(tourListViewModel, times(1)).updateTour(updatedTour);
       }
       @Test
       public void shouldResumeOnErrorWhenUpdatingTour() {

           new Tour();
           Tour tour = Tour.builder().name("TestName").from("Deinleingasse").to("Hauptbahnhof").description("Description").transportType("Walking").build();
           Tour updatedTour = Tour.builder().id(2L).build();

           updateTourViewModel.getNameProperty().set("TestName");
           updateTourViewModel.getFromProperty().set("Deinleingasse");
           updateTourViewModel.getToProperty().set("Hauptbahnhof");
           updateTourViewModel.getDescriptionProperty().set("Description");
           updateTourViewModel.getTransportTypeProperty().set("Walking");

           when(tourListViewModel.getSelectedTour())
                   .thenReturn(new SimpleObjectProperty<>(tour));

           when(tourService.updateTour(tour))
                   .thenReturn(Mono.error(new RuntimeException("Some error message")));
           Mono<Boolean> result = updateTourViewModel.updateTour().onErrorReturn(false);

           assertFalse(result.block());
           verify(tourListViewModel, times(1)).getSelectedTour();
           verify(tourService, times(1)).updateTour(tour);

       }

   }


}
