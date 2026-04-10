package org.cesumar.models.usuario;

import java.util.UUID;

public class UsuarioModel {

    private UUID id;
    private String nome;
    private Integer idade;
    private String logradouro;
    private String numeroLogradouro;
    private String cep;

    private String email;
    private String senha;
    private Role role;

    public UsuarioModel(
            UUID id,
            String nome,
            Integer idade,
            String logradouro,
            String numeroLogradouro,
            String cep,
            String email,
            String senha,
            Role role
    ) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.logradouro = logradouro;
        this.numeroLogradouro = numeroLogradouro;
        this.cep = cep;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public String getCep() {
        return cep;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Role getRole() {
        return role;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}