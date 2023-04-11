package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.SearchViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
@Slf4j
public class SearchController {
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    public static final int PAGE_ITEMS_COUNT = 10;

    private final SearchViewModel searchViewModel;

    public SearchController(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }


    @FXML
    private void initialize() {

        searchField.textProperty().bindBidirectional(searchViewModel.getSearchString());

        // search panel
        searchButton.setText("Search");
        searchButton.setOnAction(event -> loadData());
        searchButton.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loadData();
            }
        });
    }

    private void loadData() {
        searchViewModel.search();
        System.out.println("in loadData");
    }

}
