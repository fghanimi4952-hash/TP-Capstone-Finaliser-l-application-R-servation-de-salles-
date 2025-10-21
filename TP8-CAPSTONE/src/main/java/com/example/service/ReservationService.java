package com.example.service;

import com.example.model.Reservation;
import com.example.model.StatutReservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Reservation getReservationById(Long id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByUtilisateur(Long utilisateurId);
    List<Reservation> getReservationsBySalle(Long salleId);
    List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end);
    List<Reservation> getReservationsByStatut(StatutReservation statut);
    boolean createReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void cancelReservation(Long id);
    void deleteReservation(Long id);
    boolean isSalleAvailable(Long salleId, LocalDateTime start, LocalDateTime end);
}