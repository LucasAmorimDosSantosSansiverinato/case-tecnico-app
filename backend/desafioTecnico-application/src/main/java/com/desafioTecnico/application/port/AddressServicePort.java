package com.desafioTecnico.application.port;

import com.desafioTecnico.domain.vo.Address;

public interface AddressServicePort {
    Address findByCep(String cep);
}
