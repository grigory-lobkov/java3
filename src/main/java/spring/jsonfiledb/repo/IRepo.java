package spring.jsonfiledb.repo;

import spring.jsonfiledb.pojo.IPojo;

import java.util.List;

//TaskRepository
public interface IRepo {

    void init(Object... params) throws Exception;

    void destroy();

    void save(IPojo task);

    void update(IPojo task);

    void delete(String id);

    <T> List<T> get();

    IPojo get(String id);

}
