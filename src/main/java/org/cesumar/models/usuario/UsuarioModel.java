package org.cesumar.models.usuario;

import java.util.UUID;

public class UsuarioModel {

    private UUID id;
    private String nome;
    private Integer idade;
    private String logradouro;
    private String numeroLogradouro;
    private String cep;

    public UsuarioModel(UUID id, String nome, Integer idade, String logradouro, String numeroLogradouro, String cep) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.logradouro = logradouro;
        this.numeroLogradouro = numeroLogradouro;
        this.cep = cep;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public void setNumeroLogradouro(String numeroLogradouro) {
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
