package it.polimi.ingsw.client.view;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.Student;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CLI {

    public static final String ANSI_RESET = "\u001B[0m";

    private static final Map<Object, String> colorsMap = new HashMap<>(){{
        this.put(Color.YELLOW, "\u001B[33m");
        this.put(Color.GREEN, "\u001B[32m");
        this.put(Color.BLUE, "\u001B[34m");
        this.put(Color.RED, "\u001B[31m");
        this.put(Color.PINK, "\u001B[35m");
        this.put(TowerColor.BLACK, "\u001B[30m");
        this.put(TowerColor.WHITE, "\u001B[37m");
        this.put(TowerColor.GREY, "---");
    }};

    private static final Map<Object, Integer> positionMap = new HashMap<>(){{
        this.put(Color.GREEN, 0);
        this.put(Color.RED, 1);
        this.put(Color.YELLOW, 2);
        this.put(Color.PINK, 3);
        this.put(Color.BLUE, 4);
    }};

    private String getStudent(Color color) {
        return colorsMap.get(color) + "███" + ANSI_RESET;
    }

    private String getFreePlaceholder(String board, char identifier, Color color) {
        char[] boardChars = board.toCharArray();
        StringBuilder string = new StringBuilder();
        char colorId = Character.forDigit(positionMap.get(color), 10);
        int counter = 0;
        for (char c : boardChars) {
            if (c == identifier || counter != 0) {
                counter++;
                string.append(c);
            }
            if (counter == 2 && c != colorId) {
                counter = 0;
                string = new StringBuilder();
            } else if (counter == 3) {
                return string.toString();
            }
        }
        return null;
    }

    private String addStudentInBoard(String board, Color color) {
        String placeholder = getFreePlaceholder(board, 'S', color);
        if (placeholder != null) {
            board = board.replace(placeholder, getStudent(color));
        } else {
            System.out.println("ERROR. There is no space for a new student."); // To be removed --> new Exception
            //TODO: handle exception
        }
        return board;
    }

    private String getPrintableBoard(String board) {
        char[] boardChars = board.toCharArray();
        StringBuilder string = new StringBuilder();
        boolean isLetter;
        boolean isNumber;
        boolean isColorCommand = false;
        for (char c : boardChars) {
            if (c == '[') {
                isColorCommand = true;
            }
            isLetter = ((int) c >= (int) 'a' && (int) c <= (int) 'z') || ((int) c >= (int) 'A' && (int) c <= (int) 'Z');
            isNumber = ((int) c >= (int) '0' && (int) c <= (int) '9');
            if ((isLetter || isNumber) && !isColorCommand) {
                string.append(' ');
            } else {
                string.append(c);
            }
            if (isColorCommand && c == 'm') {
                isColorCommand = false;
            }
        }
        return string.toString();
     }

    private String getSchoolBoard() {
        String board =
                "╔═════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                "║                 ┌─────┐ ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐   ┌─────┐ ┌─────┐ ║\n" +
                "║  ┌─────┬─────┐  │ P00 │ │ S00 ¦ S01 ¦ S02 ¦ S03 ¦ S04 ¦ S05 ¦ S06 ¦ S07 ¦ S08 ¦ S09 │   │     │ │     │ ║\n" +
                "║  │     │     │  ├─────┤ ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤   ├─────┤ ├─────┤ ║\n" +
                "║  ├─────┼─────┤  │ P10 │ │ S10 ¦ S11 ¦ S12 ¦ S13 ¦ S14 ¦ S15 ¦ S16 ¦ S17 ¦ S18 ¦ S19 │   │     │ │     │ ║\n" +
                "║  │     │     │  ├─────┤ ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤   ├─────┤ ├─────┤ ║\n" +
                "║  ├─────┼─────┤  │ P20 │ │ S20 ¦ S21 ¦ S22 ¦ S23 ¦ S24 ¦ S25 ¦ S26 ¦ S27 ¦ S28 ¦ S29 │   │     │ │     │ ║\n" +
                "║  │     │     │  ├─────┤ ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤   ├─────┤ ├─────┤ ║\n" +
                "║  ├─────┼─────┤  │ P30 │ │ S30 ¦ S31 ¦ S32 ¦ S33 ¦ S34 ¦ S35 ¦ S36 ¦ S37 ¦ S38 ¦ S39 │   │     │ │     │ ║\n" +
                "║  │     │     │  ├─────┤ ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤   ├─────┤ └─────┘ ║\n" +
                "║  └─────┴─────┘  │ P40 │ │ S40 ¦ S41 ¦ S42 ¦ S43 ¦ S44 ¦ S45 ¦ S46 ¦ S47 ¦ S48 ¦ S49 │   │     │         ║\n" +
                "║                 └─────┘ └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘   └─────┘         ║\n" +
                "╚═════════════════════════════════════════════════════════════════════════════════════════════════════════╝";
        return board;
    }

    public void showGameBoard() {
        String school = this.getSchoolBoard();

        // Demo bad print
        System.out.println("\nTest 1");
        System.out.println(school);

        // Testing: add some students
        school = this.addStudentInBoard(school, Color.BLUE);
        school = this.addStudentInBoard(school, Color.BLUE);
        school = this.addStudentInBoard(school, Color.RED);

        // Demo good print
        System.out.println("\nTest 2");
        System.out.println(getPrintableBoard(school));

        // Testing: add some other students
        school = this.addStudentInBoard(school, Color.GREEN);
        school = this.addStudentInBoard(school, Color.YELLOW);
        school = this.addStudentInBoard(school, Color.RED);

        // Demo good print
        System.out.println("\nTest 3");
        System.out.println(getPrintableBoard(school));


    }

}
