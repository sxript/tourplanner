package at.fhtw.swen2.tutorial.presentation.viewmodel;
import at.fhtw.swen2.tutorial.model.TourLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourLogListViewModel {


    private List<TourLog> masterData = new ArrayList<>();
    private ObservableList<TourLog> tourLogListItems = FXCollections.observableArrayList();

    public ObservableList<TourLog> getPersonListItems() {
        return tourLogListItems;
    }

    public void addItem(TourLog tourLog) {
        tourLogListItems.add(tourLog);
        masterData.add(tourLog);
    }

    public void clearItems(){ tourLogListItems.clear(); }

    public void initList(){
//        personService.getPersonList().forEach(p -> {
//            addItem(p);
//        });
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
