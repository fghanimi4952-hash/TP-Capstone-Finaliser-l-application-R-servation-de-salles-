package com.example.service;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;
import com.example.repository.ReservationRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final EntityManager em;
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(EntityManager em, ReservationRepository reservationRepository) {
        this.em = em;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void save(Reservation reservation) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            reservationRepository.save(reservation);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Reservation> findByUser(Utilisateur utilisateur) {
        return reservationRepository.findReservationsByUser(utilisateur);
    }

    @Override
    public List<Reservation> findBySalle(Salle salle) {
        return reservationRepository.findReservationsBySalle(salle);
    }

    @Override
    public List<Reservation> findBetween(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findReservationsBetween(start, end);
    }
}
