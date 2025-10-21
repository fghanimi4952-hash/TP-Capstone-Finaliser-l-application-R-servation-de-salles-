package com.example.repository;

import com.example.model.Salle;
import com.example.model.StatutReservation;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalleRepositoryImpl implements SalleRepository {

    private final EntityManager em;

    public SalleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Salle findById(Long id) {
        return em.find(Salle.class, id);
    }

    @Override
    public List<Salle> findAll() {
        return em.createQuery("SELECT s FROM Salle s", Salle.class).getResultList();
    }

    @Override
    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT DISTINCT s FROM Salle s WHERE s.id NOT IN " +
                "(SELECT r.salle.id FROM Reservation r " +
                "WHERE r.statut = :statut " +
                "AND (r.dateDebut <= :end AND r.dateFin >= :start))";

        return em.createQuery(jpql, Salle.class)
                .setParameter("statut", StatutReservation.CONFIRMEE)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public List<Salle> searchRooms(Map<String, Object> criteria) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT s FROM Salle s WHERE 1=1");

        if (criteria.containsKey("capaciteMin")) {
            jpql.append(" AND s.capacite >= :capaciteMin");
        }
        if (criteria.containsKey("capaciteMax")) {
            jpql.append(" AND s.capacite <= :capaciteMax");
        }
        if (criteria.containsKey("batiment")) {
            jpql.append(" AND s.batiment = :batiment");
        }
        if (criteria.containsKey("etage")) {
            jpql.append(" AND s.etage = :etage");
        }
        if (criteria.containsKey("equipement")) {
            jpql.append(" AND EXISTS (SELECT e FROM s.equipements e WHERE e.id = :equipement)");
        }

        TypedQuery<Salle> query = em.createQuery(jpql.toString(), Salle.class);

        if (criteria.containsKey("capaciteMin")) {
            query.setParameter("capaciteMin", criteria.get("capaciteMin"));
        }
        if (criteria.containsKey("capaciteMax")) {
            query.setParameter("capaciteMax", criteria.get("capaciteMax"));
        }
        if (criteria.containsKey("batiment")) {
            query.setParameter("batiment", criteria.get("batiment"));
        }
        if (criteria.containsKey("etage")) {
            query.setParameter("etage", criteria.get("etage"));
        }
        if (criteria.containsKey("equipement")) {
            query.setParameter("equipement", criteria.get("equipement"));
        }

        return query.getResultList();
    }

    @Override
    public long countRooms() {
        return em.createQuery("SELECT COUNT(s) FROM Salle s", Long.class).getSingleResult();
    }

    @Override
    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        return em.createQuery("SELECT s FROM Salle s ORDER BY s.id", Salle.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public void save(Salle salle) {
        em.persist(salle);
    }

    @Override
    public void update(Salle salle) {
        em.merge(salle);
    }

    @Override
    public void delete(Long id) {
        Salle salle = findById(id);
        if (salle != null) {
            em.remove(salle);
        }
    }
}