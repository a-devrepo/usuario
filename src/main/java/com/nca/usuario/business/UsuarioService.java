package com.nca.usuario.business;

import com.nca.usuario.business.converter.EnderecoConverter;
import com.nca.usuario.business.converter.TelefoneConverter;
import com.nca.usuario.business.converter.UsuarioConverter;
import com.nca.usuario.business.dto.UsuarioDTO;
import com.nca.usuario.infrastructure.entities.Usuario;
import com.nca.usuario.infrastructure.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final EnderecoConverter enderecoConverter;
    private final TelefoneConverter telefoneConverter;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter,
                          EnderecoConverter enderecoConverter, TelefoneConverter telefoneConverter) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.enderecoConverter = enderecoConverter;
        this.telefoneConverter = telefoneConverter;
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {

        var usuario = paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return paraUsuarioDTO(usuario);
    }


    private Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        var enderecos = enderecoConverter.paraListaEndereco(usuarioDTO.getEnderecos());
        var telefones = telefoneConverter.paraListaTelefone(usuarioDTO.getTelefones());
        var usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario.setEnderecos(enderecos);
        usuario.setTelefones(telefones);
        return usuario;
    }

    private UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        var enderecos = enderecoConverter.paraListaEnderecoDTO(usuario.getEnderecos());
        var telefones = telefoneConverter.paraListaTelefoneDTO(usuario.getTelefones());
        var usuarioDTO = usuarioConverter.paraUsuarioDTO(usuario);
        usuarioDTO.setEnderecos(enderecos);
        usuarioDTO.setTelefones(telefones);
        return usuarioDTO;
    }
}
