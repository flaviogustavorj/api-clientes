package br.com.cotiinformatica.api_clientes.services;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.entities.Endereco;
import br.com.cotiinformatica.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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

        if (request.enderecos() == null || request.enderecos().length == 0) {
            throw new IllegalArgumentException("O cliente deve possuir pelo menos um endereço cadastrado.");
        }

        if(clienteRepository.cpfExistente(request.cpf())){
            throw new IllegalArgumentException("O CPF do cliente já está cadastrado no sistema.");
        }

        var cliente = new Cliente();
        cliente.setEnderecos(new ArrayList<>()); //Instanciando a lista de endereços do cliente

        cliente.setNome(request.nome());
        cliente.setCpf(cpfLimpo);

        for(var item : request.enderecos()) {
            var endereco = new Endereco();
            endereco.setLogradouro(item.logradouro());
            endereco.setNumero(item.numero());
            endereco.setComplemento(item.complemento());
            endereco.setBairro(item.bairro());
            endereco.setCidade(item.cidade());
            endereco.setEstado(item.estado());
            endereco.setCep(item.cep());

            cliente.getEnderecos().add(endereco); //Adiciona o endereço ao cliente
        }

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
            var lista = clienteRepository.listar(nome);

            return lista;
        }
    }

    //Método para executar a exclusão do cliente
    public void excluirCliente(Integer id) throws Exception {
        var result = clienteRepository.excluir(id);
        if (!result) {
            throw new IllegalArgumentException("Não foi possível excluir o cliente.");
        }
    }
}
