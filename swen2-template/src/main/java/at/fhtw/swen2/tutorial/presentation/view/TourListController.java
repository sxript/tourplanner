package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class TourListController implements Initializable{

    private final TourListViewModel tourListViewModel;
    @FXML
    public ListView<Tour> listView = new ListView<>();
    @FXML
    private VBox listContainer;

    public TourListController() {
        this.tourListViewModel = new TourListViewModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tourListViewModel.initList();
        ListProperty<Tour> tourListProperty = new SimpleListProperty<>(tourListViewModel.getTourListItems());
        listView.itemsProperty().bindBidirectional(tourListProperty);

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
