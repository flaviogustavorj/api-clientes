package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    //Método para inserir um cliente no banco de dados
    public void inserir(Cliente cliente) throws Exception {

        try (var connection = ConnectionFactory.getConnection();
             var statement = connection.prepareStatement("INSERT INTO clientes (nome, cpf) VALUES (?, ?)")) {

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());

            statement.execute();
        }
    }
    //Método para verificar se o cpf já está cadastrado na tabela de clientes
    public static boolean cpfExistente(String cpf) throws Exception {

        String cpfLimpo = cpf.replaceAll("[^\\d]", "");

        try (var connection = ConnectionFactory.getConnection();
             var statement = connection.prepareStatement("SELECT COUNT(*) AS QTD FROM clientes WHERE cpf = ?")) {

            statement.setString(1, cpfLimpo);

            var result = statement.executeQuery();
            if(result.next()) {
                return result.getInt("QTD") > 0;
            } else {
                return false;
            }
        }
    }

    // Método para retornar uma lista de clientes do banco de dados através do nome informado
    public List<Cliente> listar(String nome) throws Exception {
        try (var connection = ConnectionFactory.getConnection();
             var statement = connection.prepareStatement("SELECT id, nome, cpf FROM clientes WHERE nome ILIKE ? ORDER BY nome ")) {

            statement.setString(1, "%" + nome + "%");

            var result = statement.executeQuery();

            var lista = new ArrayList<Cliente>();

            while (result.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("cpf"));
                lista.add(cliente);
            }

            return lista;
        }
    }
}
