package at.technikum.api.controller;

import at.technikum.api.exception.BadRequestException;
import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.map.MapQuestLookupService;
import at.technikum.api.model.Tour;
import at.technikum.api.service.TourService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Base64;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/")
public class TourController {
    private final TourService tourService;
    private final MapQuestLookupService mapQuestLookupService;

    public TourController(TourService tourService, MapQuestLookupService mapQuestLookupService) {
        this.tourService = tourService;
        this.mapQuestLookupService = mapQuestLookupService;
    }

    @GetMapping("/tours")
    public ResponseEntity<List<Tour>> getToursBySearchQuery(@RequestParam(required = false) String searchQuery) {
        return ResponseEntity.ok(tourService.getAllTours(searchQuery));
    }

    @GetMapping("/tours/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
        return tourService.getTourById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No Tour with Id: " + id));
    }

    @PostMapping(value = "/tours", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Tour>> createTour(@Valid @RequestBody Tour newTour) {
        return mapQuestLookupService.getRouteDirections(newTour.getFrom(), newTour.getTo(), newTour.getTransportType())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(mapResult -> {
                    log.info("STATUS CODE: {}", mapResult.getInfo().getStatusCode());
                    if (mapResult.getInfo().getStatusCode() != 0) {
                        return Mono.error(new BadRequestException("StatusCode:" + mapResult.getInfo().getStatusCode() + " Messages" + mapResult.getInfo().getMessages()));
                    }
                    return mapQuestLookupService.getStaticMap(newTour.getTo(), mapResult)
                            .publishOn(Schedulers.boundedElastic())
                            .map(image -> {
                                newTour.setMapImage(Base64.getEncoder().encodeToString(image));
                                newTour.setEstimatedTime(mapResult.getRealTime());
                                newTour.setDistance(mapResult.getDistance());
                                tourService.createTour(newTour);
                                return ResponseEntity.status(HttpStatus.CREATED).body(newTour);
                            });
                });
    }

    @PutMapping(value = "/tours/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Tour>> updateTour(@Valid @RequestBody Tour newTour, @PathVariable Long id) {
        return mapQuestLookupService
                .getRouteDirections(newTour.getFrom(), newTour.getTo(), newTour.getTransportType())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(mapResult -> {
                    if (mapResult.getInfo().getStatusCode() != 0) {
                        return Mono.error(new BadRequestException("StatusCode:" + mapResult.getInfo().getStatusCode() + " Messages" + mapResult.getInfo().getMessages()));
                    }

                    return mapQuestLookupService.getStaticMap(newTour.getTo(), mapResult)
                            .publishOn(Schedulers.boundedElastic())
                            .map(image -> {
                                newTour.setMapImage(Base64.getEncoder().encodeToString(image));
                                newTour.setEstimatedTime(mapResult.getRealTime());
                                newTour.setDistance(mapResult.getDistance());
                                return ResponseEntity.ok(tourService.updateTour(newTour, id));
                            });
                });
    }

    @DeleteMapping("/tours/{id}")
    public ResponseEntity<Tour> deleteTour(@PathVariable Long id) {
        int deletedRows = tourService.deleteTour(id);
        if (deletedRows == 0) throw new ResourceNotFoundException("No Tour with Id: " + id);
        return ResponseEntity.noContent().build();
    }
}
