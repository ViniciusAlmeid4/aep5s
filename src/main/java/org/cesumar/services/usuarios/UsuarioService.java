package org.cesumar.services.usuarios;

import org.cesumar.models.usuario.UsuarioModel;
import org.cesumar.repositories.usuarios.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository repository = new UsuarioRepository();

    public UsuarioModel criarUsuario(UsuarioModel usuario) throws Exception {
        validar(usuario);

        repository.buscarPorEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email já cadastrado.");
                });

        return repository.save(usuario);
    }

    public Optional<UsuarioModel> login(String email, String senha) {
        Optional<UsuarioModel> user = repository.buscarPorEmail(email);

        if (user.isPresent() && user.get().getSenha().equals(senha)) {
            return user;
        }

        return Optional.empty();
    }

    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return repository.buscarPorEmail(email);
    }

    public List<UsuarioModel> listar() {
        return repository.listar();
    }

    public void deletar(java.util.UUID id) {
        repository.deletar(id);
    }

    private void validar(UsuarioModel u) {

        if (u.getNome() == null || u.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }

        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obrigatório");
        }

        if (u.getSenha() == null || u.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha obrigatória");
        }

        if (u.getIdade() != null && u.getIdade() < 0) {
            throw new IllegalArgumentException("Idade inválida");
        }

        if (u.getRole() == null) {
            throw new IllegalArgumentException("Role obrigatória");
        }
    }
}