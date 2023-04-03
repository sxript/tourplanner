package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class TourListController implements Initializable{

    @Autowired
    public TourListViewModel tourListViewModel;
    @FXML
    public ListView<Tour> listView = new ListView<>();
    @FXML
    private VBox listContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){

//        listView.setItems(tourListViewModel.getTourListItems().stream().map(Tour::getName).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        listView.setItems(tourListViewModel.getTourListItems());

        tourListViewModel.getTourListItems();

        if(listView.getItems().size()==0){
            listView.setPlaceholder(new Label("Kein Inhalt in der Liste"));
        }

        System.out.println("hallo ---------- " );
        listContainer.getChildren().add(listView);
//        tourListViewModel.initList();
    }

}
