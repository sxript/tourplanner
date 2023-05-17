package at.fhtw.swen2.tutorial.presentation.view;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
@Scope("prototype")
@Getter
public abstract class BaseSearchViewModel {
    private final SimpleStringProperty searchString = new SimpleStringProperty();

    public abstract void filterList(Optional<String> searchString);

    public void search() {
        filterList(Optional.ofNullable(getSearchString().getValue()));
    }
}
