package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@Getter
public class SearchViewModel {

    private final TourLogListViewModel tourLogListViewModel;

    private final SimpleStringProperty searchString = new SimpleStringProperty();

    public SearchViewModel(TourLogListViewModel tourLogListViewModel) {
        this.tourLogListViewModel = tourLogListViewModel;
    }

    public void search() {
        tourLogListViewModel.filterList(getSearchString().getValue());
    }



}
