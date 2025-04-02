package service;
import dataaccess.*;

public class AppService {
    private UserDAO udao;
    private AuthDAO adao;
    private GameDAO gdao;

    public AppService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        this.udao = udao;
        this.adao = adao;
        this.gdao = gdao;
    }

    public void deleteall() throws DataAccessException {
        if (udao != null) {
            udao.clearU();}
        if (adao != null) {
            adao.clearA();}
        if (gdao != null) {
            gdao.clearG();}
    }
}
