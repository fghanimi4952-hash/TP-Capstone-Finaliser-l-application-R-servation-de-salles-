package com.example.repository;

import com.example.model.Reservation;
import com.example.model.StatutReservation;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager em;

    public ReservationRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

    @Override
    public List<Reservation> findAll() {
        return em.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
    }

    @Override
    public List<Reservation> findByUtilisateur(Long utilisateurId) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.utilisateur.id = :utilisateurId", Reservation.class)
                .setParameter("utilisateurId", utilisateurId)
                .getResultList();
    }

    @Override
    public List<Reservation> findBySalle(Long salleId) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.salle.id = :salleId", Reservation.class)
                .setParameter("salleId", salleId)
                .getResultList();
    }

    @Override
    public List<Reservation> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.dateDebut BETWEEN :start AND :end OR r.dateFin BETWEEN :start AND :end",
                        Reservation.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public List<Reservation> findByStatut(StatutReservation statut) {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.statut = :statut", Reservation.class)
                .setParameter("statut", statut)
                .getResultList();
    }

    @Override
    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    @Override
    public void update(Reservation reservation) {
        em.merge(reservation);
    }

    @Override
    public void delete(Long id) {
        Reservation reservation = findById(id);
        if (reservation != null) {
            em.remove(reservation);
        }
    }

    @Override
    public boolean isSalleAvailable(Long salleId, LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT COUNT(r) FROM Reservation r " +
                "WHERE r.salle.id = :salleId " +
                "AND r.statut = :statut " +
                "AND (r.dateDebut <= :end AND r.dateFin >= :start)";

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("salleId", salleId)
                .setParameter("statut", StatutReservation.CONFIRMEE)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return count == 0;
    }
}