package milky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import milky.models.Team;
import milky.services.TeamService;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.List;

@Path("/milky/api/v1/team")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {

    private static final Logger LOGGER = Logger.getLogger(TeamResource.class);

    @Inject
    TeamService service;

    /**
     * e.g. GET http://localhost:2403/milky/api/v1/team
     */
    @GET
    public List<Team> findAll() {
        return service.findAll();
    }

    /**
     * e.g. GET http://localhost:2403/milky/api/v1/team/1
     */
    @GET
    @Path("{id}")
    public Team findById(Long id) {
        return service.findById(id);
    }

    /**
     * POST http://localhost:2403/milky/api/v1/team
     */
    @POST
    @Transactional
    public Response create(Team team) {
        final int code = 201;
        if (team.getId() != null) {
            /**
             * HTTP 422 Unprocessable Content
             * See https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/422
             */
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        service.create(team);
        return Response.ok(team).status(code).build();
    }

    /**
     * e.g. PUT http://localhost:2403/milky/api/v1/team/1
     */
    @PUT
    @Path("{id}")
    @Transactional
    public Team update(Long id, Team team) {
        if (team.getId() == null) {
            throw new WebApplicationException("Team was not set on request.", 422);
        }
        Team entity = service.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Team with id of " + id + " does not exist.", 404);
        }
        service.update(team);
        return entity;
    }

    /**
     * e.g. DELETE http://localhost:2403/milky/api/v1/team/1
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException("Team with id of " + id + " does not exist.", 404);
        }
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {
        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            // Catch duplicate key value violates
            Throwable throwable = exception.getCause();
            while (throwable != null && !(throwable instanceof SQLException)) {
                throwable = throwable.getCause();
            }
            if (throwable instanceof SQLException se) {
                if ("23505".equals(se.getSQLState())) {
                    code = 409;
                }
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }
    }
}
