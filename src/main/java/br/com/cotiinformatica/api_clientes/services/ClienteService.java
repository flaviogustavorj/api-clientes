package br.com.cotiinformatica.api_clientes.services;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;

import java.util.List;

public class ClienteService {

    //Método para desenvolver as regras de negócio para cadastro do cliente
    public void cadastrarCliente(ClienteRequest request) throws Exception {

        //Verificar se o nome está preenchido
        if(request.nome() == null || request.nome().trim().isEmpty() || request.nome().trim().length() < 3) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório e deve ter pelo menos 3 caracteres.");
        }

        //Verifica o cpf
        String cpfLimpo = request.cpf().replaceAll("[^\\d]", ""); // Remove espaços, pontos, etc.
        if(cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("O CPF do cliente é obrigatório e deve conter exatamente 11 dígitos numéricos.");
        }

        var clienteRepository = new ClienteRepository();
        if(ClienteRepository.cpfExistente(request.cpf())){
            throw new IllegalArgumentException("O CPF do cliente já está cadastrado no sistema.");
        }

        var cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setCpf(cpfLimpo);

        clienteRepository.inserir(cliente);
    }

    //Método para executar a pesquisa de clientes por nome
    public List<Cliente> pesquisarCliente(String nome) throws Exception {

        //Verificar se o nome do cliente tem pelo menos 5 caracteres
        if(nome == null || nome.trim().isEmpty() || nome.trim().length() < 5) {
            throw new IllegalArgumentException("O nome do cliente deve conter pelo menos 5 caracteres para realizar a pesquisa.");
        }
        //Consultar os clientes no banco de dados
        else {
            var clienteRepository = new ClienteRepository();
            var lista = clienteRepository.listar(nome);

            return lista;
        }
    }
}
