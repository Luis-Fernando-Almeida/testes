package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
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
public class UsuarioUpdateMapperTest {
    UsuarioUpdateMapper usuarioMapper;

    UsuarioEntity usuarioEntityEsperado;
    UsuarioEntity usuarioEntity;
    EnderecoEntity enderecoEntity;
    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;

    LocalDateTime dataHora;

    @BeforeEach
    public void setup() {
        usuarioMapper = Mappers.getMapper(UsuarioUpdateMapper.class);
        dataHora = LocalDateTime.of(2024, 3, 25, 21, 32, 0);
        enderecoEntity = EnderecoEntity.builder().rua("Rua Teste").bairro("Bairro Teste")
                .cep("123456789").cidade("Cidade Teste").numero(1234L).complemento("Casa 1").build();
        usuarioEntity = UsuarioEntity.builder().id(123L).nome("Usuario").documento("123456")
                .email("usuario@email.com").dataCadastro(dataHora).endereco(enderecoEntity).build();
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Teste", 1234L, "Bairro Teste", "Casa 1",
                "Cidade Teste", "123456789");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario Teste", null, "1234567", enderecoRequestDTO);

        usuarioEntityEsperado = usuarioEntity.builder().id(123L).nome("Usuario Teste").email("usuario@email.com")
                .documento("1234567").dataCadastro(dataHora).endereco(enderecoEntity).build();
    }

    @Test
    void deveConverterParaUsuaruiResponseDTO() {
        UsuarioEntity entity = usuarioMapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity);
        assertEquals( usuarioEntityEsperado, entity);
    }
}




