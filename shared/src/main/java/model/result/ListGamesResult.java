package model.result;
import model.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
    //To string method would be good for printing out
}
