package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
public class FullSearchViewModel {

    @Autowired
    private TourListViewModel tourListViewModel;

    private SimpleStringProperty searchString = new SimpleStringProperty();

    public FullSearchViewModel() {
        this.tourListViewModel = new TourListViewModel();
    }

    public void search() {
        tourListViewModel.filterList(getSearchString().getValue());
    }


}
