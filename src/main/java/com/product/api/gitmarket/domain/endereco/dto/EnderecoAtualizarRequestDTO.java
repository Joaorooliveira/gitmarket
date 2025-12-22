package com.product.api.gitmarket.domain.endereco.dto;

import jakarta.validation.constraints.Pattern;

public record EnderecoAtualizarRequestDTO(
        String logradouro,

        String numero,

        String complemento,


        @Pattern(regexp = "\\d{8}", message = "CEP deve conter apenas 8 números")
        String cep,


        String cidade,


        String uf,


        String bairro
) {
}
