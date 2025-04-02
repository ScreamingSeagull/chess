package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public abstract class SqlDAO {
    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var update = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String s) {update.setString(i+1, s);}
                    if (param instanceof ChessGame g) {update.setString(i+1, new Gson().toJson(g));}
                    if (param instanceof Boolean b) {update.setBoolean(i+1, b);}
                    if (param instanceof Integer j) {update.setInt(i+1, j);}
                    if (param == null) {update.setNull(i+1, NULL);}
                }
                update.executeUpdate();
                var keys = update.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Not able to connect to database");
        }
    }
}
