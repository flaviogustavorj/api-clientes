package br.com.cotiinformatica.api_clientes.controllers;

import br.com.cotiinformatica.api_clientes.dtos.ClienteRequest;
import br.com.cotiinformatica.api_clientes.services.ClienteService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    //Endpoint para criar cliente
    @PostMapping("criar")
    public ResponseEntity<?> criar(@RequestBody ClienteRequest request) {
        try {
            var clienteService = new ClienteService();
            clienteService.cadastrarCliente(request);

            return ResponseEntity.status(701).body("Cliente: " + request.nome() + " cadastrado com sucesso!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Erro ao cadastrar cliente: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    //Endpoint para consultar cliente
    @GetMapping("consultar")
    public ResponseEntity<?> consultar(@RequestParam String nome) {
        try {
            var clienteService = new ClienteService();
            var lista = clienteService.pesquisarCliente(nome);

            return ResponseEntity.ok(lista);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Erro ao consultar cliente: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao consultar cliente: " + e.getMessage());
        }
    }
}
