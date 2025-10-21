package com.example.service;

import com.example.model.Salle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalleService {
    Salle getSalleById(Long id);
    List<Salle> getAllSalles();
    List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end);
    List<Salle> searchRooms(Map<String, Object> criteria);
    long countRooms();
    int getTotalPages(int pageSize);
    List<Salle> getPaginatedRooms(int page, int pageSize);
    void createSalle(Salle salle);
    void updateSalle(Salle salle);
    void deleteSalle(Long id);
}