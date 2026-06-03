package com.desafioTecnico.application.port;

import com.desafioTecnico.domain.vo.Login;

import java.util.List;

public interface LoginGeneratorPort {
    Login generate(String fullName, List<String> existingLogins);
}
