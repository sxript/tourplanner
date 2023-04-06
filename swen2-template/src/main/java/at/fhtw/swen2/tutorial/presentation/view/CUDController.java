package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.CUDViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CUDController {

    @Autowired
    private CUDViewModel cudViewModel;

    public void onCreateHandle(ActionEvent event) {
        try {
            cudViewModel.openNewStage(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
