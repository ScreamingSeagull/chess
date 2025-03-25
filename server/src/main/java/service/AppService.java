package service;
import dataaccess.*;

public class AppService {
    private UserDAO UDAO;
    private AuthDAO ADAO;
    private GameDAO GDAO;

    public AppService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        UDAO = udao;
        ADAO = adao;
        GDAO = gdao;
    }

    public void deleteall() throws DataAccessException {
        if (UDAO != null) {UDAO.clearU();}
        if (ADAO != null) {ADAO.clearA();}
        if (GDAO != null) {GDAO.clearG();}
    }
}
