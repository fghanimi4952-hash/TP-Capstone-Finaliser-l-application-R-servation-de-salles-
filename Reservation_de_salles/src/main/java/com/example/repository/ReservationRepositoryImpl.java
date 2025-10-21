package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final EntityManager em;

    public ReservationRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
        } else {
            em.merge(reservation);
        }
    }

    @Override
    public List<Reservation> findReservationsByUser(Utilisateur utilisateur) {
        String jpql = "SELECT r FROM Reservation r WHERE r.utilisateur = :user ORDER BY r.dateDebut DESC";
        TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);
        query.setParameter("user", utilisateur);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findReservationsBySalle(Salle salle) {
        String jpql = "SELECT r FROM Reservation r WHERE r.salle = :salle ORDER BY r.dateDebut DESC";
        TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);
        query.setParameter("salle", salle);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findReservationsBetween(LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT r FROM Reservation r WHERE r.dateDebut >= :start AND r.dateFin <= :end";
        TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }
}
