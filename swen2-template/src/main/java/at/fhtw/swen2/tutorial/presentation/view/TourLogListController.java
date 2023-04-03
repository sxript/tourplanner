package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class TourLogListController implements Initializable{

    @Autowired
    public TourLogListViewModel tourLogListViewModel;

    @FXML
    public TableView tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(tourLogListViewModel.getTourLogListItems());


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn date = new TableColumn("Date");
        date.setCellValueFactory(new PropertyValueFactory("date"));

        TableColumn duration = new TableColumn("Duration");
        duration.setCellValueFactory(new PropertyValueFactory("duration"));

        TableColumn distance = new TableColumn("Distance");
        distance.setCellValueFactory(new PropertyValueFactory("distance"));

        tableView.getColumns().addAll(date, duration, distance);

        dataContainer.getChildren().add(tableView);
        tourLogListViewModel.initList();
    }

}
