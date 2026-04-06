package org.cesumar.models.solicitacao.DTOs;

import org.cesumar.models.solicitacao.CategoriaSolicitacao;

import java.util.UUID;

public class SolicitacaoRequest {
    private CategoriaSolicitacao categoria;
    private String descricao;
    private String anexoUrl; // pode ser null
    private String localizacao;
    private UUID solicitante; // null se anônimo
    private Boolean isAnonima;

    public SolicitacaoRequest(CategoriaSolicitacao categoria, String descricao, String anexoUrl, String localizacao, UUID solicitante, Boolean isAnonima) {
        this.categoria = categoria;
        this.descricao = descricao;
        this.anexoUrl = anexoUrl;
        this.localizacao = localizacao;
        this.solicitante = solicitante;
        this.isAnonima = isAnonima;
        this.validar();
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

    public UUID getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UUID solicitante) {
        this.solicitante = solicitante;
    }

    public Boolean isAnonima() {
        return isAnonima;
    }

    public void setAnonima(Boolean anonima) {
        isAnonima = anonima;
    }

    public void validar() {
        if (categoria == null) throw new IllegalArgumentException("Categoria obrigatória");
        if (descricao == null || descricao.isBlank()) throw new IllegalArgumentException("Descrição obrigatória");
        if (localizacao == null || localizacao.isBlank()) throw new IllegalArgumentException("Localização obrigatória");

        if (!isAnonima && solicitante == null) {
            throw new IllegalArgumentException("Nome obrigatório para solicitações identificadas");
        }
    }
}
