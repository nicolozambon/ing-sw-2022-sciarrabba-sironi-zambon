package it.polimi.ingsw.model;

public class Player {
    private String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
    * @return Player's nickname
    **/
    public String getNickname() {
        return this.nickname;
    }
}
