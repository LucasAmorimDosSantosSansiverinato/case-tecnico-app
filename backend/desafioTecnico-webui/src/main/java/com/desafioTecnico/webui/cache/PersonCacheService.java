package com.desafioTecnico.webui.cache;

import com.desafioTecnico.application.command.RegisterPersonCommand;
import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.handler.GetAllPersonsQueryHandler;
import com.desafioTecnico.application.handler.GetPersonByIdQueryHandler;
import com.desafioTecnico.application.handler.RegisterPersonCommandHandler;
import com.desafioTecnico.application.query.GetAllPersonsQuery;
import com.desafioTecnico.application.query.GetPersonByIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonCacheService {

    private static final Logger log = LoggerFactory.getLogger(PersonCacheService.class);

    private final RegisterPersonCommandHandler registerHandler;
    private final GetPersonByIdQueryHandler getByIdHandler;
    private final GetAllPersonsQueryHandler getAllHandler;

    public PersonCacheService(
            RegisterPersonCommandHandler registerHandler,
            GetPersonByIdQueryHandler getByIdHandler,
            GetAllPersonsQueryHandler getAllHandler
    ) {
        this.registerHandler = registerHandler;
        this.getByIdHandler = getByIdHandler;
        this.getAllHandler = getAllHandler;
    }

    @Caching(evict = {
        @CacheEvict(value = "persons-all", allEntries = true),
        @CacheEvict(value = "persons-by-id", allEntries = true)
    })
    public PersonResponse register(RegisterPersonCommand command) {
        log.info("[CACHE] Evicting persons cache after new registration");
        return registerHandler.handle(command);
    }

    @Cacheable(value = "persons-by-id", key = "#query.id()")
    public PersonResponse getById(GetPersonByIdQuery query) {
        log.info("[CACHE] Cache miss for person id={}", query.id());
        return getByIdHandler.handle(query);
    }

    @Cacheable(value = "persons-all")
    public List<PersonResponse> getAll(GetAllPersonsQuery query) {
        log.info("[CACHE] Cache miss for persons list — querying database");
        return getAllHandler.handle(query);
    }
}
