package org.cesumar.services.usuarios;

import org.cesumar.models.usuario.UsuarioModel;
import org.cesumar.repositories.usuarios.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public UsuarioModel criarUsuario(UsuarioModel usuario) throws Exception {
        validarUsuario(usuario);
        return repository.save(usuario);
    }

    public Optional<UsuarioModel> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    public List<UsuarioModel> listar() {
        return repository.listar();
    }

    public UsuarioModel atualizar(UsuarioModel usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório para atualização.");
        }

        validarUsuario(usuario);

        return repository.atualizar(usuario);
    }

    public void deletar(UUID id) {
        repository.deletar(id);
    }

    private void validarUsuario(UsuarioModel usuario) {

        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (usuario.getIdade() != null && usuario.getIdade() < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa.");
        }

        if (usuario.getCep() == null || usuario.getCep().isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }

        if (usuario.getLogradouro() == null || usuario.getLogradouro().isBlank()) {
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        }

        if (usuario.getNumeroLogradouro() == null || usuario.getNumeroLogradouro().isBlank()) {
            throw new IllegalArgumentException("Número do logradouro é obrigatório.");
        }
    }
}