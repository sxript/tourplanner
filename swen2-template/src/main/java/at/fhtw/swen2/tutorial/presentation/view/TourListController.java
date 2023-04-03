package at.fhtw.swen2.tutorial.presentation.view;

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
    public ListView<String> listView = new ListView<>();
    @FXML
    private VBox listContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){



        ObservableList<String> list = FXCollections.observableArrayList();

        tourListViewModel.getTourListItems().forEach(s->list.add(s.getName()));
        listView.setItems(list);

//        tableView.setItems(tourLogListViewModel.getTourLogListItems());

        tourListViewModel.getTourListItems();

        if(listView.getItems().size()==0){
            listView.setPlaceholder(new Label("Kein Inhalt in der Liste"));
        }
        listContainer.getChildren().add(listView);
        tourListViewModel.initList();
    }

}
