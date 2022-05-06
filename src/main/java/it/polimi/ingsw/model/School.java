package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {

    //private Player owner;
    private final int id;

    private final Board<Student> entrance;
    private final Map<Color, Board<Student>> diningRoom;
    private final Board<Professor> professorsTable;
    private final Board<Tower> towersBoard;

    protected School(int id, List<Student> students, List<Tower> towers) {
        //this.owner = owner;
        this.id = id;

        this.entrance = new Board<>(students);

        this.diningRoom = new HashMap<>();
        this.diningRoom.put(Color.RED, new Board<>());
        this.diningRoom.put(Color.YELLOW, new Board<>());
        this.diningRoom.put(Color.BLUE, new Board<>());
        this.diningRoom.put(Color.GREEN, new Board<>());
        this.diningRoom.put(Color.PINK, new Board<>());

        this.professorsTable = new Board<>();
        this.towersBoard = new Board<>(towers);
    }

    /*protected Player getOwner(){
        return this.owner;
    }*/

    public int getId() {
        return id;
    }

    protected Board<Student> getEntrance() {
        return this.entrance;
    }

    protected Board<Student> getDiningRoomByColor(Color color) {
        return this.diningRoom.get(color);
    }

    protected Board<Professor> getProfessorsTable() {
        return this.professorsTable;
    }

    protected Board<Tower> getTowersBoard() {
        return this.towersBoard;
    }

    protected void moveStudentDiningRoom(Student student) {
        if (this.diningRoom.get(student.getColor()).getNumPawns() < 10) {
            this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
        }
    }

    protected void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }

    protected void takeStudentsFromCloud(Cloud cloud){
        for (Student student : cloud.getPawns()){
            entrance.moveInPawn(student, cloud);
        }
    }

    protected void returnStudentsToBag(StudentBag bag, Color color, int num) {
        Board<Student> diningTable = diningRoom.get(color);
        for (Student student : diningTable.getPawns()) {
            if (num > 0) {
                bag.moveInPawn(student, diningTable);
                num--;
            }
        }
    }

    protected void setProfessor(Professor professor, Board<Professor> src) {
        professorsTable.moveInPawn(professor, src);
    }

    protected void exchangeStudentsDiningRoomEntrance(Color color, int entrancePawnPosition) {
        Student entranceStudent = entrance.getPawns().get(entrancePawnPosition);
        Student diningStudent = getDiningRoomByColor(color).getPawns().get(getDiningRoomByColor(color).getNumPawns() - 1);

        entrance.moveInPawn(diningStudent, getDiningRoomByColor(diningStudent.getColor()));
        getDiningRoomByColor(entranceStudent.getColor()).moveInPawn(entranceStudent, entrance);
    }

}
