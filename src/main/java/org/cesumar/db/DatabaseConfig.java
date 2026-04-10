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
                        CREATE TABLE IF NOT EXISTS usuarios (
                            id UUID PRIMARY KEY,
                            nome VARCHAR(255) NOT NULL,
                            idade INT,
                            logradouro VARCHAR(255),
                            numero_logradouro VARCHAR(50),
                            cep VARCHAR(20)
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS solicitacao (
                            id UUID PRIMARY KEY,
                            categoria VARCHAR(50),
                            descricao TEXT,
                            anexo_url VARCHAR(255),
                            localizacao VARCHAR(255),
                            anonima BOOLEAN,
                            solicitante UUID,
                            data_criacao TIMESTAMP,
                            CONSTRAINT fk_solicitacao_solicitante
                                    FOREIGN KEY (solicitante)
                                    REFERENCES usuarios(id)
                                    ON DELETE SET NULL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS solicitacao_status (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            situacao ENUM('ABERTO', 'TRIAGEM', 'EM_EXECUCAO', 'RESOLVIDO', 'ENCERRADO'),
                            solicitacao UUID,
                            responsavel UUID,
                            data_criacao TIMESTAMP,
                            CONSTRAINT fk_status_solicitacao
                                FOREIGN KEY (solicitacao)
                                REFERENCES solicitacao(id)
                                ON DELETE CASCADE,
                            CONSTRAINT fk_status_responsavel
                                FOREIGN KEY (responsavel)
                                REFERENCES usuarios(id)
                                ON DELETE SET NULL
                        );
                    """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}