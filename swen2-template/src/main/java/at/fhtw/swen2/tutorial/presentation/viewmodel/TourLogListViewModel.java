package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
@Getter
public class TourLogListViewModel {
    private final ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();
    private List<TourLog> tourLogItems = new LinkedList<>(); //master Data
    private final TourLogService tourLogService;

    public TourLogListViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;
    }

    public void addItem(TourLog tourLog) {
        tourLogListItems.add(tourLog);
        tourLogItems.add(tourLog);
    }

    public void deleteSelectedTourLog() {
        TourLog selectedTourLogToDelete = this.selectedTourLog.get();
        if (selectedTourLogToDelete == null) {
            return;
        }
        tourLogListItems.remove(selectedTourLogToDelete);
        tourLogItems.remove(selectedTourLogToDelete);
        tourLogService.deleteTourLogById(selectedTourLogToDelete.getId());
    }

    public void displayTourLogList(Long tourId) {
        clearItems();

        System.out.println("-------------------------");
        tourLogService.findAllTourLogsByTourId(tourId)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .subscribe(tourLog -> Platform.runLater(() -> addItem(tourLog)), error -> log.error("Error while loading tour logs", error));
    }

    public void clearItems() {
        tourLogListItems.clear();
    }

    public void initList() {
        // TODO: this is only temporal to display some data for now this should be deleted since no element is selected at start
        if (!tourLogListItems.isEmpty())
            tourLogService.findAllTourLogsByTourId(tourLogListItems.get(0).getId())
                    .subscribeOn(Schedulers.boundedElastic())
                    .publishOn(Schedulers.parallel())
                    .flatMapIterable(tourLogs -> tourLogs)
                    .subscribe(tourLog -> Platform.runLater(() -> addItem(tourLog)), error -> log.error("Error while loading tour logs", error));


        log.info(String.valueOf(tourLogListItems.size()));
    }

    public void updateTourLog(TourLog tourLog) {
        tourLogListItems.remove(selectedTourLog.get());
        tourLogItems.remove(selectedTourLog.get());
        tourLogListItems.add(tourLog);
        tourLogItems.add(tourLog);
        selectedTourLog.set(tourLog);
    }

    public void filterList(String searchText) {
        log.info("Filtering list with search text: " + searchText);
        Task<List<TourLog>> task = new Task<>() {
            @Override
            protected List<TourLog> call() {
                updateMessage("Loading data");

                return tourLogItems.stream()
                        .filter(value -> value.getComment().toLowerCase().contains(searchText.toLowerCase())
                                || value.getDifficulty().toLowerCase().contains(searchText.toLowerCase())
                                || value.getRating().toString().contains(searchText.toLowerCase())
                                || value.getDate().contains(searchText.toLowerCase())
                                || value.getId().toString().contains(searchText.toLowerCase())
                                || value.getTotalTime().toString().contains(searchText.toLowerCase())
                        ).toList();
            }
        };

        task.setOnSucceeded(event -> tourLogListItems.setAll(task.getValue()));

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
