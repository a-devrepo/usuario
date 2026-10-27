package com.nca.usuario.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rua", nullable = false, length = 100)
    private String rua;
    @Column(name = "numero", nullable = false, length = 10)
    private String numero;
    @Column(name = "complemento", nullable = false, length = 100)
    private String complemento;
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;
}
