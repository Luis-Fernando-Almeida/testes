package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UsuarioMapperTest {
    UsuarioMapper usuarioMapper;
    UsuarioEntity usuarioEntity;
    EnderecoEntity enderecoEntity;
    UsuarioResponseDTO usuarioResponseDTO;
    EnderecoResponseDTO enderecoResponseDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setup() {
        usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
        dataHora = LocalDateTime.of(2024, 3, 25, 21, 32, 0);
        enderecoEntity = EnderecoEntity.builder().rua("Rua Teste").bairro("Bairro Teste")
                .cep("123456789").cidade("Cidade Teste").numero(1234L).complemento("Casa 1").build();
        usuarioEntity = UsuarioEntity.builder().id(123L).nome("Usuario").documento("123456")
                .email("usuario@email.com").dataCadastro(dataHora).endereco(enderecoEntity).build();
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua Teste", 1234L, "Bairro Teste", "Casa 1",
                "Cidade Teste", "123456789");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(123L, "Usuario", "usuario@email.com", "123456", enderecoResponseDTO);
    }

    @Test
    void deveConverterParaUsuaruiResponseDTO() {
        UsuarioResponseDTO dto = usuarioMapper.paraUsuarioResponseDTO(usuarioEntity);

        assertEquals(usuarioResponseDTO, dto);
    }
}
