package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class AppService {
    private UserDAO UDAO = new UserDAO();
    private AuthDAO ADAO = new AuthDAO();
    private GameDAO GDAO = new GameDAO();

    public AppService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        UDAO = udao;
        ADAO = adao;
        GDAO = gdao;
    }

    public void deleteall() {
        UDAO.clearU();
        ADAO.clearA();
        GDAO.clearG();
    }
}
