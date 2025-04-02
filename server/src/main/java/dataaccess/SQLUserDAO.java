package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO extends SqlDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initAuths = """
                CREATE TABLE IF NOT EXISTS users (
                username varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                PRIMARY KEY (username)
            );
            """;
        executeUpdate(initAuths);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clearU() throws DataAccessException {
        executeUpdate("TRUNCATE users;");
    }

    public RegisterResult createUser(UserData userData, AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
        return new RegisterResult(authData.authToken(), userData.username());
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?;";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            try (var ns = ps.executeQuery()) {
                if (ns.next()) {
                    return new UserData(ns.getString("username"), ns.getString("password"), ns.getString("email"));
                }
                return null;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}