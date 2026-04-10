package org.cesumar.repositories.usuarios;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.usuario.Role;
import org.cesumar.models.usuario.UsuarioModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepository {

    public UsuarioModel save(UsuarioModel u) throws Exception {
        String sql = """
                INSERT INTO usuarios (
                    id, nome, idade, logradouro, numero_logradouro, cep,
                    email, senha, role
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        UUID id = UUID.randomUUID();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.setString(2, u.getNome());
            ps.setObject(3, u.getIdade());
            ps.setString(4, u.getLogradouro());
            ps.setString(5, u.getNumeroLogradouro());
            ps.setString(6, u.getCep());
            ps.setString(7, u.getEmail());
            ps.setString(8, u.getSenha());
            ps.setString(9, u.getRole().name());

            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new UsuarioModel(
                        id,
                        u.getNome(),
                        u.getIdade(),
                        u.getLogradouro(),
                        u.getNumeroLogradouro(),
                        u.getCep(),
                        u.getEmail(),
                        u.getSenha(),
                        u.getRole()
                );
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
    }

    public Optional<UsuarioModel> buscarPorId(UUID id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

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

    public Optional<UsuarioModel> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

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
        String sql = "SELECT * FROM usuarios";
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
        String sql = "DELETE FROM usuarios WHERE id = ?";

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
                UPDATE usuarios
                SET nome = ?, idade = ?, logradouro = ?, numero_logradouro = ?, cep = ?,
                    email = ?, senha = ?, role = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setObject(2, u.getIdade());
            ps.setString(3, u.getLogradouro());
            ps.setString(4, u.getNumeroLogradouro());
            ps.setString(5, u.getCep());
            ps.setString(6, u.getEmail());
            ps.setString(7, u.getSenha());
            ps.setString(8, u.getRole().name());
            ps.setObject(9, u.getId());

            int rows = ps.executeUpdate();

            if (rows == 1) {
                return u;
            } else {
                throw new RuntimeException("Usuário não encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UsuarioModel map(ResultSet rs) throws SQLException {
        return new UsuarioModel(
                (UUID) rs.getObject("id"),
                rs.getString("nome"),
                (Integer) rs.getObject("idade"),
                rs.getString("logradouro"),
                rs.getString("numero_logradouro"),
                rs.getString("cep"),
                rs.getString("email"),
                rs.getString("senha"),
                Role.valueOf(rs.getString("role"))
        );
    }
}