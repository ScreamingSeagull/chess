package dataaccess;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SQLUserDAO implements UserDAO {
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

    public void clearU() {
        executeUpdate("TRUNCATE users;");
    }

    public RegisterResult createUser(UserData u, AuthData a) throws DataAccessException {

    }

    public UserData getUser(String username) throws DataAccessException {

    }
}