package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.stereotype.Component;



@Component
@Getter
public class SearchViewModel {
    private final SimpleStringProperty searchString = new SimpleStringProperty();

    private final TourLogListViewModel tourLogListViewModel;

    public SearchViewModel(TourLogListViewModel tourLogListViewModel) {
        this.tourLogListViewModel = tourLogListViewModel;
    }

    public void search() {
        tourLogListViewModel.filterList(getSearchString().getValue());
    }
}
