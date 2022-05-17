package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.AssistantCardException;
import it.polimi.ingsw.exceptions.MotherNatureStepsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {

    Model model;
    Handler handler;

    @BeforeEach
    private void initialization() {
        ModelBuilder modelBuilder = new ModelBuilder();
        List<String> playersNames = new ArrayList<>();
        playersNames.add("player0");
        playersNames.add("player1");
        playersNames.add("player2");
        model = modelBuilder.buildModel(playersNames);
        handler = new HandlerFactory().buildHandler(model.getPlayers());
    }

    @Test
    void professorControl() {
        //initialization();
        int num = 9;
        if (model.getPlayers().size() == 2) num = 7;

        for (int i = 0; i < num; i++) {
            for (Player player : model.getPlayers()) {
                Student student = player.getSchool().getEntrance().getPawns().get(0);
                //System.out.println("Player : " + player.getId() + " moved student " + student.getColor());
                model.moveStudentToDiningRoom(player.getId(), 0);

                if (professorControlHelper(player, student.getColor())) {
                    //System.out.println("Checking professors presence of player : " + player.getId());
                    Professor prof = null;
                    for (Professor professor : player.getSchool().getProfessorsTable().getPawns()) {
                        if (professor.getColor() == student.getColor()) {
                            prof = professor;
                            //System.out.println("Professor found!" + "\n");
                        }

                    }
                    assertNotNull(prof, "Prof not found!");
                }

            }
        }


    }

    private boolean professorControlHelper(Player currentPlayer, Color color) {
        Integer[] array = new Integer[model.getPlayers().size()];
        for (Player player : model.getPlayers()) {
            array[player.getId()] = player.getSchool().getDiningRoomByColor(color).getNumPawns();
        }
        //System.out.println(Arrays.toString(array));

        boolean check = false;
        int max = 0;
        int maxPos = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxPos = i;
                check = true;
            }
        }

        for (int i = 0; i < array.length; i++) {
            if (i != maxPos && array[i] == max) {
                return false;
            }
        }
        if (check && currentPlayer.getId() == maxPos) return true;

        return false;
    }

    @Test
    void checkTowerPositioning() throws MotherNatureStepsException, AssistantCardException {

        initialization();
        int num = 4;
        if (model.getPlayers().size() == 2) num = 3;

        for (int i = 0; i < num; i++) {
            for (Player player : model.getPlayers()) {
                //Student student = player.getSchool().getEntrance().getPawns().get(0);
                model.moveStudentToDiningRoom(player.getId(), 0);
            }
        }

        for (Player player : model.getPlayers()) {
            List<Student> students = player.getSchool().getEntrance().getPawns();
            for (Student student : students) {
                int islandId = new Random().nextInt(model.getIslands().size());
                model.moveStudentToIsland(player.getId(), player.getSchool().getEntrance().getPawns().indexOf(student), islandId);
            }
        }

        List<Island> islands = model.getIslands();
        MotherNature mn = new MotherNature(islands.get(0));
        Player randomPlayer = model.getPlayers().get(new Random().nextInt(model.getPlayers().size()));
        randomPlayer.playAssistantCard(1);
        int val = randomPlayer.getSchool().getTowersBoard().getNumPawns();
        for (int i = 1; i < val; i++) {
            handler.motherNatureMovement(randomPlayer, mn,  1);
            Player most = mostInfluentialPlayer(islands.get(i%12));
            if (most != null) {
                //System.out.println("Found most influential, checking tower presence...");
                assertEquals(most, islands.get(i%12).getTower().getOwner());
            }
        }

    }

    private Player mostInfluentialPlayer(Island island) {
        List<Player> players = model.getPlayers().stream().sorted(Comparator.comparingInt(x -> influenceOnIsland(x, island))).toList();
        if (influenceOnIsland(players.get(players.size()-1), island) > influenceOnIsland(players.get(players.size()-2), island)) {
            return players.get(players.size()-1);
        }
        return null;
    }

    private int influenceOnIsland(Player player, Island island) {
        int influence = 0;
        for (Professor prof : player.getSchool().getProfessorsTable().getPawns()) {
            influence += island.countStudentsByColor(prof.getColor());
        }
        if (island.getTower() != null && island.getTower().getOwner().equals(player)) influence++;
        return influence;
    }

    @Test
    void unifyIslandAndTowerSwitch() {
        //initialization();
        List<Island> islands = model.getIslands();

        islands.get(4).setTower(model.getPlayers().get(0));
        islands.get(6).setTower(model.getPlayers().get(0));
        for (Island island : islands) {
            assertFalse(island.isUnifyNext());
            assertFalse(island.isUnifyPrev());
        }
        islands.get(5).setTower(model.getPlayers().get(0));

        handler.unifyIsland(islands.get(new Random().nextInt(4,7)));

        assertFalse(islands.get(4).isUnifyPrev());
        assertTrue(islands.get(4).isUnifyNext());
        assertTrue(islands.get(5).isUnifyPrev());
        assertTrue(islands.get(5).isUnifyNext());
        assertTrue(islands.get(6).isUnifyPrev());
        assertFalse(islands.get(6).isUnifyNext());

        islands.get(0).setTower(model.getPlayers().get(1));
        islands.get(1).setTower(model.getPlayers().get(1));
        islands.get(11).setTower(model.getPlayers().get(1));

        handler.unifyIsland(islands.get(11));

        assertFalse(islands.get(11).isUnifyPrev());
        assertTrue(islands.get(11).isUnifyNext());
        assertTrue(islands.get(0).isUnifyPrev());
        assertTrue(islands.get(0).isUnifyNext());
        assertTrue(islands.get(1).isUnifyPrev());
        assertFalse(islands.get(1).isUnifyNext());

        int num = new Random().nextInt(model.getPlayers().size());
        handler.switchTowers(islands.get(0), model.getPlayers().get(num));
        assertFalse(islands.get(11).isUnifyPrev());
        assertTrue(islands.get(11).isUnifyNext());
        assertTrue(islands.get(0).isUnifyPrev());
        assertTrue(islands.get(0).isUnifyNext());
        assertTrue(islands.get(1).isUnifyPrev());
        assertFalse(islands.get(1).isUnifyNext());
        assertEquals(islands.get(11).getTower().getOwner(), model.getPlayers().get(num));
        assertEquals(islands.get(0).getTower().getOwner(), model.getPlayers().get(num));
        assertEquals(islands.get(1).getTower().getOwner(), model.getPlayers().get(num));

    }
}