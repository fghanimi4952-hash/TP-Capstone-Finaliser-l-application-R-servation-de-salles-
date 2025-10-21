package com.example.util;

import com.example.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Random;

public class DataInitializer {
    private final EntityManagerFactory emf;
    private final Random random = new Random();

    public DataInitializer(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void initializeData() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Equipement[] equipements = createEquipements(em);
            Utilisateur[] utilisateurs = createUtilisateurs(em);
            Salle[] salles = createSalles(em, equipements);
            createReservations(em, utilisateurs, salles);

            em.getTransaction().commit();
            System.out.println(" Jeu de données initialisé avec succès !");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private Equipement[] createEquipements(EntityManager em) {
        Equipement[] eqs = {
                new Equipement("Projecteur", "Projecteur HD"),
                new Equipement("Tableau blanc", "Tableau magnétique"),
                new Equipement("Système audio", "4 haut-parleurs"),
                new Equipement("WiFi", "Connexion WiFi haut débit"),
                new Equipement("Climatisation", "Climatisation réglable")
        };
        for (Equipement e : eqs) em.persist(e);
        return eqs;
    }

    private Utilisateur[] createUtilisateurs(EntityManager em) {
        String[] noms = {"Martin", "Dupont", "Bernard", "Petit", "Durand"};
        String[] prenoms = {"Jean", "Sophie", "Marie", "Paul", "Lucie"};
        Utilisateur[] users = new Utilisateur[5];
        for (int i = 0; i < 5; i++) {
            users[i] = new Utilisateur(noms[i], prenoms[i],
                    prenoms[i].toLowerCase() + "." + noms[i].toLowerCase() + "@example.com");
            users[i].setDepartement("Département " + (i + 1));
            em.persist(users[i]);
        }
        return users;
    }

    private Salle[] createSalles(EntityManager em, Equipement[] eqs) {
        Salle[] salles = new Salle[3];
        for (int i = 0; i < 3; i++) {
            salles[i] = new Salle("Salle A" + (i + 1), 10 + i * 5);
            salles[i].setBatiment("Bâtiment A");
            salles[i].addEquipement(eqs[i % eqs.length]);
            em.persist(salles[i]);
        }
        return salles;
    }

    private void createReservations(EntityManager em, Utilisateur[] users, Salle[] salles) {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 10; i++) {
            Reservation r = new Reservation(
                    now.plusDays(random.nextInt(10)).withHour(9),
                    now.plusDays(random.nextInt(10)).withHour(11),
                    "Réunion n°" + (i + 1));
            r.setUtilisateur(users[random.nextInt(users.length)]);
            r.setSalle(salles[random.nextInt(salles.length)]);
            em.persist(r);
        }
    }
}
