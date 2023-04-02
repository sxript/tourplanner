package at.technikum.api.controller;

import at.technikum.api.model.TourLog;
import at.technikum.api.service.TourLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class TourLogController {
    private final TourLogsService tourLogsService;

    @Autowired
    public TourLogController(TourLogsService tourLogsService) {
        this.tourLogsService = tourLogsService;
    }

    @GetMapping("/tours/{tourId}/logs")
    public ResponseEntity<List<TourLog>> getAllLogsByTourId(@PathVariable(value = "tourId") Long tourId) {
        return ResponseEntity.ok(tourLogsService.findAllTourLogsByTourId(tourId));
    }

    @GetMapping("/logs/{id}")
    public ResponseEntity<TourLog> getTourLogById(@PathVariable Long id) {
        return tourLogsService.findTourLogById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/tours/{tourId}/logs")
    public ResponseEntity<TourLog> createTourLog(@PathVariable Long tourId, @RequestBody TourLog tourLog) {
        return ResponseEntity.ok(tourLogsService.createTourLog(tourId, tourLog));
    }

    @PutMapping("/logs/{id}")
    public ResponseEntity<TourLog> updateTourLog(@PathVariable Long id, @RequestBody TourLog tourLog) {
        return ResponseEntity.ok(tourLogsService.updateTourLog(id, tourLog));
    }

    @DeleteMapping("/logs/{id}")
    public ResponseEntity<TourLog> deleteTourLog(@PathVariable Long id) {
        return tourLogsService.deleteTourLogById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tours/{tourId}/logs")
    public ResponseEntity<List<TourLog>> deleteAllTourLogsByTourId(@PathVariable Long tourId) {
        return ResponseEntity.ok(tourLogsService.deleteAllTourLogsFromTourById(tourId));
    }
}
