package org.cesumar.repositories.usuario;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.usuario.UsuarioModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository {

    public UsuarioModel save(UsuarioModel u) throws Exception {
        String sql = """
                INSERT INTO usuario (
                    id, nome, idade, logradouro, numero_logradouro, cep
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        UUID id = UUID.randomUUID();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.setString(2, u.getNome());
            ps.setLong(3, u.getIdade());
            ps.setString(4, u.getLogradouro());
            ps.setString(5, u.getNumeroLogradouro());
            ps.setString(6, u.getCep());

            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new UsuarioModel(
                        id,
                        u.getNome(),
                        u.getIdade(),
                        u.getLogradouro(),
                        u.getNumeroLogradouro(),
                        u.getCep()
                );
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
    }

    public Optional<UsuarioModel> buscarPorId(UUID id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

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

    public List<UsuarioModel> listar() {
        String sql = "SELECT * FROM usuario";
        List<UsuarioModel> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(map(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(UUID id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UsuarioModel atualizar(UsuarioModel u) {
        String sql = """
                UPDATE usuario
                SET nome = ?, idade = ?, logradouro = ?, numero_logradouro = ?, cep = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setLong(2, u.getIdade());
            ps.setString(3, u.getLogradouro());
            ps.setString(4, u.getNumeroLogradouro());
            ps.setString(5, u.getCep());
            ps.setObject(6, u.getId());

            int rows = ps.executeUpdate();

            if (rows == 1) {
                return u;
            } else {
                throw new RuntimeException("Usuário não encontrado para atualização.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UsuarioModel map(ResultSet rs) throws SQLException {
        return new UsuarioModel(
                (UUID) rs.getObject("id"),
                rs.getString("nome"),
                rs.getLong("idade"),
                rs.getString("logradouro"),
                rs.getString("numero_logradouro"),
                rs.getString("cep")
        );
    }
}