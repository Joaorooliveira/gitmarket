package com.product.api.gitmarket.domain.endereco;

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
}
