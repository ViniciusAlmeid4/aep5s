package org.cesumar.models.solicitacao;

import org.cesumar.models.solicitacaoStatus.SolicitacaoStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Solicitacao {
    private UUID id;
    private CategoriaSolicitacao categoria;
    private String descricao;
    private String anexoUrl; // pode ser null
    private String localizacao;
    private UUID solicitante; // null se anônimo
    private LocalDateTime dtCriacao;
    private Boolean isAnonima;

    // ✅ NEW: histórico de status
    private List<SolicitacaoStatus> historicoStatus;

    public List<SolicitacaoStatus> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<SolicitacaoStatus> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

    public Solicitacao(
            UUID id,
            CategoriaSolicitacao categoria,
            String descricao,
            String anexoUrl,
            String localizacao,
            UUID solicitante,
            Boolean anonima,
            LocalDateTime dtCriacao
    ) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
        this.anexoUrl = anexoUrl;
        this.localizacao = localizacao;
        this.solicitante = solicitante;
        this.isAnonima = anonima;
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

    public Optional<UUID> getSolicitante() {
        Optional<UUID> sol;
        if (!isAnonima) {
            sol = Optional.ofNullable(solicitante);
        } else {
            sol = Optional.empty();
        }
        return sol;
    }

    public void setSolicitante(UUID solicitante) {
        this.solicitante = solicitante;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Boolean isAnonima() {
        return isAnonima;
    }

    public void setAnonima(Boolean anonima) {
        isAnonima = anonima;
    }
}