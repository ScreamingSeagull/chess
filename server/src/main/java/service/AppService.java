package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class AppService {
    private UserDAO DAO = new UserDAO();
    private AuthDAO DAOA = new AuthDAO();
    private GameDAO DAOG = new GameDAO();
    public void deleteall() throws DataAccessException {
        DAO.clearU();
        DAOA.clearA();
        DAOG.clearG();
    }
}
