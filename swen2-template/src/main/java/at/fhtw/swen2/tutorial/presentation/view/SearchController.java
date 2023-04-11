package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.SearchViewModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
@Slf4j
public class SearchController extends BaseSearchController {
    public SearchController(SearchViewModel searchViewModel) {
        super(searchViewModel);
    }
}
