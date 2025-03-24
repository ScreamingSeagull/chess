package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
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

    public void clearA() throws DataAccessException {
        executeUpdate("TRUNCATE authData;");
    }
    public void createAuth(AuthData data) throws DataAccessException {
        //auths.put(data.authToken(), data);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        //return auths.get(authToken);
        return null;
    }
    public void deleteAuth(AuthData authData) throws DataAccessException {
//        if (auths.get(authData.authToken()) != null) {
//            auths.remove(authData.authToken());
//        }
    }
}
