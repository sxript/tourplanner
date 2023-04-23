package at.fhtw.swen2.tutorial.util;

import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class DataIOUtil {
    public static final String FILE_EXTENSION = ".json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TourService tourService;
    private final TourLogService tourLogService;

    public DataIOUtil(TourServiceImpl tourService, TourLogServiceImpl tourLogService) {
        this.tourService = tourService;
        this.tourLogService = tourLogService;
    }

    public String exportData() {
        Mono<List<DataExportDTO>> dataMono = tourService.findToursBySearchQuery(Optional.empty())
                .flatMap(tour -> tourLogService.findAllTourLogsByTourId(tour.getId(),Optional.empty())
                        .map(tourLogs -> DataExportDTO.builder()
                                .tour(tour)
                                .tourLogs(tourLogs)
                                .build()))
                .collectList();

        List<DataExportDTO> data = dataMono.block();

        String jsonData = null;
        try {
            jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonData;
    }
}
