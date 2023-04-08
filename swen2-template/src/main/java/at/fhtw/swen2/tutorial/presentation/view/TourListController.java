package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.viewmodel.CUDViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.DetailTourViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class TourListController implements Initializable {

    @Autowired
    private TourListViewModel tourListViewModel;

    @Autowired
    private CUDViewModel cudViewModel;


    @FXML
    public ListView<Tour> listView = new ListView<>();
    @FXML
    private VBox listContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        setupTourListViewCellFactory();


        tourListViewModel.initList();

        ListProperty<Tour> tourListProperty = new SimpleListProperty<>(tourListViewModel.getTourListItems());
        listView.itemsProperty().bindBidirectional(tourListProperty);

        listView.setItems(tourListViewModel.getTourListItems());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            log.info("Selected item changed from {} to {}", oldSelection, newSelection);
            if (newSelection != null) {
                tourListViewModel.getSelectedTour().setValue(newSelection);
                tourListViewModel.selectedTour(newSelection);
            }
            cudViewModel.updateDeleteButtonEnabled();
            cudViewModel.updateUpdateButtonEnabled();
        });

        if (listView.getItems().isEmpty()) {
            listView.setPlaceholder(new Label("No tours available. Please create a new tour."));
        }
        listContainer.getChildren().add(listView);


    }

    // This method is used to only display the name of the tour in the list view
    private void setupTourListViewCellFactory() {
        listView.setCellFactory(param -> new ListCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(Tour tour, boolean empty) {
                super.updateItem(tour, empty);

                if (empty || tour == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(tour.getName());
                    setText(null);
                    setGraphic(label);
                }
            }
        });
    }

}
