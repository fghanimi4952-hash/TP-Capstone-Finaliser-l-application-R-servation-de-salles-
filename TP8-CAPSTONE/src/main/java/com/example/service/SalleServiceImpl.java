package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;

import javax.persistence.EntityManager;
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
    public Salle getSalleById(Long id) {
        return salleRepository.findById(id);
    }

    @Override
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    @Override
    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return salleRepository.findAvailableRooms(start, end);
    }

    @Override
    public List<Salle> searchRooms(Map<String, Object> criteria) {
        return salleRepository.searchRooms(criteria);
    }

    @Override
    public long countRooms() {
        return salleRepository.countRooms();
    }

    @Override
    public int getTotalPages(int pageSize) {
        long totalItems = countRooms();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    @Override
    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        return salleRepository.getPaginatedRooms(page, pageSize);
    }

    @Override
    public void createSalle(Salle salle) {
        em.getTransaction().begin();
        try {
            salleRepository.save(salle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void updateSalle(Salle salle) {
        em.getTransaction().begin();
        try {
            salleRepository.update(salle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteSalle(Long id) {
        em.getTransaction().begin();
        try {
            salleRepository.delete(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}