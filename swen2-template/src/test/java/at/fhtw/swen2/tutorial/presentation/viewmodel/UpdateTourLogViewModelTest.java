package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateTourLogViewModelTest {
    @Mock
    TourLogService tourLogService;

    @Mock
    TourService tourService;

    TourLogListViewModel tourLogListViewModel = spy(new TourLogListViewModel(tourLogService, tourService, null));

    @InjectMocks
    UpdateTourLogViewModel updateTourLogViewModel;

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
    @DisplayName("UpdateTourLog")
    class UpdateTourLog {

        @Test
        public void shouldHandleEmptyOrInvalidFieldsWhenUpdatingTourLog() {
            Mono<Boolean> result = updateTourLogViewModel.updateTourLog();
            assertFalse(result.block());

            verifyNoInteractions(tourLogListViewModel);
            verifyNoInteractions(tourLogService);

        }

        @Test
        public void shouldUpdateTourLogSuccessfully() {

            updateTourLogViewModel.getCommentProperty().set("Comment");
            updateTourLogViewModel.getDurationProperty().set("4");
            updateTourLogViewModel.getDifficultyProperty().set("Medium");
            updateTourLogViewModel.getRatingProperty().set("5");
            ObjectProperty<TourLog> selectedTourLogProperty = new SimpleObjectProperty<>();
            TourLog selectedTourLog = TourLog.builder().id(1L).build();
            selectedTourLogProperty.set(selectedTourLog);

            when(tourLogListViewModel.getSelectedTourLog()).thenReturn(selectedTourLogProperty);

            TourLog updatedTourLog = new TourLog();
            when(tourLogService.updateTourLog(any(TourLog.class))).thenReturn(Mono.just(updatedTourLog));


            Mono<Boolean> result = updateTourLogViewModel.updateTourLog();

            assertTrue(result.block());
            verify(tourLogListViewModel).updateTourLog(updatedTourLog);
            verify(tourLogService).updateTourLog(any(TourLog.class));

        }

        @Test
        public void shouldHandleErrorWhenUpdatingTourLog() {

            updateTourLogViewModel.getCommentProperty().set("Comment");
            updateTourLogViewModel.getDurationProperty().set("4");
            updateTourLogViewModel.getDifficultyProperty().set("Medium");
            updateTourLogViewModel.getRatingProperty().set("5");

            ObjectProperty<TourLog> selectedTourLogProperty = new SimpleObjectProperty<>();
            TourLog selectedTourLog = TourLog.builder().id(1L).build();
            selectedTourLogProperty.set(selectedTourLog);

            when(tourLogListViewModel.getSelectedTourLog()).thenReturn(selectedTourLogProperty);


            Throwable error = new RuntimeException("Update error");
            when(tourLogService.updateTourLog(any(TourLog.class))).thenReturn(Mono.error(error));

            Mono<Boolean> result = updateTourLogViewModel.updateTourLog();
            assertFalse(result.block());



            verify(tourLogListViewModel, never()).updateTourLog(any(TourLog.class));
            verify(tourLogService).updateTourLog(any(TourLog.class));

        }

    }

}
