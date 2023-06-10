package at.fhtw.swen2.tutorial.reports;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.util.AlertUtils;
import at.fhtw.swen2.tutorial.util.DataExportDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Component
public class ImportReportUtility {
    private final TourService tourService;
    private final TourLogService tourLogService;
    private final TourListViewModel tourListViewModel;

    public ImportReportUtility(TourService tourService, TourLogService tourLogService, TourListViewModel tourListViewModel) {
        this.tourService = tourService;
        this.tourLogService = tourLogService;
        this.tourListViewModel = tourListViewModel;
    }

    public void fileImport(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
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
}
