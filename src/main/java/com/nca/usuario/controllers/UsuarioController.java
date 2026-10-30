package com.nca.usuario.controllers;

import com.nca.usuario.business.UsuarioService;
import com.nca.usuario.business.dto.EnderecoDTO;
import com.nca.usuario.business.dto.TelefoneDTO;
import com.nca.usuario.business.dto.UsuarioDTO;
import com.nca.usuario.infrastructure.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager
            , JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvar(@RequestBody UsuarioDTO usuarioDTO) {
        var usuarioSalvo = usuarioService.salvar(usuarioDTO);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()
                )
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarPorEmail(@PathVariable String email) {
        usuarioService.deletarPorEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizar(@RequestBody UsuarioDTO usuarioDTO,
                                                @RequestHeader("Authorization") String token) {
        var usuarioAtualizado = usuarioService.atualizar(token, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                         @RequestParam("id") String id) {
        var enderecoAtualizado = usuarioService.atualizarEndereco(id, enderecoDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                         @RequestParam("id") String id) {
        var telefoneAtualizado = usuarioService.atualizarTelefone(id, telefoneDTO);
        return ResponseEntity.ok(telefoneAtualizado);
    }
}