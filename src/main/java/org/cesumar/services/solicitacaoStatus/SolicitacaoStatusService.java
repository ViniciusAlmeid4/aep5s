package org.cesumar.services.solicitacaoStatus;

import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.models.solicitacaoStatus.SolicitacaoStatus;
import org.cesumar.repositories.solicitacao.SolicitacaoRepository;
import org.cesumar.repositories.solicitacaoStatusRepository.SolicitacaoStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoStatusService {

    private final SolicitacaoStatusRepository repository = new SolicitacaoStatusRepository();
    private final SolicitacaoRepository solicitacaoRepository = new SolicitacaoRepository();

    public void adicionarStatus(SolicitacaoStatusRequest req) {
        validar(req);
        repository.save(req);
    }

    public List<SolicitacaoStatus> listarHistorico(UUID solicitacaoId) {
        return repository.listarPorSolicitacao(solicitacaoId);
    }

    public Optional<Solicitacao> buscarComHistorico(UUID id) {
        Optional<Solicitacao> sol = solicitacaoRepository.buscarPorId(id);

        sol.ifPresent(s -> {
            List<SolicitacaoStatus> hist =
                    repository.listarPorSolicitacao(s.getId());
            s.setHistoricoStatus(hist);
        });

        return sol;
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