package com.example.util;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

public class PerformanceReport {

    private final EntityManagerFactory emf;

    public PerformanceReport(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void generate() {
        Session session = emf.createEntityManager().unwrap(Session.class);
        Statistics stats = session.getSessionFactory().getStatistics();

        System.out.println("\n=== RAPPORT DE PERFORMANCE ===");
        System.out.println("Date : " + LocalDateTime.now());
        System.out.println("Nombre de requêtes exécutées : " + stats.getQueryExecutionCount());
        System.out.println("Entités chargées : " + stats.getEntityLoadCount());
        System.out.println("Hits du cache L2 : " + stats.getSecondLevelCacheHitCount());
        System.out.println("Miss du cache L2 : " + stats.getSecondLevelCacheMissCount());
        System.out.println("==============================\n");
    }
}
