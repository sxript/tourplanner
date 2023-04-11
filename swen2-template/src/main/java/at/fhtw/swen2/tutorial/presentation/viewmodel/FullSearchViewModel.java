package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
public class FullSearchViewModel {

    private final TourListViewModel tourListViewModel;

    private final SimpleStringProperty searchString = new SimpleStringProperty();

    public FullSearchViewModel(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
    }

    public void search() {
        tourListViewModel.filterList(getSearchString().getValue());
    }


}
