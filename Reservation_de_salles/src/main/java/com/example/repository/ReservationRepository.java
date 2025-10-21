package com.example.repository;

import com.example.model.Salle;

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
    public List<Salle> findAvailableRooms(LocalDateTime dateDebut, LocalDateTime dateFin) {
        String jpql = "SELECT DISTINCT s FROM Salle s " +
                "WHERE s.id NOT IN (" +
                "SELECT r.salle.id FROM Reservation r " +
                "WHERE (r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut)" +
                ")";
        TypedQuery<Salle> query = em.createQuery(jpql, Salle.class);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    @Override
    public List<Salle> searchRooms(Map<String, Object> criteres) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT s FROM Salle s LEFT JOIN s.equipements e WHERE 1=1 ");

        if (criteres.containsKey("capaciteMin")) {
            jpql.append("AND s.capacite >= :capaciteMin ");
        }
        if (criteres.containsKey("capaciteMax")) {
            jpql.append("AND s.capacite <= :capaciteMax ");
        }
        if (criteres.containsKey("batiment")) {
            jpql.append("AND s.batiment = :batiment ");
        }
        if (criteres.containsKey("etage")) {
            jpql.append("AND s.etage = :etage ");
        }
        if (criteres.containsKey("equipement")) {
            jpql.append("AND e.id = :equipement ");
        }

        TypedQuery<Salle> query = em.createQuery(jpql.toString(), Salle.class);

        criteres.forEach((key, value) -> {
            if (value != null) query.setParameter(key, value);
        });

        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    @Override
    public long countRooms() {
        return em.createQuery("SELECT COUNT(s) FROM Salle s", Long.class).getSingleResult();
    }

    @Override
    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        TypedQuery<Salle> query = em.createQuery("SELECT s FROM Salle s ORDER BY s.id", Salle.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }
}
