package com.nca.usuario.business;

import com.nca.usuario.business.converter.EnderecoConverter;
import com.nca.usuario.business.converter.TelefoneConverter;
import com.nca.usuario.business.converter.UsuarioConverter;
import com.nca.usuario.business.dto.UsuarioDTO;
import com.nca.usuario.infrastructure.entities.Usuario;
import com.nca.usuario.infrastructure.exceptions.ConflictException;
import com.nca.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.nca.usuario.infrastructure.repositories.UsuarioRepository;
import com.nca.usuario.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final EnderecoConverter enderecoConverter;
    private final TelefoneConverter telefoneConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter,
                          EnderecoConverter enderecoConverter, TelefoneConverter telefoneConverter,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.enderecoConverter = enderecoConverter;
        this.telefoneConverter = telefoneConverter;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {

        validarUsuario(usuarioDTO);
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        var usuario = paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return paraUsuarioDTO(usuario);
    }

    public void deletarPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizar(String token, UsuarioDTO usuarioDTO) {
        var email = jwtUtil.extractUsername(token.substring(7));
        var usuario = this.buscarPorEmail(email);
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ?
                passwordEncoder.encode(usuarioDTO.getSenha()) : usuario.getSenha());
        var usuarioAtualizado = usuarioConverter.updateUsuario(usuarioDTO, usuario);
        usuarioAtualizado = usuarioRepository.save(usuarioAtualizado);
        return paraUsuarioDTO(usuarioAtualizado);
    }

    private void validarUsuario(UsuarioDTO usuarioDTO) {
        if (emailJaCadastrado(usuarioDTO.getEmail())) {
            throw new ConflictException("Email ja cadastrado: " + usuarioDTO.getEmail());
        }
    }

    private boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com email: " + email));
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
