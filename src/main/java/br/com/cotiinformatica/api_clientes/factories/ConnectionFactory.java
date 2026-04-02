package br.com.cotiinformatica.api_clientes.factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    //Conexão com o banco de dados
    public static Connection getConnection() throws Exception {
        var host = "jdbc:postgresql://localhost:5432/db-api-clientes";
        var user = "postgres";
        var pass = "Flv@1997";

        return DriverManager.getConnection(host, user, pass);
    }
}
