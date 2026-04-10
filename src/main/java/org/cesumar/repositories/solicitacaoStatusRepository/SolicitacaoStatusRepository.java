package org.cesumar.repositories.solicitacaoStatusRepository;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.models.solicitacaoStatus.SolicitacaoStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoStatusRepository {
    public SolicitacaoStatus save(SolicitacaoStatusRequest s) {
        String sql = """
                    INSERT INTO solicitacao_status (
                        situacao, solicitacao, responsavel, data_criacao
                    ) VALUES (?, ?, ?, ?)
                """;

        LocalDateTime agora = LocalDateTime.now();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getSituacao().name());
            ps.setObject(2, s.getSolicitacao());
            ps.setObject(3, s.getResponsavel()); // pode ser null
            ps.setTimestamp(4, Timestamp.valueOf(agora));

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);

                return new SolicitacaoStatus(
                        id,
                        s.getSituacao(),
                        s.getSolicitacao(),
                        s.getResponsavel(),
                        agora
                );
            }

            throw new SQLException("Erro ao obter ID gerado");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<SolicitacaoStatus> buscarPorId(Integer id) {
        String sql = "SELECT * FROM solicitacao_status WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SolicitacaoStatus> listarPorSolicitacao(UUID solicitacaoId) {
        String sql = """
                    SELECT * FROM solicitacao_status
                    WHERE solicitacao = ?
                    ORDER BY data_criacao ASC
                """;

        List<SolicitacaoStatus> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, solicitacaoId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(map(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SolicitacaoStatus map(ResultSet rs) throws SQLException {
        return new SolicitacaoStatus(
                rs.getInt("id"),
                SituacaoSolicitacaoStatus.valueOf(rs.getString("situacao")),
                (UUID) rs.getObject("solicitacao"),
                (UUID) rs.getObject("responsavel"), // nullable
                rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }
}
