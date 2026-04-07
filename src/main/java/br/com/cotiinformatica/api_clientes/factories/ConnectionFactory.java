package br.com.cotiinformatica.api_clientes.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ConnectionFactory {

    //Pegando valores do arquivo application.properties e atribuindo a variaveis privadas.
    @Value("${datasource.host}")
    private String host;
    @Value("${datasource.user}")
    private String user;
    @Value("${datasource.pass}")
    private String pass;

    //Conexão com o banco de dados
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(host, user, pass);
    }
}
