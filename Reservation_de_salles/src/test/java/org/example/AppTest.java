package com.example;

import com.example.service.*;
import com.example.repository.*;
import com.example.util.*;
import com.example.test.TestScenarios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {

    public static void main(String[] args) {
        System.out.println("🚀 Démarrage de l’application Optimistic Locking Demo...");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("optimistic-locking-demo");
        EntityManager em = emf.createEntityManager();

        // Initialisation des dépôts
        SalleRepository salleRepo = new SalleRepositoryImpl(em);
        ReservationRepository reservationRepo = new ReservationRepositoryImpl(em);

        // Services
        SalleService salleService = new SalleServiceImpl(em, salleRepo);
        ReservationService reservationService = new ReservationServiceImpl(em, reservationRepo);

        // Initialisation des données
        DataInitializer initializer = new DataInitializer(emf);
        initializer.initializeData();

        // Tests principaux
        TestScenarios test = new TestScenarios(emf, salleService, reservationService);
        test.runAllTests();

        // Rapport de performance
        PerformanceReport report = new PerformanceReport(emf);
        report.generate();

        // Migration SQL optionnelle
        DatabaseMigrationTool migration = new DatabaseMigrationTool(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        migration.executeMigration();

        em.close();
        emf.close();

        System.out.println("\n🏁 Application terminée avec succès !");
    }
}
