package org.example.web.dto;

import org.example.domain.Veiculo;

import java.util.List;

public record VeiculoResponse(String modelo,
                              String fabricante,
                              Integer ano,
                              Double preco) {


    public static List<VeiculoResponse> fromEntity (List<Veiculo> veiculos){
        return veiculos.stream()
                .map(VeiculoResponse::toView)
                .toList();
    }

    public static  VeiculoResponse toView(Veiculo veiculo){
        return new VeiculoResponse(veiculo.getModelo(), veiculo.getFabricante(), veiculo.getAno(), veiculo.getPreco());
    }
}
