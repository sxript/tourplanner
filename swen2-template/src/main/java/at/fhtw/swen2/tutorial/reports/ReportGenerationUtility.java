package at.fhtw.swen2.tutorial.reports;

import at.fhtw.swen2.tutorial.exception.DataIOException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.text.TextAlignment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ReportGenerationUtility {
    private final TourLogService tourLogService;

    public ReportGenerationUtility(TourLogServiceImpl tourLogService) {
        this.tourLogService = tourLogService;
    }


    // a tour-report which contains all information of a single tour and all its associated tour logs
    public void generateTourReport(File file, Tour tour, List<TourLog> tourLogs) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.addTitle("Tour Report");

            Paragraph title = new Paragraph("Tour Report: " + tour.getId());
            title.setAlignment(TextAlignment.CENTER.ordinal());
            title.setFont(FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD));
            document.add(title);

            // Add Tour Details
            Paragraph tourName = new Paragraph("Name: " + tour.getName());
            tourName.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourName);

            Paragraph tourFrom = new Paragraph("From: " + tour.getFrom());
            tourFrom.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourFrom);

            Paragraph tourTo = new Paragraph("To: " + tour.getTo());
            tourTo.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourTo);

            Paragraph tourTransportType = new Paragraph("Transport Type: " + tour.getTransportType());
            tourTransportType.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourTransportType);

            Paragraph tourDistance = new Paragraph("Distance: " + tour.getDistance());
            tourDistance.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourDistance);

            Paragraph tourDuration = new Paragraph("Duration: " + tour.getEstimatedTime());
            tourDuration.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourDuration);

            Paragraph tourDescription = new Paragraph("Description:\n" + tour.getDescription());
            tourDescription.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(tourDescription);

            // Map Image
            Paragraph mapImage = new Paragraph("Map Image:\n");
            mapImage.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD));
            document.add(mapImage);

            String mapImagePath = getMapImagePath(tour.getId());
            File mapImageFile = new File(mapImagePath);

            if (mapImageFile.exists()) {
                Image image = Image.getInstance(mapImagePath);
                image.scaleToFit(500, 500);
                document.add(image);
            } else {
                Paragraph noMapImage = new Paragraph("No Map Image available");
                noMapImage.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
                document.add(noMapImage);
            }

            // Add Tour Logs
            Paragraph tourLogsTitle = new Paragraph("Tour Logs:");
            tourLogsTitle.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD));
            document.add(tourLogsTitle);

            if (tourLogs.isEmpty()) {
                Paragraph noTourLogs = new Paragraph("No Tour Logs available");
                noTourLogs.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
                document.add(noTourLogs);
            } else {
                PdfPTable table = new PdfPTable(5);

                PdfPCell cell = new PdfPCell(new Phrase("Date"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Difficulty"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Total Time"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Comment"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rating"));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                for (TourLog tourLog : tourLogs) {
                    table.addCell(tourLog.getDate());
                    table.addCell(tourLog.getDifficulty());
                    table.addCell(tourLog.getTotalTime().toString());
                    table.addCell(tourLog.getComment());
                    table.addCell(tourLog.getRating().toString());
                }

                document.add(table);
            }
            document.close();
        } catch (DocumentException | IOException e) {
            throw new DataIOException("Error generating tour report", e);
        }
    }

    // a summarize-report for statistical analysis, which for each tour provides the average time, -distance and rating over all associated tour-logs
    public void generateSummarizeReport(File file, List<Tour> tours) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.addTitle("Summarize Report");

            Paragraph title = new Paragraph("Summarize Report: " + LocalDateTime.now());
            title.setAlignment(TextAlignment.CENTER.ordinal());
            title.setFont(FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD));
            document.add(title);

            Paragraph statistics = new Paragraph("Statistics:");
            statistics.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD));
            document.add(statistics);

            int totalDistance = 0;
            int totalDuration = 0;
            for (Tour tour : tours) {
                totalDistance += tour.getDistance();
                totalDuration += tour.getEstimatedTime();
            }

            Paragraph totalDistanceParagraph = new Paragraph("Total Distance over all Tours: " + totalDistance);
            totalDistanceParagraph.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(totalDistanceParagraph);

            Paragraph totalDurationParagraph = new Paragraph("Total Duration over all Tours: " + totalDuration);
            totalDurationParagraph.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(totalDurationParagraph);

            Paragraph averageDistanceParagraph = new Paragraph("Average Distance over all Tours: " + totalDistance / tours.size());
            averageDistanceParagraph.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(averageDistanceParagraph);

            Paragraph averageDurationParagraph = new Paragraph("Average Duration over all Tours: " + totalDuration / tours.size());
            averageDurationParagraph.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL));
            document.add(averageDurationParagraph);

            Paragraph tourStatistics = new Paragraph("Specific Tour Statistics:");
            tourStatistics.setFont(FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD));
            document.add(tourStatistics);

            PdfPTable table = new PdfPTable(4);

            PdfPCell cell = new PdfPCell(new Phrase("Tour ID"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Average Time"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Average Rating"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            for (Tour tour : tours) {
                table.addCell(tour.getId().toString());
                table.addCell(tour.getName());

                AtomicReference<Double> totalTime = new AtomicReference<>((double) 0);
                AtomicReference<Double> totalRating = new AtomicReference<>((double) 0);
                AtomicInteger count = new AtomicInteger();

                CountDownLatch latch = new CountDownLatch(1);

                tourLogService.findAllTourLogsByTourId(tour.getId(), null)
                        .flatMapMany(Flux::fromIterable)
                        .doOnComplete(() -> {
                            latch.countDown();
                        })
                        .subscribe(tourLog -> {
                            totalTime.updateAndGet(v -> (v + tourLog.getTotalTime()));
                            totalRating.updateAndGet(v -> (v + tourLog.getRating()));
                            count.getAndIncrement();
                            System.out.println(tourLog);

                        });


                try {
                    latch.await(); // Warte auf das Signal, dass die asynchrone Verarbeitung abgeschlossen ist
                } catch (InterruptedException e) {
                }

                if (count.get() == 0) {
                    table.addCell("No Tour Logs available");
                    table.addCell("No Tour Logs available");
                } else {

                    table.addCell(String.valueOf(totalTime.get() / count.get()));
                    table.addCell(String.valueOf(totalRating.get() / count.get()));
                }
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            throw new DataIOException("Error generating tour report", e);
        }
    }

    private String getMapImagePath(Long tourId) {
        Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images", tourId + ".jpg");
        return imageDir.toAbsolutePath().toString();
    }
}
