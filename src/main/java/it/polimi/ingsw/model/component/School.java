package it.polimi.ingsw.model.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {

    private Player owner;

    private final Board<Student> entrance;
    private Map<Color, Board<Student>> diningRoom;
    private final Board<Professor> professorsTable;
    private Board<Tower> towersBoard;

    public School(Player owner, List<Student> students, List<Tower> towers) {
        this.owner = owner;

        this.entrance = new Board<Student>(students);

        this.diningRoom = new HashMap<>();
        this.diningRoom.put(Color.RED, new Board<Student>());
        this.diningRoom.put(Color.YELLOW, new Board<Student>());
        this.diningRoom.put(Color.BLUE, new Board<Student>());
        this.diningRoom.put(Color.GREEN, new Board<Student>());
        this.diningRoom.put(Color.PINK, new Board<Student>());

        this.professorsTable = new Board<Professor>();
        this.towersBoard = new Board<Tower> (towers);
    }

    public Player getOwner(){
        return this.owner;
    }

    public Board<Student> getEntrance() {
        return this.entrance;
    }

    public Board<Student> getDiningRoomByColor(Color color) {
        return this.diningRoom.get(color);
    }

    public Board<Professor> getProfessorsTable() {
        return this.professorsTable;
    }

    public Board<Tower> getTowersBoard() {
        return this.towersBoard;
    }

    public boolean moveStudentDiningRoom(Student student) {
        if (this.diningRoom.get(student.getColor()).getPawns().size() < 8) {
            this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
        }

        if ((this.diningRoom.get(student.getColor()).getPawns().indexOf(student)) % 3 == 2) return true;
        return false;
    }

    public void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }

    public void takeStudentsFromCloud(Cloud cloud){
        for (Student student : cloud.getPawns()){
            entrance.moveInPawn(student, cloud);
        }
    }

    public void controlProfessor(Professor professor, Board<Professor> src) {
        professorsTable.moveInPawn(professor, src);
    }

    public void takeInTower(Tower tower){
        List<Tower> temp = towersBoard.getPawns();
        temp.add(tower);
        towersBoard = new Board<>(temp);
    }

    public Tower takeOutTower(){
        Tower tower;
        List<Tower> temp = towersBoard.getPawns();
        tower = temp.remove(temp.size()-1);
        towersBoard = new Board<>(temp);
        return tower;
    }

}
