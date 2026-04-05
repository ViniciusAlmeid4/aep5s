package org.cesumar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String URL = "jdbc:h2:mem:solicitacoes;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            init();
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }

    private static void init() {
        try (
                Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS solicitacao (
                            id UUID PRIMARY KEY,
                            categoria VARCHAR(50),
                            descricao TEXT,
                            anexo_url VARCHAR(255),
                            localizacao VARCHAR(255),
                            anonima BOOLEAN,
                            nome_solicitante VARCHAR(255),
                            data_criacao TIMESTAMP
                        )
                    """);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}