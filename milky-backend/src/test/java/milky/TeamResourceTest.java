package milky;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import milky.models.Team;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static milky.Utils.getRandomString;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(TeamResource.class)
public class TeamResourceTest {

    private static final Logger LOGGER = Logger.getLogger(TeamResourceTest.class);

    @Inject
    ObjectMapper mapper;

    @Test
    @Order(1)
    public void shouldWorkForCRUD() throws Exception {
        final var teamName = getRandomString();
        final Team newTeam = new Team(teamName);
        final var jsonNewTeam = mapper.writeValueAsString(newTeam);

        // creat a new Team
        // POST http://localhost:2403/milky/api/v1/team
        var newTeamCreated = given()
                .body(jsonNewTeam)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("name", containsString(teamName))
                .extract()
                .as(Team.class);

        // GET http://localhost:2403/milky/api/v1/team
        when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString(teamName));

        // retrieve the new Team
        // GET http://localhost:2403/milky/api/v1/team/1
        final Long id = newTeamCreated.getId();
        when()
                .get(id.toString())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString(teamName));

        // update the new Team
        // PUT http://localhost:2403/milky/api/v1/team/1
        final var newTeamName = getRandomString();
        newTeamCreated.setName(newTeamName);
        final var jsonUpdatedTeam = mapper.writeValueAsString(newTeamCreated);

        var newTeamUpdated = given()
                .body(jsonUpdatedTeam)
                .contentType(ContentType.JSON)
                .when()
                .put(id.toString())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", containsString(newTeamName))
                .extract()
                .as(Team.class);
        LOGGER.info(newTeamUpdated);

        // delete the new Team
        // DELETE http://localhost:2403/milky/api/v1/team/1
        when()
                .delete(id.toString())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        // retrieve the new Team
        // GET http://localhost:2403/milky/api/v1/team/1
        when()
                .get(id.toString())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}
