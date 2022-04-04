package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Tower;
import it.polimi.ingsw.model.card.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    Player player;
    List<Student> students;
    List<Tower> towers;

    @BeforeEach
    public void initialization() {
        students = new ArrayList<>();
        for (Color c : Color.values()) {
            students.add(new Student(c));
        }
        towers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            towers.add(new Tower(TowerColor.GREY));
        }
        player = new Player(1, "player", students, towers, new Deck<>());
    }

    @Test
    void ownerSetting() {
        Tower tower;
        for (TowerColor c : TowerColor.values()) {
            tower = new Tower(c);
            tower.setOwner(player);
            assertEquals(player, tower.getOwner());

        }

    }

}