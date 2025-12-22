package com.product.api.gitmarket.domain.cliente.dto;

import com.product.api.gitmarket.domain.cliente.Cliente;
import com.product.api.gitmarket.domain.endereco.Endereco;
import com.product.api.gitmarket.domain.endereco.dto.EnderecoRequestDTO;
import com.product.api.gitmarket.domain.usuario.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClienteComUsuarioRequestDTO(
        @NotBlank
        String cpf,

        @NotBlank
        String nome,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números (DDD + Número), entre 10 e 11 dígitos")
        String telefone,

        @Valid
        @NotNull
        EnderecoRequestDTO endereco,

        @NotBlank
        String login,
        @NotBlank
        String senha
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
