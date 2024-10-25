package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryRepositoryImpl<ID,Entity extends BaseEntity<ID>> implements Repository<ID,Entity>{


    private Map<ID, Entity> entities;
    private Validator<Entity> validator;

    public InMemoryRepositoryImpl(Validator<Entity> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<Entity> findOne(ID id) {
       if(id == null){
           throw new IllegalArgumentException("id must not be null !");
       }
       return Optional.ofNullable(entities.get(id));

    }

    @Override
    public Iterable<Entity> findAll() {
        List<Entity> allEntities = entities.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        return allEntities;
    }

    @Override
    public Optional<Entity> save(Entity entity) throws ValidatorException {
        if(entity == null){
            throw new IllegalArgumentException("entity must not be null !");
        }
        validator.validate(entity);

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<Entity> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("id must not be null !");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<Entity> update(Entity entity) throws ValidatorException {
        if(entity == null){
            throw new IllegalArgumentException("entity must not be null !");
        }
        validator.validate(entity);
        if(entities.containsKey(entity.getId())){
            return Optional.ofNullable(entities.replace(entity.getId(), entity));
        }else {
            throw new ValidatorException("Entity not found !");
        }

    }
}
