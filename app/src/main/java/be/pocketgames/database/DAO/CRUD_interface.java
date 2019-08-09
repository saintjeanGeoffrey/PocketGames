package be.pocketgames.database.DAO;

public interface CRUD_interface <T> {

    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(T entity);

//    T cursorToEntity(Cursor cursor);
    T getEntityById(String id);

}
