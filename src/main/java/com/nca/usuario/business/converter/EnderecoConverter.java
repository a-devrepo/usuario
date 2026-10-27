package com.nca.usuario.business.converter;

import com.nca.usuario.business.dto.EnderecoDTO;
import com.nca.usuario.infrastructure.entities.Endereco;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnderecoConverter {

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecosDTOs) {
        return enderecosDTOs.stream()
                .map(this::paraEndereco)
                .toList();
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::paraEnderecoDTO)
                .toList();
    }
}
