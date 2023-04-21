package at.technikum.api.controller;

import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.TourLog;
import at.technikum.api.service.TourLogsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Validated
public class TourLogController {
    private final TourLogsService tourLogsService;

    @Autowired
    public TourLogController(TourLogsService tourLogsService) {
        this.tourLogsService = tourLogsService;
    }

    @GetMapping("/tours/{tourId}/logs")
    public ResponseEntity<List<TourLog>> getAllLogsByTourId(@PathVariable(value = "tourId") Long tourId,
                                                            @RequestParam(required = false) String searchQuery) {
        return ResponseEntity.ok(tourLogsService.findAllTourLogsByTourId(tourId, searchQuery));
    }

    @GetMapping("/logs/{id}")
    public ResponseEntity<TourLog> getTourLogById(@PathVariable Long id) {
        return tourLogsService.findTourLogById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No Tour with Id: " + id));
    }

    @PostMapping("/tours/{tourId}/logs")
    public ResponseEntity<TourLog> createTourLog(@PathVariable Long tourId, @Valid @RequestBody TourLog tourLog) {
        return ResponseEntity.ok(tourLogsService.createTourLog(tourId, tourLog));
    }

    @PutMapping("/logs/{id}")
    public ResponseEntity<TourLog> updateTourLog(@PathVariable Long id, @Valid @RequestBody TourLog tourLog) {
        return ResponseEntity.ok(tourLogsService.updateTourLog(id, tourLog));
    }

    @DeleteMapping("/logs/{id}")
    public ResponseEntity<Integer> deleteTourLog(@PathVariable Long id) {
        int deletedRows = tourLogsService.deleteTourLogById(id);
        if (deletedRows == 0) throw new ResourceNotFoundException("No TourLog with Id: " + id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tours/{tourId}/logs")
    public ResponseEntity<List<TourLog>> deleteAllTourLogsByTourId(@PathVariable Long tourId) {
        return ResponseEntity.ok(tourLogsService.deleteAllTourLogsFromTourById(tourId));
    }
}
