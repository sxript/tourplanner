package at.technikum.api.controller;

import at.technikum.api.map.MapQuestLookupService;
import at.technikum.api.map.MapResult;
import at.technikum.api.model.Tour;
import at.technikum.api.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class TourController {
    private static final Logger logger = LoggerFactory.getLogger(TourController.class);
    private final TourService tourService;
    private final MapQuestLookupService mapQuestLookupService;

    @Autowired
    public TourController(TourService tourService, MapQuestLookupService mapQuestLookupService) {
        this.tourService = tourService;
        this.mapQuestLookupService = mapQuestLookupService;
    }

    // TODO: add better response messages
    // TODO: add better error messages + handling
    @GetMapping("/tours")
    public ResponseEntity<List<Tour>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @GetMapping("/tours/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
        return tourService.getTourById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/tours")
    public ResponseEntity<Tour> createTour(@RequestBody Tour newTour) {
        MapResult mapResult = mapQuestLookupService.getRouteDirections(newTour.getFrom(), newTour.getTo(), newTour.getTransportType()).join();
        logger.info("STATUS CODE: {}", mapResult.getInfo().getStatusCode());
        if (mapResult.getInfo().getStatusCode() != 0) {
            // TODO: RETURN ERROR MESSAGE FROM getInfo().getMessages()
            return ResponseEntity.badRequest().build();
        }

        byte[] image = mapQuestLookupService.getStaticMap(newTour.getTo(), mapResult).join();
        newTour.setMapImage(image);

        newTour.setEstimatedTime(mapResult.getRealTime());
        newTour.setDistance(mapResult.getDistance());

        return ResponseEntity.ok().body(tourService.createTour(newTour));
    }

    @PutMapping("/tours/{id}")
    public ResponseEntity<Tour> updateTour(@RequestBody Tour newTour, @PathVariable Long id) {
        return tourService.updateTour(newTour, id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tours/{id}")
    public ResponseEntity<Tour> deleteTour(@PathVariable Long id) {
        return tourService.deleteTour(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
