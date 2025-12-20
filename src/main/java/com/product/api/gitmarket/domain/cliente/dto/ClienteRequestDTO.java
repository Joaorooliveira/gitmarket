package com.product.api.gitmarket.domain.cliente.dto;

import com.product.api.gitmarket.domain.cliente.Cliente;
import com.product.api.gitmarket.domain.endereco.Endereco;
import com.product.api.gitmarket.domain.endereco.dto.EnderecoRequestDTO;
import com.product.api.gitmarket.domain.usuario.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClienteRequestDTO(

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

        @NotNull
        Long usuario_id


) {

    public Cliente toEntity(Usuario usuario) {
        Cliente cliente = new Cliente();
        cliente.setCpf(this.cpf);
        cliente.setNome(this.nome);
        cliente.setTelefone(this.telefone);
        cliente.setEndereco(new Endereco(this.endereco));
        cliente.setUsuario(usuario);
        return cliente;
    }
}
