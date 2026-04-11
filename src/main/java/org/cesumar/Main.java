package org.cesumar;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacao.CategoriaSolicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.models.solicitacaoStatus.DTOs.SolicitacaoStatusRequest;
import org.cesumar.models.solicitacaoStatus.SituacaoSolicitacaoStatus;
import org.cesumar.models.solicitacaoStatus.SolicitacaoStatus;
import org.cesumar.models.usuario.Role;
import org.cesumar.models.usuario.UsuarioModel;
import org.cesumar.services.solicitacao.SolicitacaoService;
import org.cesumar.services.solicitacaoStatus.SolicitacaoStatusService;
import org.cesumar.services.usuarios.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        DatabaseConfig.init();

        UsuarioService usuarioService = new UsuarioService();
        SolicitacaoService solicitacaoService = new SolicitacaoService();
        SolicitacaoStatusService statusService = new SolicitacaoStatusService();

        seedDados(usuarioService, solicitacaoService, statusService);

        System.out.println("=== SISTEMA DE SOLICITAÇÕES ===");

        while (true) {
            UsuarioModel usuario = login(usuarioService);

            if (usuario == null) {
                System.out.println("Usuário ou senha inválidos. Tente novamente.");
                continue;
            }

            System.out.println("\nBem-vindo(a), " + usuario.getNome() + " [" + usuario.getRole() + "]");

            boolean logado = true;

            while (logado) {
                switch (usuario.getRole()) {
                    case CIDADAO  -> logado = menuCidadao(usuario, solicitacaoService, statusService);
                    case ATENDENTE -> logado = menuAtendente(solicitacaoService, statusService, usuario);
                    case ADMIN    -> logado = menuAdmin(usuarioService);
                }
            }

            System.out.println("Logout realizado.\n");
        }
    }

    private static UsuarioModel login(UsuarioService service) {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Optional<UsuarioModel> user = service.login(email, senha);
        return user.orElse(null);
    }

    private static boolean menuCidadao(
            UsuarioModel user,
            SolicitacaoService solService,
            SolicitacaoStatusService statusService
    ) throws Exception {
        System.out.println("\n=== MENU CIDADÃO ===");
        System.out.println("1. Criar solicitação");
        System.out.println("2. Listar minhas solicitações");
        System.out.println("3. Ver histórico de uma solicitação");
        System.out.println("9. Logout");
        System.out.println("0. Sair");

        int op = lerInt();

        switch (op) {
            case 1 -> criarSolicitacao(user, solService);
            case 2 -> listarSolicitacoes(user, solService);
            case 3 -> verHistorico(statusService);
            case 9 -> { return false; }
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }

        return true;
    }

    private static boolean menuAtendente(
            SolicitacaoService solService,
            SolicitacaoStatusService statusService,
            UsuarioModel user
    ) {
        System.out.println("\n=== MENU ATENDENTE ===");
        System.out.println("1. Listar todas as solicitações");
        System.out.println("2. Atualizar status de uma solicitação");
        System.out.println("3. Ver histórico de uma solicitação");
        System.out.println("9. Logout");
        System.out.println("0. Sair");

        int op = lerInt();

        switch (op) {
            case 1 -> listarTodasSolicitacoes(solService);
            case 2 -> atualizarStatus(solService, statusService, user);
            case 3 -> verHistorico(statusService);
            case 9 -> { return false; }
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }

        return true;
    }

    private static boolean menuAdmin(UsuarioService service) throws Exception {
        System.out.println("\n=== MENU ADMIN ===");
        System.out.println("1. Criar usuário");
        System.out.println("2. Listar usuários");
        System.out.println("9. Logout");
        System.out.println("0. Sair");

        int op = lerInt();

        switch (op) {
            case 1 -> criarUsuario(service);
            case 2 -> listarUsuarios(service);
            case 9 -> { return false; }
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }

        return true;
    }

    private static void criarSolicitacao(UsuarioModel user, SolicitacaoService service) throws Exception {
        System.out.println("\n-- Categorias disponíveis --");
        for (CategoriaSolicitacao c : CategoriaSolicitacao.values()) {
            System.out.println("  " + c.name());
        }

        System.out.print("Categoria: ");
        CategoriaSolicitacao categoria;
        try {
            categoria = CategoriaSolicitacao.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Categoria inválida.");
            return;
        }

        System.out.print("Descrição: ");
        String desc = scanner.nextLine();

        System.out.print("Localização: ");
        String local = scanner.nextLine();

        System.out.print("É anônima? (s/n): ");
        boolean anonima = scanner.nextLine().trim().equalsIgnoreCase("s");

        Solicitacao s = service.criarSolicitacao(new SolicitacaoRequest(
                categoria,
                desc,
                null,
                local,
                anonima ? null : user.getId(),
                anonima
        ));

        System.out.println("✔ Solicitação criada! ID: " + s.getId());
    }

    private static void listarSolicitacoes(UsuarioModel user, SolicitacaoService service) {
        List<Solicitacao> lista = service.listar(user.getId());

        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            return;
        }

        System.out.println("\n-- Suas solicitações --");
        for (Solicitacao s : lista) {
            imprimirSolicitacao(s);
        }
    }

    private static void listarTodasSolicitacoes(SolicitacaoService service) {
        List<Solicitacao> lista = service.listar(null);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação cadastrada.");
            return;
        }

        System.out.println("\n-- Todas as solicitações --");
        for (Solicitacao s : lista) {
            imprimirSolicitacao(s);
        }
    }

    private static void imprimirSolicitacao(Solicitacao s) {
        System.out.println("─────────────────────────────────");
        System.out.println("ID:          " + s.getId());
        System.out.println("Categoria:   " + s.getCategoria());
        System.out.println("Descrição:   " + s.getDescricao());
        System.out.println("Localização: " + s.getLocalizacao());
        System.out.println("Solicitante: " + s.getSafeSolicitante().map(UUID::toString).orElse("anônima"));
        System.out.println("Criada em:   " + s.getDtCriacao());

        if (s.getHistoricoStatus() != null && !s.getHistoricoStatus().isEmpty()) {
            SolicitacaoStatus ultimo = s.getHistoricoStatus()
                    .get(s.getHistoricoStatus().size() - 1);
            System.out.println("Status atual:" + ultimo.getSituacao());
        }
    }

    private static void atualizarStatus(
            SolicitacaoService solService,
            SolicitacaoStatusService statusService,
            UsuarioModel user
    ) {
        System.out.print("ID da solicitação: ");
        UUID id;
        try {
            id = UUID.fromString(scanner.nextLine().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Solicitacao> solOpt = solService.buscarPorId(id);
        if (solOpt.isEmpty()) {
            System.out.println("Solicitação não encontrada.");
            return;
        }

        System.out.println("\n-- Situações disponíveis --");
        for (SituacaoSolicitacaoStatus s : SituacaoSolicitacaoStatus.values()) {
            System.out.println("  " + s.name());
        }

        System.out.print("Nova situação: ");
        SituacaoSolicitacaoStatus situacao;
        try {
            situacao = SituacaoSolicitacaoStatus.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Situação inválida.");
            return;
        }

        System.out.print("Descrição/Observação (opcional, Enter para pular): ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isBlank()) descricao = null;

        statusService.adicionarStatus(new SolicitacaoStatusRequest(
                situacao,
                id,
                user.getId(),
                descricao
        ));

        System.out.println("✔ Status atualizado para: " + situacao);
    }

    private static void verHistorico(SolicitacaoStatusService service) {
        System.out.print("ID da solicitação: ");
        UUID id;
        try {
            id = UUID.fromString(scanner.nextLine().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido.");
            return;
        }

        List<SolicitacaoStatus> hist = service.listarHistorico(id);

        if (hist.isEmpty()) {
            System.out.println("Sem histórico para esta solicitação.");
            return;
        }

        System.out.println("\n-- Histórico de status --");
        for (SolicitacaoStatus s : hist) {
            System.out.println("─────────────────────────────────");
            System.out.println("Data:        " + s.getDataCriacao());
            System.out.println("Situação:    " + s.getSituacao());
            System.out.println("Responsável: " + (s.getResponsavel() != null ? s.getResponsavel() : "Sistema"));
            System.out.println("Descrição:   " + (s.getDescricao() != null ? s.getDescricao() : "-"));
        }
        System.out.println("─────────────────────────────────");
    }

    private static void criarUsuario(UsuarioService service) throws Exception {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Idade: ");
        Integer idade = lerInt();

        System.out.print("Logradouro: ");
        String rua = scanner.nextLine();

        System.out.print("Número: ");
        String numero = scanner.nextLine();

        System.out.print("CEP: ");
        String cep = scanner.nextLine();

        System.out.print("Role (ADMIN, ATENDENTE, CIDADAO): ");
        Role role;
        try {
            role = Role.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Role inválida.");
            return;
        }

        UsuarioModel u = service.criarUsuario(new UsuarioModel(
                null, nome, idade, rua, numero, cep, email, senha, role
        ));

        System.out.println("✔ Usuário criado! ID: " + u.getId());
    }

    private static void listarUsuarios(UsuarioService service) {
        List<UsuarioModel> lista = service.listar();

        if (lista.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("\n-- Usuários cadastrados --");
        for (UsuarioModel u : lista) {
            System.out.println(u.getId() + " | " + u.getNome() + " | " + u.getRole() + " | " + u.getEmail());
        }
    }

    private static void seedDados(
            UsuarioService usuarioService,
            SolicitacaoService solicitacaoService,
            SolicitacaoStatusService statusService
    ) {
        try {
            if (!usuarioService.listar().isEmpty()) {
                return;
            }

            System.out.println("Dados iniciais...");

            UsuarioModel admin = usuarioService.criarUsuario(new UsuarioModel(
                    null, "Admin", 30, "Sistema", "0", "00000-000",
                    "admin@admin.com", "admin", Role.ADMIN
            ));

            UsuarioModel atendente = usuarioService.criarUsuario(new UsuarioModel(
                    null, "Atendente", 28, "Rua A", "10", "87000-000",
                    "atendente@teste.com", "123", Role.ATENDENTE
            ));

            UsuarioModel cidadao = usuarioService.criarUsuario(new UsuarioModel(
                    null, "Cidadão", 22, "Rua B", "20", "87000-001",
                    "cidadao@teste.com", "123", Role.CIDADAO
            ));

            Solicitacao s1 = solicitacaoService.criarSolicitacao(new SolicitacaoRequest(
                    CategoriaSolicitacao.INFRAESTRUTURA,
                    "Buraco na rua principal",
                    null,
                    "Centro",
                    cidadao.getId(),
                    false
            ));

            Solicitacao s2 = solicitacaoService.criarSolicitacao(new SolicitacaoRequest(
                    CategoriaSolicitacao.SAUDE,
                    "Posto sem médico há 3 dias",
                    null,
                    "Zona 7",
                    cidadao.getId(),
                    true
            ));

            statusService.adicionarStatus(new SolicitacaoStatusRequest(
                    SituacaoSolicitacaoStatus.TRIAGEM,
                    s1.getId(),
                    atendente.getId(),
                    "Solicitação recebida e em análise"
            ));

            statusService.adicionarStatus(new SolicitacaoStatusRequest(
                    SituacaoSolicitacaoStatus.EM_EXECUCAO,
                    s1.getId(),
                    atendente.getId(),
                    "Equipe de obras enviada ao local"
            ));

            System.out.println("   Admin     → admin@admin.com     / admin");
            System.out.println("   Atendente → atendente@teste.com / 123");
            System.out.println("   Cidadão   → cidadao@teste.com   / 123");

        } catch (Exception e) {
            System.err.println("Erro ao criar dados iniciais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}