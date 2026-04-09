package org.cesumar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String URL = "jdbc:h2:mem:solicitacoes;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void init() {
        try (
                Statement stmt = getConnection().createStatement();
        ) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS solicitacao (
                            id UUID PRIMARY KEY,
                            categoria VARCHAR(50),
                            descricao TEXT,
                            anexo_url VARCHAR(255),
                            localizacao VARCHAR(255),
                            anonima BOOLEAN,
                            solicitante UUID,
                            data_criacao TIMESTAMP
                        )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS usuario (
                        id UUID PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        idade BIGINT,
                        logradouro VARCHAR(255),
                        numero_logradouro VARCHAR(50),
                        cep VARCHAR(20)
                    )
                """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}