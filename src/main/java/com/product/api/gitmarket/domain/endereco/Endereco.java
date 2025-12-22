package com.product.api.gitmarket.domain.endereco;

import com.product.api.gitmarket.domain.endereco.dto.EnderecoAtualizarRequestDTO;
import com.product.api.gitmarket.domain.endereco.dto.EnderecoRequestDTO;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    private String logradouro;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String uf;
    private String bairro;

    public Endereco(EnderecoRequestDTO endereco) {
        this(endereco.logradouro(),
                endereco.numero(),
                endereco.complemento(),
                endereco.cep(),
                endereco.cidade(),
                endereco.uf(),
                endereco.bairro());
    }

    public Endereco(EnderecoAtualizarRequestDTO dados) {
        this.atualizarInformacoes(dados);
    }

    public void atualizarInformacoes(EnderecoAtualizarRequestDTO dados) {
        if (dados.logradouro() != null) {
            this.logradouro = dados.logradouro();
        }
        if (dados.numero() != null) {
            this.numero = dados.numero();
        }
        if (dados.complemento() != null) {
            this.complemento = dados.complemento();
        }
        if (dados.cep() != null) {
            this.cep = dados.cep();
        }
        if (dados.cidade() != null) {
            this.cidade = dados.cidade();
        }
        if (dados.uf() != null) {
            this.uf = dados.uf();
        }
        if (dados.bairro() != null) {
            this.bairro = dados.bairro();
        }
    }

}
