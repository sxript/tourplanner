package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewTourLogViewModelTest {

    @Mock
    private TourLogListViewModel tourLogListViewModel;


    private TourListViewModel tourListViewModel = spy(new TourListViewModel(null,new DetailTourViewModel(null,null),null));
    @Mock
    private TourLogService tourLogService;

    @Mock
    private TourService tourService;

    @InjectMocks
    private NewTourLogViewModel newTourLogViewModel;

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
    void addNewTourLog_whenFieldsAreEmpty_shouldReturnFalse() {

        Mono<Boolean> result = newTourLogViewModel.addNewTourLog();

        assertFalse(result.block());
        verifyNoInteractions(tourLogService);
        verifyNoInteractions(tourService);
        verifyNoInteractions(tourLogListViewModel);
    }

    @Test
    void shouldSaveNewTourLogAndReturnTrue() {
        Long tourId = 0l;
        Tour tour = Tour.builder().id(tourId).build();
        TourLog tourLog = TourLog.builder().comment("Test comment").totalTime(60).difficulty("Medium").rating(5).build();
        ObjectProperty<Tour> tour2 = new SimpleObjectProperty<>();
        tour2.set(tour);


        when(tourListViewModel.getSelectedTour()).thenReturn(tour2);
        when(tourLogService.saveTourLog(tourId, tourLog)).thenReturn(Mono.just(tourLog));
        when(tourService.findTourById(tourId)).thenReturn(Mono.just(tour));
        when(tourListViewModel.getTourListItems()).thenReturn(FXCollections.observableArrayList());


        newTourLogViewModel.getCommentProperty().set("Test comment");
        newTourLogViewModel.getDurationProperty().set("60");
        newTourLogViewModel.getDifficultyProperty().set("Medium");
        newTourLogViewModel.getRatingProperty().set("5");
        Mono<Boolean> result = newTourLogViewModel.addNewTourLog();

        assertTrue(result.block());
        verify(tourLogService).saveTourLog(tourId, tourLog);
        verify(tourService).findTourById(tourId);
        verify(tourLogListViewModel).addItem(tourLog);
    }


}
