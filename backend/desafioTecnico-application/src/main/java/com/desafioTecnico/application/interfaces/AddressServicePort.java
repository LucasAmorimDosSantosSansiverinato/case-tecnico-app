package com.desafioTecnico.application.interfaces;

import com.desafioTecnico.domain.vo.Address;

public interface AddressServicePort {
    Address findByCep(String cep);
}
