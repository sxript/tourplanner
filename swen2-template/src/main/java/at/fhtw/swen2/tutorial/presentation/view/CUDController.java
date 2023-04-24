package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.CUDViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class CUDController implements Initializable {

    @FXML
    public Button deleteButton;
    @FXML
    public Button updateButton;

    private final CUDViewModel cudViewModel;

    private final TourListViewModel tourListViewModel;

    public CUDController(CUDViewModel cudViewModel, TourListViewModel tourListViewModel) {
        this.cudViewModel = cudViewModel;
        this.tourListViewModel = tourListViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.disableProperty().bind(cudViewModel.getIsDeleteButtonEnabled().not());
        updateButton.disableProperty().bind(cudViewModel.getIsUpdateButtonEnabled().not());
    }

    public void onCreateHandle(ActionEvent event) {
        try {
            cudViewModel.openNewStage(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onDeleteHandle() {
        tourListViewModel.deleteSelectedTour();
    }

    public void onUpdateHandle(ActionEvent actionEvent) {
        try {
            cudViewModel.openUpdateStage(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
