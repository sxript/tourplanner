package at.fhtw.swen2.tutorial.util;

import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DataIOUtil {
    public static final String FILE_EXTENSION = ".json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TourService tourService = new TourServiceImpl();
    private final TourLogService tourLogService = new TourLogServiceImpl();

    public String exportData() {
        List<DataExportDTO> data = tourService.findAllTours().stream()
                .map(tour -> DataExportDTO.builder()
                        .tour(tour)
                        .tourLogs(tourLogService.findAllTourLogsByTourId(tour.getId()))
                        .build())
                .toList();

        String jsonData = null;
        try {
            jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonData;
    }
}
