package com.example.repository;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    void save(Reservation reservation);
    List<Reservation> findReservationsByUser(Utilisateur utilisateur);
    List<Reservation> findReservationsBySalle(Salle salle);
    List<Reservation> findReservationsBetween(LocalDateTime start, LocalDateTime end);
}
