package org.cesumar.models.solicitacaoStatus.DTOs;

import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;

import java.util.UUID;

public class SolicitacaoStatusRequest {

    private SituacaoSolicitacaoStatus situacao;
    private UUID solicitacao;
    private UUID responsavel;
    private java.time.LocalDateTime dataCriacao;

    public SolicitacaoStatusRequest(
            SituacaoSolicitacaoStatus situacao,
            UUID solicitacao,
            UUID responsavel,
            java.time.LocalDateTime dataCriacao
    ) {
        this.situacao = situacao;
        this.solicitacao = solicitacao;
        this.responsavel = responsavel;
        this.dataCriacao = dataCriacao;
        this.validar();
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

    public java.time.LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(java.time.LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void validar() {
        if (situacao == null) {
            throw new IllegalArgumentException("Situação obrigatória");
        }

        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação é obrigatória");
        }
    }
}