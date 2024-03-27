package com.javanauta.cadastrousuario.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.business.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    UsuarioController controller;

    @Mock
    UsuarioService usuarioService;

    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;

    private UsuarioRequestDTO usuarioRequestDTO;

    private EnderecoRequestDTO enderecoRequestDTO;

    private UsuarioResponseDTO usuarioResponseDTO;

    private EnderecoResponseDTO enderecoResponseDTO;

    private String json;

    @BeforeEach
    void setup() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();
        url = "/user";

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Teste", 1234L, "Bairro Teste",
                "Casa 1", "Cidade Teste", "123456789");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "usuria@email.com", "123478946", enderecoRequestDTO);
        json = objectMapper.writeValueAsString(usuarioRequestDTO);
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua Teste", 1234L, "Bairro Teste",
                "Casa 1", "Cidade Teste", "123456789");

        usuarioResponseDTO = UsuarioResponseDTOFixture.build(132L, "Usuario", "usuria@email.com", "123478946", enderecoResponseDTO);


    }

    @Test
    void deveGravarDadosDeUsuarioComSucesso() throws Exception {

        when(usuarioService.gravarUsuarios(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(usuarioService).gravarUsuarios(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);

    }

    @Test
    void naoDeveGravarDadosDeUsuarioCasoJsonNull() throws Exception {


        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);

    }

    @Test
    void deveAtualizarDadosDeUsuarioComSucesso() throws Exception {

        when(usuarioService.atualizaCadastro(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(usuarioService).atualizaCadastro(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);

    }

    @Test
    void naoDeveAtualizarrDadosDeUsuarioCasoJsonNull() throws Exception {

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);

    }

    @Test
    void deveBuscarDadosDeUsuarioComSucesso() throws Exception {
        when(usuarioService.buscaDadosUsuario("usario@email.com")).thenReturn(usuarioResponseDTO);
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "usario@email.com")
        ).andExpect(status().isOk());
        verify(usuarioService).buscaDadosUsuario("usario@email.com");
        verifyNoMoreInteractions(usuarioService);

    }

    @Test
    void naoDeveBuscarDadosDeUsuarioCasoParametroNull() throws Exception {

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);

    }

    @Test
    void deveDeletarDadosDeUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).deletaDadosUsuario("usario@email.com");
        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "usario@email.com")
        ).andExpect(status().isAccepted());
        verify(usuarioService).deletaDadosUsuario("usario@email.com");
        verifyNoMoreInteractions(usuarioService);

    }
    @Test
    void naoDeveDeletarrDadosDeUsuarioCasoParametroNull() throws Exception {

        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(usuarioService);

    }


}
