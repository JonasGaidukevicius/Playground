package lt.jonas.playground.configuration;

import lt.jonas.playground.model.entity.*;
import lt.jonas.playground.repository.AttractionRepository;
import lt.jonas.playground.repository.KidRepository;
import lt.jonas.playground.repository.PlaygroundAttractionRepository;
import lt.jonas.playground.repository.PlaygroundRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Configuration
public class FillingInitialData {
    @Value("${add.tickets:false}")
    private boolean addTickets;
    @Bean
    @ConditionalOnProperty(value="insert.initial.data", havingValue = "true")
    CommandLineRunner commandLineRunner(AttractionRepository attractionRepository, PlaygroundAttractionRepository playgroundAttractionRepository, KidRepository kidRepository,
                                        PlaygroundRepository playgroundRepository) {
        // Inserting some initial data
        return args -> {
            // Inserting attractions
            Attraction attraction1 = new Attraction(AttractionType.CAROUSEL, "Carousel 3", 3);
            Attraction attraction2 = new Attraction(AttractionType.DOUBLE_SWINGS, "Double swings 2", 2);
            Attraction attraction3 = new Attraction(AttractionType.BALL_PIT, "Ball pit 2", 2);
            Attraction attraction4 = new Attraction(AttractionType.SLIDE, "Slide 4", 4);
            attractionRepository.saveAll(List.of(attraction1, attraction2, attraction3, attraction4));

            // Inserting kids
            Kid kid1 = new Kid("Peter no ticket", 6,"8d05dc1d-0f85-4674-a9ae-b5439b43dc48");
            Kid kid2 = new Kid("Sam", 5, "1e860972-f186-4bc6-8523-7c7d3cb7f93d");
            Kid kid3 = new Kid("Lisa", 7, "d7c9600f-6dfb-4cb2-9903-f90274613177");
            Kid kid4 = new Kid("Barbie", 5, "5e386087-fd93-4e00-8f86-88434cd36eac");
            Kid kid5 = new Kid("Joe", 4, "7d27b74a-19d3-4a59-b683-e930d9cadf80");
            Kid kid6 = new Kid("Jenifer", 5, "66dc7556-f0e4-4e45-a8f7-ddbb0db7ab44");
            Kid kid7 = new Kid("Ralf", 4, "99a82517-a34c-4542-a2d0-d6dd21ff9011");
            Kid kid8 = new Kid("Monika", 8, "bf218ddb-990d-4ab6-bd14-00a4ec1d0735");
            Kid kid9 = new Kid("Dylan", 7, "d4b499c2-5370-468d-9c75-993bc1ca9375");
            Kid kid10 = new Kid("Emma", 5, "d8ca9b5a-266c-4754-8224-886752d47496");
            Kid kid11 = new Kid("Jerry", 4, "7076278a-d4f0-417a-b02a-e0e1fc027547");

            // Adding tickets for kids
            if (addTickets) {
                kid2.setTicketNumber("aa567370-6611-42bc-ac7d-570777dc7673");
                kid3.setTicketNumber("a55aa335-d71c-4b03-8379-80640050ff95");
                kid4.setTicketNumber("d081320b-7564-46fa-8339-d71891128c76");
                kid5.setTicketNumber("eeb0e29b-58d6-4e3f-bbcb-6d43311c48ea");
                kid6.setTicketNumber("c22b316c-bee8-4df1-be32-d42d42173bf0");
                kid7.setTicketNumber("56c8829e-288a-40df-8de2-a7e0db620e00");
                kid8.setTicketNumber("a5fc07c9-7633-496b-ba3f-baa8da9478cf");
                kid9.setTicketNumber("c64f91b5-d235-4696-ad10-4b46138a2c90");
                kid10.setTicketNumber("009cc5c0-49e7-45b5-9ab6-6987393d6d62");
                kid11.setTicketNumber("cfaa22c1-a3a6-47e9-a000-7417f19eee0e");
            }

            kidRepository.saveAll(List.of(kid1, kid2, kid3, kid4, kid5, kid6, kid7, kid8, kid9, kid10, kid11));

            // Inserting playground attractions
            PlaygroundAttraction playgroundAttraction1 = PlaygroundAttraction.builder()
                    .attraction(attraction1)
                    .playgroundAttractionName("Carousel for 3 kids")
                    .occupation(0)
                    .build();
            PlaygroundAttraction playgroundAttraction2 = PlaygroundAttraction.builder()
                    .attraction(attraction2)
                    .playgroundAttractionName("Double swings for 2 kids")
                    .occupation(0)
                    .build();
            PlaygroundAttraction playgroundAttraction3 = PlaygroundAttraction.builder()
                    .attraction(attraction3)
                    .playgroundAttractionName("Ball pit for 2 kids")
                    .occupation(0)
                    .build();
            playgroundAttractionRepository.saveAll(List.of(playgroundAttraction1, playgroundAttraction2, playgroundAttraction3));

            // Inserting playground
            Playground playground = Playground.builder()
                    .name("Happy")
                    .playgroundAttractions(List.of(playgroundAttraction1, playgroundAttraction2, playgroundAttraction3))
                    .kidsInPlayground(new ArrayList<>())
                    .playgroundQueue(new TreeMap<>())
                    .visitorsCount(0L)
                    .queueCount(1L)
                    .build();

            playground.setCapacity(playground.getPlaygroundAttractions().stream().reduce(0, (sum, playgroundAttraction) -> sum + playgroundAttraction.getAttraction().getCapacity(), Integer::sum));
            playgroundRepository.save(playground);

            playground.getPlaygroundAttractions().forEach(playgroundAttraction -> playgroundAttraction.setPlayground(playground));
            playgroundAttractionRepository.saveAll(playground.getPlaygroundAttractions());
        };
    }
}
