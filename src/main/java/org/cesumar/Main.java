package org.cesumar;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacao.CategoriaSolicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.repositories.solicitacao.SolicitacaoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig.init();
        SolicitacaoRepository repo = new SolicitacaoRepository();

        // 2. create test data
        UUID solicitanteId = UUID.randomUUID();

        SolicitacaoRequest req = new SolicitacaoRequest(
                CategoriaSolicitacao.INFRAESTRUTURA,
                "Buraco na rua",
                null,
                "Centro",
                solicitanteId,
                true
        );

        // 3. save
        Solicitacao saved = null;
        try {
            saved = repo.save(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Saved ID: " + saved.getId());

        // 4. buscarPorId
        Optional<Solicitacao> found = repo.buscarPorId(saved.getId());

        if (found.isPresent()) {
            System.out.println("Found: " + found.get().getDescricao());
        } else {
            throw new RuntimeException("Erro: não encontrou por ID");
        }

        // 5. listar sem filtro
        List<Solicitacao> all = repo.listar(null);
        System.out.println("Total registros: " + all.size());

        // 6. listar com filtro
        List<Solicitacao> filtrados = repo.listar(solicitanteId);
        System.out.println("Filtrados: " + filtrados.size());

        if (filtrados.isEmpty()) {
            throw new RuntimeException("Erro: filtro não funcionou");
        }

        // 7. testar anônima
        SolicitacaoRequest anonReq = new SolicitacaoRequest(
                CategoriaSolicitacao.SAUDE,
                "Posto sem médico",
                null,
                "Zona 7",
                null,
                true
        );

        Solicitacao saved1 = null;
        try {
            saved1 = repo.save(anonReq);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Solicitacao> anonList = repo.listar(null);

        long anonCount = anonList.stream()
                .filter(Solicitacao::isAnonima)
                .count();

        System.out.println("Anonimas: " + anonCount);

        System.out.println("✔ TESTES OK");
    }
}