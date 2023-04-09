package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@Getter
public class SearchViewModel {

    @Autowired
    private final TourLogListViewModel tourLogListViewModel;

    private SimpleStringProperty searchString = new SimpleStringProperty();

    public SearchViewModel() {
        this.tourLogListViewModel = new TourLogListViewModel();
    }

    public void search() {
        tourLogListViewModel.filterList(getSearchString().getValue());
    }



}
