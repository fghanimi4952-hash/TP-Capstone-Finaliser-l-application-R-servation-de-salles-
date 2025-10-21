package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalleServiceImpl implements SalleService {

    private final EntityManager em;
    private final SalleRepository salleRepository;

    public SalleServiceImpl(EntityManager em, SalleRepository salleRepository) {
        this.em = em;
        this.salleRepository = salleRepository;
    }

    @Override
    public List<Salle> findAvailableRooms(LocalDateTime dateDebut, LocalDateTime dateFin) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Salle> salles = salleRepository.findAvailableRooms(dateDebut, dateFin);
            tx.commit();
            return salles;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Salle> searchRooms(Map<String, Object> criteres) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Salle> salles = salleRepository.searchRooms(criteres);
            tx.commit();
            return salles;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        return salleRepository.getPaginatedRooms(page, pageSize);
    }

    @Override
    public long countRooms() {
        return salleRepository.countRooms();
    }

    @Override
    public int getTotalPages(int pageSize) {
        long count = countRooms();
        return (int) Math.ceil((double) count / pageSize);
    }
}
