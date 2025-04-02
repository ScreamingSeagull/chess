package service;
import dataaccess.*;

public class AuthService {
    private UserDAO udao;
    private AuthDAO adao;
    private GameDAO gdao;

    public AuthService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        this.udao = udao;
        this.adao = adao;
        this.gdao = gdao;
    }
}
