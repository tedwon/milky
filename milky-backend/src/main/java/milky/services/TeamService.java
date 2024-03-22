package milky.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import milky.models.Team;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TeamService {

    @Inject
    EntityManager entityManager;

    public List<Team> findAll() {
        return entityManager.createNamedQuery("Team.findAll", Team.class).getResultList();
    }

    public List<Team> findByCustomCriteria(final String keyword) {
        if (keyword == null) {
            return findAll();
        }

        final var namedQuery = new StringBuilder("FROM Team t");
        namedQuery.append(" WHERE ");
        if (keyword != null) {
            namedQuery.append("AND (upper(t.name) LIKE upper(:keyword)) ");
        }
        namedQuery.append("ORDER BY t.name ASC ");

        Query jpaQuery = entityManager.createQuery(namedQuery.toString().replace(" WHERE AND ", " WHERE "));

        if (keyword != null) {
            jpaQuery.setParameter("keyword", "%" + keyword + "%");
        }

        return jpaQuery.getResultList();
    }

    public Team findById(Long id) {
        return entityManager.find(Team.class, id);
    }

    public void create(Team entity) {
        entityManager.persist(entity);
    }

    public Team update(Team entity) {
        return entityManager.merge(entity);
    }

    public void delete(Long id) {
        Team entity = entityManager.getReference(Team.class, id);
        entityManager.remove(entity);
    }
}
