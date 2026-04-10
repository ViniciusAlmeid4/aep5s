package org.cesumar.services.solicitacao;

import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.repositories.solicitacao.SolicitacaoRepository;
import org.cesumar.repositories.solicitacaoStatusRepository.SolicitacaoStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepo = new SolicitacaoRepository();
    private final SolicitacaoStatusRepository statusRepo = new SolicitacaoStatusRepository();

    public Solicitacao criarSolicitacao(SolicitacaoRequest req) throws Exception {
        Solicitacao solicitacao = solicitacaoRepo.save(req);

        statusRepo.save(new SolicitacaoStatusRequest(
                SituacaoSolicitacaoStatus.ABERTO,
                solicitacao.getId(),
                null,
                null
        ));

        return solicitacao;
    }

    public Optional<Solicitacao> buscarPorId(UUID id) {
        return solicitacaoRepo.buscarPorId(id);
    }

    public List<Solicitacao> listar(UUID solicitanteId) {
        return solicitacaoRepo.listar(solicitanteId);
    }
}