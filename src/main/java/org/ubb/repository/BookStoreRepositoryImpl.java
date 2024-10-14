package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookStoreRepositoryImpl<Entity extends BaseEntity> implements BookStoreRepository<Entity>{

    private List<Entity> entityList;


    // the constructor initializing the list
    public BookStoreRepositoryImpl() {
        entityList = new ArrayList<>();
    }

    @Override
    public List<Entity> findAll() {
        return entityList;
    }

    @Override
    public Optional<Entity> findById(int id) {
        return entityList.stream()
                .filter(entity -> entity.getId() == id)
                .findFirst();
    }

    @Override
    public Entity save(Entity entity) {
        entityList.add(entity);
        return entity;
    }

    @Override
    public boolean deleteById(int id) {
        entityList.removeIf(entity -> entity.getId() == id);
        return false;
    }

    @Override
    public boolean update (Entity entity) {
        int index = entityList.indexOf(entity);
        if (index == -1){
            return false;
        }
        entityList.set(index, entity);
        return true;
    }

}
