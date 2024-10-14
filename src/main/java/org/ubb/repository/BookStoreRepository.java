package org.ubb.repository;

import java.util.List;
import java.util.Optional;

public interface BookStoreRepository <Entity>{
    List<Entity> findAll();
    Optional<Entity> findById(int id);
    Entity save(Entity entity);
    boolean deleteById(int id);
    boolean update(Entity entity);

}
