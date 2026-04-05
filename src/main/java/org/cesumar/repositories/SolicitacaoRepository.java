package org.cesumar.repositories;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacao.CategoriaSolicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacao.Solicitacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoRepository {
    public static Solicitacao save(SolicitacaoRequest s) throws Exception {
        String sql = """
                    INSERT INTO solicitacao (
                        id, categoria, descricao, anexo_url, localizacao,
                        anonima, nome_solicitante, data_criacao
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.setString(2, s.getCategoria().name());
            ps.setString(3, s.getDescricao());
            ps.setString(4, s.getAnexoUrl());
            ps.setString(5, s.getLocalizacao());
            ps.setBoolean(6, s.isAnonima());
            ps.setObject(7, s.getSolicitante());
            ps.setTimestamp(8, Timestamp.valueOf(agora));

            if (ps.execute()) {
                return new Solicitacao(
                        id,
                        s.getCategoria(),
                        s.getDescricao(),
                        s.getAnexoUrl(),
                        s.getLocalizacao(),
                        s.getSolicitante(),
                        s.isAnonima(),
                        agora
                );
            } else {
                throw new SQLException("Erro ao inserir registro no banco de dados.");
            }
        }
    }

    public Optional<Solicitacao> buscarPorId(UUID id) {
        String sql = "SELECT * FROM solicitacao WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Solicitacao map(ResultSet rs) throws SQLException {
        return new Solicitacao(
                (UUID) rs.getObject("id"),
                CategoriaSolicitacao.valueOf(rs.getString("categoria")),
                rs.getString("descricao"),
                rs.getString("anexo_url"),
                rs.getString("localizacao"),
                (UUID) rs.getObject("solicitante"), // nullable
                rs.getBoolean("anonima"),
                rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }

    public List<Solicitacao> listar(Optional<UUID> solicitanteId) {

        String baseSql = "SELECT * FROM solicitacao";
        List<Solicitacao> lista = new ArrayList<>();

        boolean filtrar = solicitanteId.isPresent();

        String sql = filtrar
                ? baseSql + " WHERE solicitante = ?"
                : baseSql;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (filtrar) {
                ps.setObject(1, solicitanteId.get());
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(map(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
