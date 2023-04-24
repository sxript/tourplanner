package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Getter
public class TourLogListViewModel {
    private final ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();
    private final TourLogService tourLogService;

    private final TourListViewModel tourListViewModel;

    private Disposable disposable;

    public TourLogListViewModel(TourLogService tourLogService, @Lazy TourListViewModel tourListViewModel) {
        this.tourLogService = tourLogService;
        this.tourListViewModel = tourListViewModel;
    }

    public void addItem(TourLog tourLog) {
        tourLogListItems.add(tourLog);
    }

    public void deleteSelectedTourLog() {
        TourLog selectedTourLogToDelete = this.selectedTourLog.get();
        if (selectedTourLogToDelete == null) {
            return;
        }
        tourLogListItems.remove(selectedTourLogToDelete);
        tourLogService.deleteTourLogById(selectedTourLogToDelete.getId());
    }

    public void displayTourLogList(Long tourId) {
        if (disposable != null) {
            disposable.dispose();
        }
        clearItems();
        disposable = tourLogService.findAllTourLogsByTourId(tourId, Optional.empty())
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .subscribe(tourLog -> Platform.runLater(() -> addItem(tourLog)), error -> log.error("Error while loading tour logs", error));
    }

    public void clearItems() {
        tourLogListItems.clear();
    }

    public void initList() {
        clearItems();
        if (!tourLogListItems.isEmpty()) {
            tourLogService.findAllTourLogsByTourId(tourListViewModel.getSelectedTour().get().getId(), Optional.empty())
                    .subscribeOn(Schedulers.boundedElastic())
                    .publishOn(Schedulers.parallel())
                    .flatMapIterable(tourLogs -> tourLogs)
                    .subscribe(tourLog -> Platform.runLater(() -> addItem(tourLog)), error -> log.error("Error while loading tour logs", error));
        }

        log.info(String.valueOf(tourLogListItems.size()));
    }

    public void updateTourLog(TourLog tourLog) {
        tourLogListItems.remove(selectedTourLog.get());
        tourLogListItems.add(tourLog);
        selectedTourLog.set(tourLog);
    }

    public void filterList(String searchText) {
        log.info("Filtering list with search text: " + searchText);

        tourLogService.findAllTourLogsByTourId(tourListViewModel.getSelectedTour().get().getId(), Optional.ofNullable(searchText))
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .subscribe(tourLogs -> Platform.runLater(() -> tourLogListItems.setAll(tourLogs)), error -> log.error("Error while loading tour logs", error));
    }
}
