package org.cesumar.models.solicitacaoStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class SolicitacaoStatus {

    private Integer id;
    private SituacaoSolicitacaoStatus situacao;
    private UUID solicitacao;
    private UUID responsavel;
    private LocalDateTime dataCriacao;

    public SolicitacaoStatus(
            Integer id,
            SituacaoSolicitacaoStatus situacao,
            UUID solicitacao,
            UUID responsavel,
            LocalDateTime dataCriacao
    ) {
        this.id = id;
        this.situacao = situacao;
        this.solicitacao = solicitacao;
        this.responsavel = responsavel;
        this.dataCriacao = dataCriacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SituacaoSolicitacaoStatus getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoSolicitacaoStatus situacao) {
        this.situacao = situacao;
    }

    public UUID getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(UUID solicitacao) {
        this.solicitacao = solicitacao;
    }

    public UUID getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UUID responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}