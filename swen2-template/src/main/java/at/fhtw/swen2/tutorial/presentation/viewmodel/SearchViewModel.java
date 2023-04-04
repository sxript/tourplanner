package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.stereotype.Component;


@Component
public class SearchViewModel {

    private final TourLogListViewModel tourLogListViewModel;

    private SimpleStringProperty searchString = new SimpleStringProperty();

    public SearchViewModel() {
        this.tourLogListViewModel = new TourLogListViewModel();
    }


    public String getSearchString() {
        return searchString.get();
    }

    public SimpleStringProperty searchStringProperty() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString.set(searchString);
    }

    public void search() {
//        tourLogListViewModel.filterList(getSearchString());
    }



}
