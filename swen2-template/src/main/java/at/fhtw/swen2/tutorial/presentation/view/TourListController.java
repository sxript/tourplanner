package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.viewmodel.CUDViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class TourListController implements Initializable {
    @FXML
    public final ListView<Tour> listView = new ListView<>();
    @FXML
    private VBox listContainer;

    private final TourListViewModel tourListViewModel;
    private final CUDViewModel cudViewModel;

    public TourListController(CUDViewModel cudViewModel, TourListViewModel tourListViewModel) {
        this.cudViewModel = cudViewModel;
        this.tourListViewModel = tourListViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        setupTourListViewCellFactory();

        tourListViewModel.initList();

        ListProperty<Tour> tourListProperty = new SimpleListProperty<>(tourListViewModel.getTourListItems());
        listView.itemsProperty().bindBidirectional(tourListProperty);

        listView.setItems(tourListViewModel.getTourListItems());

        listView.getItems().addListener((ListChangeListener<Tour>) change -> {
            if (listView.getItems().isEmpty()) {
                listView.setPlaceholder(new Label("No tours available. Please create a new tour."));
            } else {
                while (change.next()) {
                    if (change.wasAdded()) {
                        int lastIndex = change.getAddedSubList().size() - 1;
                        listView.getSelectionModel().select(change.getFrom() + lastIndex);
                    }
                }
            }
        });
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
        } else {
            listView.getSelectionModel().selectFirst();
            Tour t = listView.getSelectionModel().getSelectedItem();

            tourListViewModel.getSelectedTour().setValue(t);
            tourListViewModel.selectedTour(t);

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
