package milky.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import milky.models.Team;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TeamService {

    private static final Logger LOGGER = Logger.getLogger(TeamService.class);

    @Inject
    EntityManager entityManager;

    public List<Team> findAll() {
        return entityManager.createNamedQuery("Team.findAll", Team.class).getResultList();
    }

    public Team findById(Long id) {
        return entityManager.find(Team.class, id);
    }

    public void create(Team offering) {
        entityManager.persist(offering);
    }

    public Team update(Team offering) {
        return entityManager.merge(offering);
    }

    public void delete(Long id) {
        Team entity = entityManager.getReference(Team.class, id);
        entityManager.remove(entity);
    }
}
