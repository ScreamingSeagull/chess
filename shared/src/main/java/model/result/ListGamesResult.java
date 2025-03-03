package model.result;
import model.GameData;

public record ListGamesResult(GameData[] games) {
    //To string method would be good for printing out
}
