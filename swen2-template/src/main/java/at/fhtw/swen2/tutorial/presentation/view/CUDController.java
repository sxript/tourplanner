package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.CUDViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Button updateButton;

    @Autowired
    private CUDViewModel cudViewModel;

    @Autowired
    private TourListViewModel tourListViewModel;

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


    public void onDeleteHandle(ActionEvent actionEvent) {
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
