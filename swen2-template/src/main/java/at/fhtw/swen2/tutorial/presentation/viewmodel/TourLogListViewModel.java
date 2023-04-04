package at.fhtw.swen2.tutorial.presentation.viewmodel;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TourLogListViewModel {


    private List<TourLog> masterData = new ArrayList<>();
    private ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();
    private TourLogService tourLogService;

    public TourLogListViewModel() {
        this.tourLogService = new TourLogServiceImpl();
    }

    public void addItem(TourLog tourLog) {
        tourLogListItems.add(tourLog);
        masterData.add(tourLog);
    }

    public void clearItems(){ tourLogListItems.clear(); }

    public void initList(){
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
