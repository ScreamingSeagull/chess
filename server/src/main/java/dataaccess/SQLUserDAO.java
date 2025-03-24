package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initUsers = """
                CREATE TABLE IF NOT EXISTS users (
                username varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                PRIMARY KEY (username)
            );
            """;
        executeUpdate(initUsers);
    }

    private int executeUpdate(String statement) throws DataAccessException {
        try (var con = DatabaseManager.getConnection()) {
            try (var update = con.prepareStatement(statement)) {
                update.executeUpdate();
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException("Not able to connect to database, SQL_User_DAO");
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clearU() throws DataAccessException {
        executeUpdate("TRUNCATE users;");
    }

    public RegisterResult createUser(UserData u, AuthData a) throws DataAccessException {
//        var statement = "INSERT INTO users (name, data) VALUES (?, ?)";
//        var id = executeUpdate(statement, u.username(), u);
//        return new RegisterResult(a.authToken(), u.username()); //Better to just add the auth data separately from the service???
        return null;
    }

    public UserData getUser(String username) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT username, json FROM users WHERE username=?";
//            return UserData;
//        }
        return null;
    }
}