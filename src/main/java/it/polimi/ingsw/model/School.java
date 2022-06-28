package it.polimi.ingsw.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InvalidActionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {

    /**
     * Identifier
     */
    private final int id;

    /**
     * Entrance
     */
    private final Board<Student> entrance;

    /**
     * Map of dining room with colors
     */
    private final Map<Color, Board<Student>> diningRoom;

    /**
     * Professors table
     */
    private final Board<Professor> professorsTable;

    /**
     * Towers board
     */
    private final Board<Tower> towersBoard;

    /**
     * Constructor of School
     * @param id identifier
     * @param students list of students
     * @param towers list of towers
     */
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

    /**
     * Getter for id
     * @return identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for entrance
     * @return entrance
     */
    protected Board<Student> getEntrance() {
        return this.entrance;
    }

    /**
     * Getter for dining room
     * @param color color of students
     * @return dining room section
     */
    protected Board<Student> getDiningRoomByColor(Color color) {
        return this.diningRoom.get(color);
    }

    /**
     * Getter for professors table
     * @return professors table
     */
    protected Board<Professor> getProfessorsTable() {
        return this.professorsTable;
    }

    /**
     * Getter for towers board
     * @return towers board
     */
    protected Board<Tower> getTowersBoard() {
        return this.towersBoard;
    }

    /**
     * Move student to dining room
     * @param student student to be moved
     * @throws InvalidActionException raised on invalid action
     */
    protected void moveStudentDiningRoom(Student student) throws InvalidActionException {
        if (this.diningRoom.get(student.getColor()).getNumPawns() < 10) {
            this.diningRoom.get(student.getColor()).moveInPawn(student, entrance);
        } else throw new InvalidActionException("Dining room is full!");
    }

    /**
     * Move student to island
     * @param student student to be moved
     * @param island island of destination
     */
    protected void moveStudentIsland(Student student, Island island) {
        island.moveInPawn(student, this.entrance);
    }

    /**
     * Take students from cloud
     * @param cloud cloud of source
     */
    protected void takeStudentsFromCloud(Cloud cloud){
        for (Student student : cloud.getPawns()){
            entrance.moveInPawn(student, cloud);
        }
    }

    /**
     * Return students to bag
     * @param bag StudentBag instance
     * @param color color of student
     * @param num amount of students
     */
    protected void returnStudentsToBag(StudentBag bag, Color color, int num) {
        Board<Student> diningTable = diningRoom.get(color);
        for (Student student : diningTable.getPawns()) {
            if (num > 0) {
                bag.moveInPawn(student, diningTable);
                num--;
            }
        }
    }

    /**
     * Set professor
     * @param professor profeessor
     * @param src source
     */
    protected void setProfessor(Professor professor, Board<Professor> src) {
        professorsTable.moveInPawn(professor, src);
    }

    /**
     * Exchange students between entrance and dining room
     * @param entrancePawnPosition position in entrance
     * @param color color
     * @throws InvalidActionException raised on invalid action
     */
    protected void exchangeStudentsDiningRoomEntrance(int entrancePawnPosition, Color color) throws InvalidActionException{
        try {
            Student entranceStudent = entrance.getPawns().get(entrancePawnPosition);
            Student diningStudent = getDiningRoomByColor(color).getPawns().get(getDiningRoomByColor(color).getNumPawns() - 1);

            if (getDiningRoomByColor(entranceStudent.getColor()).getNumPawns() < 10) {
                entrance.moveInPawn(diningStudent, getDiningRoomByColor(diningStudent.getColor()));
                getDiningRoomByColor(entranceStudent.getColor()).moveInPawn(entranceStudent, entrance);
            } else throw new InvalidActionException("Dining room is full!");
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidActionException("Invalid pawn chosen, retry!");
        }
    }

}
