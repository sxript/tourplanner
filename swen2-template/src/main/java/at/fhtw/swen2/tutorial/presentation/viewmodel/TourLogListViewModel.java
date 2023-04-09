package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
public class TourLogListViewModel {
    private final ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();
    private List<TourLog> tourLogItems = new LinkedList<>();//master Data
    @Autowired
    private TourLogService tourLogService;

    public TourLogListViewModel() {
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

        tourLogItems = tourLogService.findAllTourLogsByTourId(tourId);
        tourLogListItems.addAll(tourLogItems);

        System.out.println(tourLogListItems.size());
    }

    public void clearItems() {
        tourLogListItems.clear();
    }

    public void initList() {
        // TODO: this is only temporal to display some data for now this should be deleted since no element is selected at start


        if (!tourLogListItems.isEmpty())
            tourLogService.findAllTourLogsByTourId(tourLogListItems.get(0).getId()).forEach(this::addItem);

        System.out.println(tourLogListItems.size());
        System.out.println(tourLogListItems);
        log.info(String.valueOf(tourLogListItems.size()));
    }

    public void updateTourLog(TourLog tourLog) {
        tourLogListItems.remove(selectedTourLog.get());
        tourLogItems.remove(selectedTourLog.get());
        tourLogListItems.add(tourLog);
        tourLogItems.add(tourLog);
        System.out.println(tourLogListItems);
        selectedTourLog.set(tourLog);
    }

    public void filterList(String searchText) {
        System.out.println(searchText);
        System.out.println(getTourLogListItems().size());


        Task<List<TourLog>> task = new Task<>() {
            @Override
            protected List<TourLog> call() throws Exception {
                updateMessage("Loading data");

                System.out.println(tourLogListItems);

                return tourLogItems
                        .stream()
                        .filter(value -> value.getComment().toLowerCase().contains(searchText.toLowerCase())
                                || value.getDifficulty().toLowerCase().contains(searchText.toLowerCase())
                                || value.getRating().toString().contains(searchText.toLowerCase())
                                || value.getDate().contains(searchText.toLowerCase())
                                || value.getId().toString().contains(searchText.toLowerCase())
                                || value.getTotalTime().toString().contains(searchText.toLowerCase())

                        )
                        .collect(Collectors.toList());
            }
        };

        //TODO: fix getDuration -> man bekommt null von der DB zurück

        System.out.println(task.getValue());

        task.setOnSucceeded(event -> {
            tourLogListItems.setAll(task.getValue());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }


}
