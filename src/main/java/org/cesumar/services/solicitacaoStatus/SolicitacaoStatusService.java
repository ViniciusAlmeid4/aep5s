package org.cesumar.services.solicitacaoStatus;

import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.repositories.solicitacaoStatusRepository.SolicitacaoStatusRepository;

import java.util.UUID;

public class SolicitacaoStatusService {

    private final SolicitacaoStatusRepository repository = new SolicitacaoStatusRepository();

    public void adicionarStatus(SolicitacaoStatusRequest req) {
        validar(req);
        repository.save(req);
    }

    private void validar(SolicitacaoStatusRequest req) {
        if (req.getSituacao() == null) {
            throw new IllegalArgumentException("Situação obrigatória");
        }

        if (req.getSolicitacao() == null) {
            throw new IllegalArgumentException("Solicitação obrigatória");
        }

        if (req.getSituacao() == SituacaoSolicitacaoStatus.RESOLVIDO
                && req.getResponsavel() == null) {
            throw new IllegalArgumentException("Status RESOLVIDO exige responsável");
        }
    }
}