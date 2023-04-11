package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.presentation.viewmodel.DetailTourViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class TourLogListController implements Initializable{

    @FXML
    private final TableView<TourLog> tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    private final TourLogListViewModel tourLogListViewModel;

    private final DetailTourViewModel detailTourViewModel;

    public TourLogListController(TourLogListViewModel tourLogListViewModel, DetailTourViewModel detailTourViewModel) {
        this.tourLogListViewModel = tourLogListViewModel;
        this.detailTourViewModel = detailTourViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(tourLogListViewModel.getTourLogListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TourLog, Long> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<TourLog, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TourLog, String> comment = new TableColumn<>("Comment");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        TableColumn<TourLog, Integer> duration = new TableColumn<>("Duration");
        duration.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        TableColumn<TourLog, String> difficulty = new TableColumn<>("Difficulty");
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        TableColumn<TourLog, Integer> rating = new TableColumn<>("Rating");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tableView.getColumns().addAll(id, date, comment, duration, difficulty, rating);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            log.info("Selected item changed from {} to {}", oldSelection, newSelection);
            if (newSelection != null) {
                tourLogListViewModel.getSelectedTourLog().setValue(newSelection);
            }
            detailTourViewModel.updateDeleteButtonEnabled();
            detailTourViewModel.updateUpdateButtonEnabled();
        });


        dataContainer.getChildren().add(tableView);
        tourLogListViewModel.initList();
    }

}
