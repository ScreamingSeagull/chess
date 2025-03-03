package dataaccess;
import model.UserData;

public class UserDAO {
    void clear() throws DataAccessException {
        //returns 200 if all good
        //returns 500 if description
    }
    void createUser(UserData u) throws DataAccessException {

    }
    String getUser(String username) throws DataAccessException {
        return username; //returns authtoken? check network map
    }
}