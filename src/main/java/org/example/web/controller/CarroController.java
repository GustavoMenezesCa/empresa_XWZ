package org.example.web.controller;

import org.example.domain.Carro;
import org.example.service.CarroService;
import org.example.web.dto.CarroCadastroForm;
import org.example.web.dto.CarroUpdateForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {

    private final CarroService carroService;



    public CarroController(CarroService carroService){
        this.carroService = carroService;
    }


    @PostMapping("/cadastro")
    public ResponseEntity<Object> cadastraCarro(@RequestBody CarroCadastroForm carroCadastroForm){

        Carro carro = carroService.cadastraCarro(carroCadastroForm);

        return ResponseEntity.status(HttpStatus.OK).body(carro);
    }

    @GetMapping("/consultar")
    public ResponseEntity<List<Carro>> listarCarro(@RequestBody CarroUpdateForm carroUpdateForm) throws SQLException {

        List<Carro> carro = carroService.listaCarroFiltrados(carroUpdateForm.modelo(), carroUpdateForm.cor(), carroUpdateForm.ano());
        return ResponseEntity.status(HttpStatus.OK).body(carro);
    }

}


