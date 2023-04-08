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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Getter
public class TourLogListViewModel {
    private final ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    @Autowired
    private TourLogService tourLogService;

    public TourLogListViewModel() {
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

    public void displayTourLogList(Long tourId){
        clearItems();
        tourLogListItems.addAll(tourLogService.findAllTourLogsByTourId(tourId));
    }
    public void clearItems(){ tourLogListItems.clear(); }

    public void initList(){
        // TODO: this is only temporal to display some data for now this should be deleted since no element is selected at start
        if (!tourLogListItems.isEmpty())
            tourLogService.findAllTourLogsByTourId(tourLogListItems.get(0).getId()).forEach(this::addItem);
        log.info(String.valueOf(tourLogListItems.size()));
    }

//    public void filterList(String searchText){
//        Task<List<TourLog>> task = new Task<>() {
//            @Override
//            protected List<TourLog> call() throws Exception {
//                updateMessage("Loading data");
//                return masterData
//                        .stream()
//                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase()))
//                        .collect(Collectors.toList());
//            }
//        };
//
//        task.setOnSucceeded(event -> {
//            tourLogListItems.setAll(task.getValue());
//        });
//
//        Thread th = new Thread(task);
//        th.setDaemon(true);
//        th.start();
//
//    }


}
