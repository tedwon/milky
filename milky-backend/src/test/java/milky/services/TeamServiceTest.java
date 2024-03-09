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

import static milky.Utils.getRandomName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void testCRUD() {
        // creat a new team
        final String newTeamName = getRandomName();
        final Team team = new Team(newTeamName);
        service.create(team);
        assertFalse(service.findAll().isEmpty());

        // retrieve the team
        Long id = team.getId();
        final var retrievedTeamAfterCreation = service.findById(id);
        assertNotNull(retrievedTeamAfterCreation);
        assertEquals(newTeamName, retrievedTeamAfterCreation.getName());

        // update the team
        team.setName(getRandomName());
        service.update(team);
        assertFalse(service.findAll().isEmpty());

        // delete the team
        service.delete(id);
        assertNull(service.findById(id));
    }
}
