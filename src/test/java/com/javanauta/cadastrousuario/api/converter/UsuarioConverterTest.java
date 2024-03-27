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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UsuarioConverterTest {

    @InjectMocks
    UsuarioConverter usuarioConverter;

    @Mock
    Clock clock;

    UsuarioEntity usuarioEntity;
    EnderecoEntity enderecoEntity;
    UsuarioRequestDTO usuarioRequestDTO;
    EnderecoRequestDTO enderecoRequestDTO;

    LocalDateTime dateTime;

    @BeforeEach
    public void setup() {

            dateTime = LocalDateTime.of(2024,3,25,21,32,0);

            enderecoEntity = EnderecoEntity.builder().rua("Rua Teste").bairro("Bairro Teste")
                    .cep("123456789").cidade("Cidade Teste").numero(1234L).complemento("Casa 1").build();


            usuarioEntity = UsuarioEntity.builder().nome("Usuario").documento("123478946")
                    .email("usuria@email.com").dataCadastro(dateTime).endereco(enderecoEntity).build();

            enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Teste", 1234L, "Bairro Teste",
                    "Casa 1", "Cidade Teste", "123456789");

            usuarioRequestDTO = UsuarioRequestDTOFixture.build("Usuario", "usuria@email.com", "123478946", enderecoRequestDTO);



        ZoneId zoneId = ZoneId.systemDefault();
        Clock fixedClock = Clock.fixed(dateTime.atZone(zoneId).toInstant(), zoneId);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

    }

    @Test
    void deveConverterParaUsuarioEntity(){

        UsuarioEntity entity = usuarioConverter.paraUsuarioEntity(usuarioRequestDTO);

        assertEquals(usuarioEntity, entity);



    }
}
