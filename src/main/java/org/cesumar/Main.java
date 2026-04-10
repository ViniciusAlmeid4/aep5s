package org.cesumar;

import org.cesumar.db.DatabaseConfig;
import org.cesumar.models.solicitacao.CategoriaSolicitacao;
import org.cesumar.models.solicitacao.DTOs.SolicitacaoRequest;
import org.cesumar.models.solicitacao.Solicitacao;
import org.cesumar.models.usuario.Role;
import org.cesumar.models.usuario.UsuarioModel;
import org.cesumar.services.solicitacao.SolicitacaoService;
import org.cesumar.services.usuarios.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        DatabaseConfig.init();

        UsuarioService usuarioService = new UsuarioService();
        SolicitacaoService solicitacaoService = new SolicitacaoService();

        criarUsuarioPadrao(usuarioService);

        System.out.println("=== SISTEMA ===");

        while (true) {
            UsuarioModel usuario = login(usuarioService);

            if (usuario == null) {
                System.out.println("Falha no login.");
                continue; // volta pro login
            }

            System.out.println("Logado como: " + usuario.getNome() + " (" + usuario.getRole() + ")");

            boolean logado = true;

            while (logado) {
                switch (usuario.getRole()) {
                    case CIDADAO -> logado = menuCidadao(usuario, solicitacaoService);
                    case ATENDENTE -> logado = menuAtendente(solicitacaoService);
                    case ADMIN -> logado = menuAdmin(usuarioService);
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

    private static boolean menuCidadao(UsuarioModel user, SolicitacaoService service) throws Exception {
        System.out.println("\n=== MENU CIDADAO ===");
        System.out.println("1. Criar solicitação");
        System.out.println("2. Listar minhas solicitações");
        System.out.println("9. Logout");
        System.out.println("0. Sair");

        int op = lerInt();

        switch (op) {
            case 1 -> criarSolicitacao(user, service);
            case 2 -> listarSolicitacoes(user, service);
            case 9 -> {
                return false; // logout
            }
            case 0 -> System.exit(0);
        }

        return true;
    }

    private static boolean menuAtendente(SolicitacaoService service) {
        System.out.println("\n=== MENU ATENDENTE ===");
        System.out.println("1. Listar todas solicitações");
        System.out.println("9. Logout");
        System.out.println("0. Sair");

        int op = lerInt();

        switch (op) {
            case 1 -> {
                List<Solicitacao> lista = service.listar(null);
                lista.forEach(s ->
                        System.out.println(s.getId() + " - " + s.getDescricao())
                );
            }
            case 9 -> {
                return false;
            }
            case 0 -> System.exit(0);
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
            case 9 -> {
                return false;
            }
            case 0 -> System.exit(0);
        }

        return true;
    }

    private static void criarUsuarioPadrao(UsuarioService service) {
        String email = "admin@admin.com";

        try {
            if (service.buscarPorEmail(email).isPresent()) {
                return;
            }

            service.criarUsuario(new UsuarioModel(
                    null,
                    "Administrador",
                    30,
                    "Sistema",
                    "0",
                    "00000-000",
                    email,
                    "admin",
                    Role.ADMIN
            ));

            System.out.println("Usuário padrão criado: admin@admin.com / admin");

        } catch (Exception ignored) {}
    }

    private static void criarSolicitacao(UsuarioModel user, SolicitacaoService service) throws Exception {
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();

        System.out.print("Localização: ");
        String local = scanner.nextLine();

        Solicitacao s = service.criarSolicitacao(new SolicitacaoRequest(
                CategoriaSolicitacao.INFRAESTRUTURA,
                desc,
                null,
                local,
                user.getId(),
                false
        ));

        System.out.println("Criado com ID: " + s.getId());
    }

    private static void listarSolicitacoes(UsuarioModel user, SolicitacaoService service) {
        List<Solicitacao> lista = service.listar(user.getId());

        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            return;
        }

        lista.forEach(s ->
                System.out.println(s.getId() + " - " + s.getDescricao())
        );
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

        System.out.print("Role (ADMIN, ATENDENTE, CIDADAO): ");
        Role role = Role.valueOf(scanner.nextLine().toUpperCase());

        UsuarioModel u = service.criarUsuario(new UsuarioModel(
                null,
                nome,
                idade,
                "Rua X",
                "123",
                "00000-000",
                email,
                senha,
                role
        ));

        System.out.println("Usuário criado com ID: " + u.getId());
    }

    private static void listarUsuarios(UsuarioService service) {
        service.listar().forEach(u ->
                System.out.println(u.getId() + " - " + u.getNome() + " (" + u.getRole() + ")")
        );
    }

    private static int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}