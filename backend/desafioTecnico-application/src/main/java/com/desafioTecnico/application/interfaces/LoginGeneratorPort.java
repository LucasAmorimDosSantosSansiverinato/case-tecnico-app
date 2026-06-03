package com.desafioTecnico.application.interfaces;

import com.desafioTecnico.domain.vo.Login;

import java.util.List;


public interface LoginGeneratorPort {
    Login generate(String fullName, List<String> existingLogins);
}
