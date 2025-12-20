package com.product.api.gitmarket.domain.endereco;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter apenas 8 números")
    private String cep;
    private String cidade;
    private String uf;
    private String bairro;

}
