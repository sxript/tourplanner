package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.presentation.StageAware;
import at.fhtw.swen2.tutorial.presentation.event.ApplicationShutdownEvent;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.reports.ReportManager;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.util.AlertUtils;
import at.fhtw.swen2.tutorial.util.DataExportDTO;
import at.fhtw.swen2.tutorial.util.DataIOUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class ApplicationController implements Initializable, StageAware {

    private final ReportManager reportManager;
    ApplicationEventPublisher publisher;

    @FXML
    BorderPane layout;

    // Menu, at some point break out
    @FXML
    MenuItem miPreferences;
    @FXML
    MenuItem miQuit;
    @FXML
    MenuItem miAbout;
    @FXML
    MenuItem miExport;
    @FXML
    MenuItem miImport;
    @FXML
    MenuItem miTourReport;
    @FXML
    MenuItem miSummarizeReport;

    // Toolbar, at some point break out
    @FXML
    Label tbMonitorStatus;
    Circle monitorStatusIcon = new Circle(8);

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();
    private final TourListViewModel tourListViewModel;

    private final TourService tourService;
    private final TourLogService tourLogService;

    private final DataIOUtil dataIOUtil;

    public ApplicationController(ReportManager reportManager, ApplicationEventPublisher publisher, TourService tourService, TourLogService tourLogService, TourListViewModel tourListViewModel, DataIOUtil dataIOUtil) {
        log.debug("Initializing application controller");
        this.publisher = publisher;
        this.tourService = tourService;
        this.tourLogService = tourLogService;
        this.tourListViewModel = tourListViewModel;
        this.reportManager = reportManager;
        this.dataIOUtil = dataIOUtil;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));
        tbMonitorStatus.setGraphic(monitorStatusIcon);
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        publisher.publishEvent(new ApplicationShutdownEvent(event.getSource()));
    }

    @FXML
    public void onHelpAbout(ActionEvent event) {
        new AboutDialogController().show();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }


    // TODO: is this the right place for this?
    public void onFileImport() {
        // TODO: loading indicator
        ObjectMapper objectMapper = new ObjectMapper();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Show save file dialog
        File file = fileChooser.showOpenDialog(stage.getValue());

        if (file != null) {
            // User selected a file, do something with it
            try {
                String jsonData = Files.readString(file.toPath());
                List<DataExportDTO> data = objectMapper.readValue(jsonData, new TypeReference<>() {
                });
                // Loop through each exported data item and create a Callable for it
                for (DataExportDTO dataItem : data) {
                    tourService.saveTour(dataItem.getTour())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe(importedTour -> {
                                Platform.runLater(() -> tourListViewModel.addItem(importedTour));

                                // Loop through each tour log in the exported data item and import it
                                log.info("Importing {} tour logs for tour {}", dataItem.getTourLogs().size(), importedTour.getName());
                                for (TourLog tourLog : dataItem.getTourLogs()) {
                                    tourLogService.saveTourLog(importedTour.getId(), tourLog)
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .subscribe(savedTourLog -> log.info("Imported tour log {}", savedTourLog.getId()));
                                }
                            }, error -> AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error importing tour", error.getMessage()));
                }
            } catch (IOException e) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error reading file", e.getMessage());
            }
        }
    }

    // TODO: is this the right place for this?
    public void onFileExport() {
        String jsonData = dataIOUtil.exportData();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(stage.getValue());
        if (selectedFile != null) {
            String filename = selectedFile.getName();
            if (!filename.endsWith(DataIOUtil.FILE_EXTENSION)) {
                filename += DataIOUtil.FILE_EXTENSION;
            }
            try (FileWriter fileWriter = new FileWriter(selectedFile)) {
                fileWriter.write(jsonData);
                fileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onTourReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.setInitialFileName("tour-report.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Show the file chooser dialog and get the selected file
        File file = fileChooser.showSaveDialog(stage.getValue());
        Tour selectedTour = tourListViewModel.getSelectedTour().get();

        if (file == null || selectedTour == null) {
            log.info("No file or tour selected");
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report", "Please select a tour and a file to save the report to.");
            return;
        }

        reportManager.generateTourReport(file, selectedTour, tourLogService.findAllTourLogsByTourId(selectedTour.getId()).block());
        AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Report Generated", "Report Generated", "The report was generated successfully.");
    }

    public void onSummarizeReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.setInitialFileName("summarize-report.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Show the file chooser dialog and get the selected file
        File file = fileChooser.showSaveDialog(stage.getValue());
        List<Tour> allTours = tourListViewModel.getTourListItems().stream().toList();

        if (file == null) {
            log.info("No file selected");
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report", "Please select a file to save the report to.");
            return;
        }

        reportManager.generateSummarizeReport(file, allTours);
        AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Report Generated", "Report Generated", "The report was generated successfully.");
    }
}
