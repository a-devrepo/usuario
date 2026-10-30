package com.nca.usuario.business;

import com.nca.usuario.business.converter.EnderecoConverter;
import com.nca.usuario.business.converter.TelefoneConverter;
import com.nca.usuario.business.converter.UsuarioConverter;
import com.nca.usuario.business.dto.EnderecoDTO;
import com.nca.usuario.business.dto.TelefoneDTO;
import com.nca.usuario.business.dto.UsuarioDTO;
import com.nca.usuario.infrastructure.entities.Usuario;
import com.nca.usuario.infrastructure.exceptions.ConflictException;
import com.nca.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.nca.usuario.infrastructure.repositories.EnderecoRepository;
import com.nca.usuario.infrastructure.repositories.TelefoneRepository;
import com.nca.usuario.infrastructure.repositories.UsuarioRepository;
import com.nca.usuario.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioConverter usuarioConverter;
    private final EnderecoConverter enderecoConverter;
    private final TelefoneConverter telefoneConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter,
                          EnderecoConverter enderecoConverter, TelefoneConverter telefoneConverter,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, EnderecoRepository enderecoRepository,
                          TelefoneRepository telefoneRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.enderecoConverter = enderecoConverter;
        this.telefoneConverter = telefoneConverter;
        this.passwordEncoder = passwordEncoder;
        this.enderecoRepository = enderecoRepository;
        this.telefoneRepository = telefoneRepository;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {

        validarUsuario(usuarioDTO);
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        var usuario = paraEntidade(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return paraDTO(usuario);
    }

    public void deletarPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizar(String token, UsuarioDTO usuarioDTO) {
        var email = jwtUtil.extractUsername(token.substring(7));
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário nao encontrado com email: " + email));
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ?
                passwordEncoder.encode(usuarioDTO.getSenha()) : usuario.getSenha());
        var usuarioAtualizado = usuarioConverter.updateUsuario(usuarioDTO, usuario);
        usuarioAtualizado = usuarioRepository.save(usuarioAtualizado);
        return paraDTO(usuarioAtualizado);
    }

    public EnderecoDTO atualizarEndereco(String id, EnderecoDTO enderecoDTO) {
        var endereco = enderecoRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Endereco nao encontrado com id: " + id));
        var enderecoAtualizado = enderecoConverter.updateEndereco(enderecoDTO, endereco);
        enderecoAtualizado = enderecoRepository.save(enderecoAtualizado);
        return enderecoConverter.paraEnderecoDTO(enderecoAtualizado);
    }

    public TelefoneDTO atualizarTelefone(String id, TelefoneDTO telefoneDTO) {
        var telefone = telefoneRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Telefone nao encontrado com id: " + id));
        var telefoneAtualizado = telefoneConverter.updateTelefone(telefoneDTO, telefone);
        telefoneAtualizado = telefoneRepository.save(telefoneAtualizado);
        return telefoneConverter.paraTelefoneDTO(telefoneAtualizado);
    }

    public UsuarioDTO buscarPorEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário nao encontrado com email: " + email));
        return paraDTO(usuario);
    }

    private void validarUsuario(UsuarioDTO usuarioDTO) {
        if (emailJaCadastrado(usuarioDTO.getEmail())) {
            throw new ConflictException("Email ja cadastrado: " + usuarioDTO.getEmail());
        }
    }

    private boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    private Usuario paraEntidade(UsuarioDTO usuarioDTO) {
        var enderecos = enderecoConverter.paraListaEndereco(usuarioDTO.getEnderecos());
        var telefones = telefoneConverter.paraListaTelefone(usuarioDTO.getTelefones());
        var usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario.setEnderecos(enderecos);
        usuario.setTelefones(telefones);
        return usuario;
    }

    private UsuarioDTO paraDTO(Usuario usuario) {
        var enderecos = enderecoConverter.paraListaEnderecoDTO(usuario.getEnderecos());
        var telefones = telefoneConverter.paraListaTelefoneDTO(usuario.getTelefones());
        var usuarioDTO = usuarioConverter.paraUsuarioDTO(usuario);
        usuarioDTO.setEnderecos(enderecos);
        usuarioDTO.setTelefones(telefones);
        return usuarioDTO;
    }
}
