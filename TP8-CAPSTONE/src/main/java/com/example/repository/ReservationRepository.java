package com.example.repository;

import com.example.model.Reservation;
import com.example.model.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    Reservation findById(Long id);
    List<Reservation> findAll();
    List<Reservation> findByUtilisateur(Long utilisateurId);
    List<Reservation> findBySalle(Long salleId);
    List<Reservation> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByStatut(StatutReservation statut);
    void save(Reservation reservation);
    void update(Reservation reservation);
    void delete(Long id);
    boolean isSalleAvailable(Long salleId, LocalDateTime start, LocalDateTime end);
}