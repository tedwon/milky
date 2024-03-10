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

import static milky.Utils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        // creat a new Team
        final var teamName = getRandomString();
        final Team newTeam = new Team(teamName);
        service.create(newTeam);
        assertFalse(service.findAll().isEmpty());

        // retrieve the new Team
        final Long id = newTeam.getId();
        final var retrievedNewTeam = service.findById(id);
        assertNotNull(retrievedNewTeam);
        assertEquals(teamName, retrievedNewTeam.getName());

        // update the new Team
        final var newTeamName = getRandomString();
        newTeam.setName(newTeamName);
        var updatedNewTeam = service.update(newTeam);
        assertEquals(newTeamName, updatedNewTeam.getName());

        // delete the new Team
        service.delete(id);
        assertNull(service.findById(id));
        assertTrue(service.findAll().isEmpty());
    }
}
