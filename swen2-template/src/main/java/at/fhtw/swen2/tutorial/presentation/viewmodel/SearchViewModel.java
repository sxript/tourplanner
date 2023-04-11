package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.presentation.view.BaseSearchViewModel;
import lombok.Getter;
import org.springframework.stereotype.Component;



@Component
@Getter
public class SearchViewModel extends BaseSearchViewModel {
    private final TourLogListViewModel tourLogListViewModel;

    public SearchViewModel(TourLogListViewModel tourLogListViewModel) {
        this.tourLogListViewModel = tourLogListViewModel;
    }

    @Override
    public void filterList(String searchString) {
        tourLogListViewModel.filterList(getSearchString().getValue());
    }
}
