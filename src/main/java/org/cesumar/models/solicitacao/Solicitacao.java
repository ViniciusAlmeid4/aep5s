package org.cesumar.models.solicitacao;

import java.time.LocalDateTime;
import java.util.UUID;

abstract public class Solicitacao {

    private UUID id;
    private CategoriaSolicitacao categoria;
    private String descricao;
    private String anexoUrl; // pode ser null
    private String localizacao;
    private String nomeSolicitante; // null se anônimo
    private LocalDateTime dtCriacao;
    private Boolean isAnonima;

    public Solicitacao(
            UUID id,
            CategoriaSolicitacao categoria,
            String descricao,
            String anexoUrl,
            String localizacao,
            String nomeSolicitante,
            Boolean anonima,
            LocalDateTime dtCriacao
    ) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
        this.anexoUrl = anexoUrl;
        this.localizacao = localizacao;
        this.nomeSolicitante = nomeSolicitante;
        this.dtCriacao = dtCriacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoriaSolicitacao getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaSolicitacao categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAnexoUrl() {
        return anexoUrl;
    }

    public void setAnexoUrl(String anexoUrl) {
        this.anexoUrl = anexoUrl;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Boolean getAnonima() {
        return isAnonima;
    }

    public void setAnonima(Boolean anonima) {
        isAnonima = anonima;
    }
}