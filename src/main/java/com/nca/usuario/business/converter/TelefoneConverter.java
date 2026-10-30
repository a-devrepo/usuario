package com.nca.usuario.business.converter;

import com.nca.usuario.business.dto.TelefoneDTO;
import com.nca.usuario.infrastructure.entities.Telefone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelefoneConverter {

    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefonesDTOs) {
        return telefonesDTOs.stream()
                .map(this::paraTelefone)
                .toList();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefones) {
        return telefones.stream()
                .map(this::paraTelefoneDTO)
                .toList();
    }

    public Telefone updateTelefone(TelefoneDTO telefoneDTO, Telefone telefone) {
        return Telefone.builder()
                .id(telefone.getId())
                .numero(telefoneDTO.getNumero() != null ? telefoneDTO.getNumero() : telefone.getNumero())
                .ddd(telefoneDTO.getDdd() != null ? telefoneDTO.getDdd() : telefone.getDdd())
                .build();
    }
}
