package it.polimi.ingsw.model;

import java.util.List;

public class Player {

    private String nickname;
    public final School school;


    public Player(String nickname, List<Student> students, List<Tower> towers) {
        this.nickname = nickname;
        school = new School(this, students, towers);
    }

    /**
    * @return Player's nickname
    **/
    public String getNickname() {
        return this.nickname;
    }
}
