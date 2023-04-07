package at.fhtw.swen2.tutorial.reports;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReportManager {

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
                    table.addCell(tourLog.getDate().toString());
                    table.addCell(tourLog.getDifficulty());
                    table.addCell(tourLog.getTotalTime().toString());
                    table.addCell(tourLog.getComment());
                    table.addCell(tourLog.getRating().toString());
                }

                document.add(table);
            }
            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMapImagePath(Long tourId) {
        Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images", tourId + ".jpg");
        return imageDir.toAbsolutePath().toString();
    }
}
