package org.cesumar;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacao.CategoriaSolicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.models.usuario.UsuarioModel;
import org.cesumar.repositories.solicitacao.SolicitacaoRepository;
import org.cesumar.repositories.solicitacaoStatusRepository.SolicitacaoStatusRepository;
import org.cesumar.repositories.usuarios.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig.init();

        org.cesumar.repositories.usuarios.UsuarioRepository usuarioRepo = new UsuarioRepository();
        SolicitacaoRepository solicitacaoRepo = new SolicitacaoRepository();
        SolicitacaoStatusRepository statusRepo = new SolicitacaoStatusRepository();

        // -----------------------------------
        // 1. Criar usuário
        // -----------------------------------
        UsuarioModel usuario;
        try {
            usuario = usuarioRepo.save(new UsuarioModel(
                    null,
                    "João",
                    25,
                    "Rua A",
                    "123",
                    "87000-000"
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Usuario criado: " + usuario.getId());

        // -----------------------------------
        // 2. Criar solicitação vinculada ao usuário
        // -----------------------------------
        Solicitacao solicitacao;
        try {
            solicitacao = solicitacaoRepo.save(new SolicitacaoRequest(
                    CategoriaSolicitacao.INFRAESTRUTURA,
                    "Buraco na rua",
                    null,
                    "Centro",
                    usuario.getId(),
                    false
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Solicitacao criada: " + solicitacao.getId());

        // -----------------------------------
        // 3. Criar histórico de status
        // -----------------------------------
        statusRepo.save(new SolicitacaoStatusRequest(
                SituacaoSolicitacaoStatus.ABERTO,
                solicitacao.getId(),
                usuario.getId(),
                null
        ));

        statusRepo.save(new SolicitacaoStatusRequest(
                SituacaoSolicitacaoStatus.TRIAGEM,
                solicitacao.getId(),
                usuario.getId(),
                null
        ));

        statusRepo.save(new SolicitacaoStatusRequest(
                SituacaoSolicitacaoStatus.RESOLVIDO,
                solicitacao.getId(),
                usuario.getId(),
                null
        ));

        // -----------------------------------
        // 4. Buscar solicitação com histórico
        // -----------------------------------
        Optional<Solicitacao> found = solicitacaoRepo.buscarPorId(solicitacao.getId());

        if (found.isEmpty()) {
            throw new RuntimeException("Erro: não encontrou solicitação");
        }

        Solicitacao s = found.get();

        System.out.println("Descricao: " + s.getDescricao());

        List<?> historico = s.getHistoricoStatus();

        System.out.println("Qtd status: " + historico.size());

        if (historico.size() != 3) {
            throw new RuntimeException("Erro: histórico incorreto");
        }

        // -----------------------------------
        // 5. Validar ordenação
        // -----------------------------------
        var primeiro = s.getHistoricoStatus().get(0);

        System.out.println("Primeiro status: " + primeiro.getSituacao());

        if (primeiro.getSituacao() != SituacaoSolicitacaoStatus.ABERTO) {
            throw new RuntimeException("Erro: ordem incorreta");
        }

        System.out.println("✔ Histórico OK");

        // -----------------------------------
        // 6. Testar FK ON DELETE SET NULL
        // -----------------------------------
        usuarioRepo.deletar(usuario.getId());

        Optional<Solicitacao> afterDelete = solicitacaoRepo.buscarPorId(solicitacao.getId());

        if (afterDelete.isEmpty()) {
            throw new RuntimeException("Erro: solicitacao sumiu após deletar usuário");
        }

        System.out.println("Usuario deletado, solicitacao ainda existe ✔");

        // -----------------------------------
        // 7. Testar FK CASCADE (status)
        // -----------------------------------
        // (Opcional: você pode deletar a solicitacao direto no banco)

        System.out.println("✔ TESTES COMPLETOS OK");
    }
}