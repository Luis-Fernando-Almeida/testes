package com.javanauta.cadastrousuario.business;

import com.javanauta.cadastrousuario.api.converter.UsuarioConverter;
import com.javanauta.cadastrousuario.api.converter.UsuarioMapper;
import com.javanauta.cadastrousuario.api.converter.UsuarioUpdateMapper;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import com.javanauta.cadastrousuario.infrastructure.exceptions.BusinessException;
import com.javanauta.cadastrousuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioUpdateMapper usuarioUpdateMapper;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private UsuarioConverter usuarioConverter;

    UsuarioEntity usuarioEntity;
    EnderecoEntity enderecoEntity;
    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    EnderecoResponseDTO enderecoResponseDTO;
    LocalDateTime dateTime;
    String email;

    @BeforeEach
    public void setup() {

        dateTime = LocalDateTime.of(2024, 3, 25, 21, 32, 0);

        enderecoEntity = EnderecoEntity.builder().rua("Rua Teste").bairro("Bairro Teste")
                .cep("123456789").cidade("Cidade Teste").numero(1234L).complemento("Casa 1").build();


        usuarioEntity = UsuarioEntity.builder().nome("Usuario").documento("123478946")
                .email("usuria@email.com").dataCadastro(dateTime).endereco(enderecoEntity).build();

        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Teste", 1234L, "Bairro Teste",
                "Casa 1", "Cidade Teste", "123456789");

        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "usuria@email.com", "123478946", enderecoRequestDTO);
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua Teste", 1234L, "Bairro Teste",
                "Casa 1", "Cidade Teste", "123456789");

        usuarioResponseDTO = UsuarioResponseDTOFixture.build(132L, "Usuario", "usuria@email.com", "123478946", enderecoResponseDTO);
        email = "usuria@email.com";

    }

    @Test
    void deveSalvarComSucesso() {

        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);

        UsuarioEntity entity = this.usuarioService.salvaUsuario(usuarioEntity);

        assertEquals(entity, usuarioEntity);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository);

    }

    @Test
    void deveGravarUsuariosComSucesso() {

        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.gravarUsuarios(usuarioRequestDTO);

        assertEquals(dto, usuarioResponseDTO);
        verify(usuarioConverter).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, usuarioMapper);

    }

    @Test
    void naoDeveSalvarUsuariosCasoUsuarioRequestDTONull() {

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(null));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoMoreInteractions(usuarioMapper, usuarioConverter, usuarioRepository);

    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoGravarUsuario() {

        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Falha ao gravar dados de usuário"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao gravar dados de usuário"));
        verify(usuarioConverter).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioConverter, usuarioRepository);

    }

    @Test
    void deveAtualizarCadastroDeUsuariosComSucesso() {

        when(usuarioRepository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO,usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.atualizaCadastro( usuarioRequestDTO);

        assertEquals(dto, usuarioResponseDTO);
        verify(usuarioRepository).findByEmail(email);
        verify(usuarioUpdateMapper).updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioUpdateMapper, usuarioMapper);

    }

    @Test
    void naoDeveAtualizarDadosDeUsuarioCasoUsuarioRequestDTONull() {

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(null));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause(), notNullValue());
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoMoreInteractions(usuarioMapper, usuarioUpdateMapper, usuarioRepository);



    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarUsuario() {
        when(usuarioRepository.findByEmail(email)).thenThrow(new RuntimeException("Falha ao buscar dados de usuário"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao buscar dados de usuário"));
        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(usuarioUpdateMapper, usuarioMapper);
        verifyNoMoreInteractions(usuarioRepository);

    }

    @Test
    void deveBuscarDadosDeUsuarioComSucesso() {
        when(usuarioRepository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);

        verify(usuarioRepository).findByEmail(email);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        assertEquals(dto,usuarioResponseDTO);

    }

    @Test
    void deveRetornarNullCasoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);

        assertEquals(dto, null);
        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(usuarioMapper);

    }

    @Test
    void deveDeletarDadosDeUsuarioComSucesso() {
        doNothing().when(usuarioRepository).deleteByEmail(email);

        usuarioService.deletaDadosUsuario(email);

        verify(usuarioRepository).deleteByEmail(email);
    }

}


