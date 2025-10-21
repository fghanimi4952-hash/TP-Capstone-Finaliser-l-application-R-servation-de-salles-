package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseMigrationTool {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DatabaseMigrationTool(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void executeMigration() {
        System.out.println("🚀 Démarrage de la migration...");
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("migration_v2.sql");
            if (input == null) throw new RuntimeException("Fichier migration_v2.sql introuvable");
            String sql = new BufferedReader(new InputStreamReader(input))
                    .lines().collect(Collectors.joining("\n"));

            for (String cmd : sql.split(";")) {
                if (!cmd.trim().isEmpty()) {
                    try (Statement st = conn.createStatement()) {
                        st.execute(cmd);
                    }
                }
            }
            System.out.println(" Migration exécutée avec succès !");
        } catch (Exception e) {
            System.err.println(" Erreur migration : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
