package at.fhtw.swen2.tutorial.utils;

import at.fhtw.swen2.tutorial.persistence.entities.PersonEntity;
import at.fhtw.swen2.tutorial.model.TourLog;
import org.junit.jupiter.api.Test;

public class BuilderTest {

    @Test
    void testPersonEntityBuilder() {
        PersonEntity maxi = PersonEntity.builder()
                .name("Maxi")
                .email("maxi@email.com")
                .build();
    }
    @Test
    void testPersonBuilder() {
        TourLog maxi = TourLog.builder()
                .name("Maxi")
                .id(11L)
                .build();
    }


}
