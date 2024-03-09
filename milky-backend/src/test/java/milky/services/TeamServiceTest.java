package milky.services;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import milky.models.Team;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class TeamServiceTest {

    private static final Logger LOGGER = Logger.getLogger(TeamServiceTest.class);

    @Inject
    TeamService service;

    @BeforeAll
    static void beforeQuarkusTest() {
    }

    @BeforeEach
    void beforeEachTest() {
    }

    @Test
    @Order(1)
    public void create() {
        Team team = new Team("BigData Platform Dev");
        service.create(team);
        assertFalse(service.findAll().isEmpty());
    }

    @Test
    @Order(2)
    public void update() {
        Team team = service.findById(1L);
        team.setName("AI Platform Dev");
        service.update(team);
        assertFalse(service.findAll().isEmpty());
    }

    @Test
    @Order(3)
    public void delete() {
        service.delete(1L);
        Team team = service.findById(1L);
        assertNull(team);
    }
}
