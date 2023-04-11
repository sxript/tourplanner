package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.FullSearchViewModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class FullSearchController extends BaseSearchController {
    public FullSearchController(FullSearchViewModel searchViewModel) {
        super(searchViewModel);
    }
}
