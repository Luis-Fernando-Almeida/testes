package com.javanauta.cadastrousuario.api.response;

import java.nio.channels.Pipe;

public class UsuarioResponseDTOFixture{
    public static UsuarioResponseDTO build(Long id,

                                           String nome,

                                           String email,

                                           String documento,

                                           EnderecoResponseDTO endereco){
        return new UsuarioResponseDTO(id, nome, email, documento, endereco);
    }
}
