package com.example.service;

import com.example.model.Reservation;
import com.example.model.StatutReservation;
import com.example.repository.ReservationRepository;

import javax.persistence.EntityManager;
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
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByUtilisateur(Long utilisateurId) {
        return reservationRepository.findByUtilisateur(utilisateurId);
    }

    @Override
    public List<Reservation> getReservationsBySalle(Long salleId) {
        return reservationRepository.findBySalle(salleId);
    }

    @Override
    public List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByDateRange(start, end);
    }

    @Override
    public List<Reservation> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    @Override
    public boolean createReservation(Reservation reservation) {
        // Vérifier la disponibilité de la salle
        boolean isAvailable = isSalleAvailable(
                reservation.getSalle().getId(),
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (!isAvailable) {
            return false;
        }

        em.getTransaction().begin();
        try {
            reservationRepository.save(reservation);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {
        em.getTransaction().begin();
        try {
            reservationRepository.update(reservation);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void cancelReservation(Long id) {
        em.getTransaction().begin();
        try {
            Reservation reservation = reservationRepository.findById(id);
            if (reservation != null) {
                reservation.setStatut(StatutReservation.ANNULEE);
                reservationRepository.update(reservation);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteReservation(Long id) {
        em.getTransaction().begin();
        try {
            reservationRepository.delete(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean isSalleAvailable(Long salleId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.isSalleAvailable(salleId, start, end);
    }
}