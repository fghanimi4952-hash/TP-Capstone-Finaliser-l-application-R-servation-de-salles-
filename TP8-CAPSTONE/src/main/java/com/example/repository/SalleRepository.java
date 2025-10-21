package com.example.repository;

import com.example.model.Salle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalleRepository {
    Salle findById(Long id);
    List<Salle> findAll();
    List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end);
    List<Salle> searchRooms(Map<String, Object> criteria);
    long countRooms();
    List<Salle> getPaginatedRooms(int page, int pageSize);
    void save(Salle salle);
    void update(Salle salle);
    void delete(Long id);
}