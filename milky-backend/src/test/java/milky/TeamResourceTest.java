package milky;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static milky.utils.Utils.getRandomString;
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
    public void restAPIs_should_work_for_CRUD() throws Exception {
        final var teamName = getRandomString();
        final var jsonTeam = getTeamAsJson(new Team(teamName));

        // creat a new Team
        // POST http://localhost:2403/milky/api/v1/team
        final var createdTeam =
                given()
                        .body(jsonTeam)
                        .contentType(ContentType.JSON)
                        .when().post()
                        .then()
                        .statusCode(Response.Status.CREATED.getStatusCode())
                        .body("name", containsString(teamName))
                        .extract().as(Team.class);
        final var teamId = createdTeam.getId().toString();

        // GET http://localhost:2403/milky/api/v1/team
        when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString(teamName));

        // GET http://127.0.0.1:2403/milky/api/v1/team/search?keyword=teamName
        final List<Team> searchResult =
                given()
                        .queryParam("keyword", teamName)
                        .when()
                        .get("search")
                        .then()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .body(containsString(teamName))
                        .body("size()", org.hamcrest.Matchers.greaterThanOrEqualTo(1))
                        .extract().as(List.class);
        assertTrue(searchResult.size() == 1);

        // retrieve the new Team
        // GET http://localhost:2403/milky/api/v1/team/1
        when()
                .get(teamId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString(teamName));

        final var newTeamName = getRandomString();
        createdTeam.setName(newTeamName);
        final var jsonUpdatedTeam = getTeamAsJson(createdTeam);

        // update the new Team
        // PUT http://localhost:2403/milky/api/v1/team/1
        given()
                        .body(jsonUpdatedTeam)
                        .contentType(ContentType.JSON)
                        .when().put(teamId)
                        .then()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .body("name", containsString(newTeamName));

        // delete the new Team
        // DELETE http://localhost:2403/milky/api/v1/team/1
        when()
                .delete(teamId)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        // retrieve the new Team
        // GET http://localhost:2403/milky/api/v1/team/1
        when()
                .get(teamId)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    private String getTeamAsJson(Team team) throws JsonProcessingException {
        return mapper.writeValueAsString(team);
    }
}
