package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.presentation.view.BaseSearchViewModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Getter
public class FullSearchViewModel extends BaseSearchViewModel {
    private final TourListViewModel tourListViewModel;
    public FullSearchViewModel(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
    }

    @Override
    public void filterList(Optional<String> searchString) {
        tourListViewModel.filterList(searchString);
    }
}
