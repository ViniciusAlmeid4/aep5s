package org.cesumar.models.solicitacaoStatus.DTOs;

import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;

import java.util.UUID;

public class SolicitacaoStatusRequest {

    private SituacaoSolicitacaoStatus situacao;
    private UUID solicitacao;
    private UUID responsavel;
    private String descricao; // ✅ campo correto, alinhado com o banco

    public SolicitacaoStatusRequest(
            SituacaoSolicitacaoStatus situacao,
            UUID solicitacao,
            UUID responsavel,
            String descricao
    ) {
        this.situacao = situacao;
        this.solicitacao = solicitacao;
        this.responsavel = responsavel;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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