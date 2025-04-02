package dataaccess;
import model.AuthData;
import java.sql.PreparedStatement;

public class SQLAuthDAO extends SqlDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initAuths = """
                CREATE TABLE IF NOT EXISTS auths (
                username varchar(256) NOT NULL,
                authToken varchar(256) NOT NULL,
                PRIMARY KEY (authToken)
            );
            """;
        executeUpdate(initAuths);
    }

    public void clearA() throws DataAccessException {
        executeUpdate("TRUNCATE auths;");
    }
    public void createAuth(AuthData data) throws DataAccessException {
        var statement = "INSERT INTO auths (username, authToken) VALUES (?, ?);";
        executeUpdate(statement, data.username(), data.authToken());
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, authToken FROM auths WHERE authToken=?;";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var newstate = ps.executeQuery()) {
                    if (newstate.next()) {
                        return new AuthData(newstate.getString("authToken"), newstate.getString("username"));
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void deleteAuth(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?;";
        executeUpdate(statement, authData.authToken());
    }
}
