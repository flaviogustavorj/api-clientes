package br.com.cotiinformatica.api_clientes.repositories;

import br.com.cotiinformatica.api_clientes.entities.Cliente;
import br.com.cotiinformatica.api_clientes.factories.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    /*
        Método para inserir um cliente no banco de dados
     */
    public void inserir(Cliente cliente) throws Exception {

        try (var connection = connectionFactory.getConnection()) {

            connection.setAutoCommit(false); //iniciar uma transação

            //Inserindo o cliente no banco de dados e capturando o ID gerado na tabela (AUTO_INCREMENT)
            var statement = connection.prepareStatement("INSERT INTO CLIENTES(NOME, CPF) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getCpf());
            statement.execute();

            //Capturando o Id do cliente gerado no banco de dados
            var generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }

            //verificar se o cliente possui endereços
            if(cliente.getEnderecos() != null) {
                //percorrer cada endereço do cliente
                for(var endereco : cliente.getEnderecos()) {

                    try (var enderecoStatement = connection.prepareStatement(
                            "INSERT INTO enderecos (cliente_id, logradouro, numero, complemento, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                        enderecoStatement.setInt(1, cliente.getId());
                        enderecoStatement.setString(2, endereco.getLogradouro());
                        enderecoStatement.setString(3, endereco.getNumero());
                        enderecoStatement.setString(4, endereco.getComplemento());
                        enderecoStatement.setString(5, endereco.getBairro());
                        enderecoStatement.setString(6, endereco.getCidade());
                        enderecoStatement.setString(7, endereco.getEstado());
                        enderecoStatement.setString(8, endereco.getCep());

                        enderecoStatement.execute();
                    }
                }
            }

            connection.commit(); //confirmando a transação
        }
    }

    /*
        Método para verificar se um CPF já está cadastrado na tabela de clientes
     */
    public boolean cpfExistente(String cpf) throws Exception {
        try (var connection = connectionFactory.getConnection()) {
            var statement = connection.prepareStatement("SELECT COUNT(*) AS QTD FROM CLIENTES WHERE CPF = ?");
            statement.setString(1, cpf);
            var result = statement.executeQuery();
            if(result.next()) {
                return result.getInt("QTD") == 1;
            }
            return false;
        }
    }

    /*
        Método para retornar uma lista de clientes do banco de dados
        através do nome informado.
     */
    public List<Cliente> listar(String nome) throws Exception {
        try (var connection = connectionFactory.getConnection()) {
            var statement = connection.prepareStatement("SELECT * FROM CLIENTES WHERE NOME ILIKE ? ORDER BY NOME");
            statement.setString(1, "%" + nome + "%");
            var result = statement.executeQuery();

            var lista = new ArrayList<Cliente>(); //criando uma lista de clientes

            while(result.next()) { //Percorrendo cada registro obtido na consulta
                var cliente = new Cliente(); //criando um objeto cliente
                cliente.setId(result.getInt("id"));
                cliente.setNome(result.getString("nome"));
                cliente.setCpf(result.getString("cpf"));

                lista.add(cliente); //adicionar o cliente dentro da lista
            }

            return lista;
        }
    }


}
