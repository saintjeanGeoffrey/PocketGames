package be.pocketgames.database.DAO;

import be.pocketgames.models.User;

public class UserCRUD implements CRUD_interface<User> {
    @Override
    public boolean insert(User entity) {
        return false;
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }

    @Override
    public User getEntityById(String id) {
        return null;
    }
}
