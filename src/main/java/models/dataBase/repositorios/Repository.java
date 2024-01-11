package models.dataBase.repositorios;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();
    T findById(Long id);
    void save(T t);
    void update(T t);
    void delete(T t);

}
