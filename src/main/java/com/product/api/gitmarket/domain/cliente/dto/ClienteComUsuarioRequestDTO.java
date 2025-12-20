package com.product.api.gitmarket.domain.cliente.dto;

import com.product.api.gitmarket.domain.endereco.dto.EnderecoRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CadastroClienteComUsuarioRequestDTO(
        @NotBlank
        String cpf,

        @NotBlank
        String nome,

        @Pattern(
                regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}",
                message = "O telefone deve estar no formato (XX) XXXXX-XXXX"
        )
        String telefone,

        @Valid
        @NotNull
        EnderecoRequestDTO endereco,

        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}
