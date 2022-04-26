package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.util.*;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class CLI {

    private final String STUD_PLACEHOLDER = "███";
    private final String PROF_PLACEHOLDER = "###";
    private final AssetsLoader loader;

    public CLI() {
        this.loader = new AssetsLoader();
    }

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

    private static final Map<Character, Character> bordersMap = new HashMap<>(){{
        this.put('║', '═');
        this.put('╚', '╗');
        this.put('│', '─');
        this.put('¦', '─');
        this.put('├', '┬');
        this.put('┤', '┴');
        this.put('└', '┐');
    }};

    private static final Map<String, String> pawnsMap = new HashMap<>(){{
        this.put("s", " ● ");
        this.put("p", "PRO");
    }};

    private String getStudent(Color color) {
        return colorsMap.get(color) + this.STUD_PLACEHOLDER + ANSI_RESET;
    }

    private String getProfessor(Color color) {
        return colorsMap.get(color) + this.PROF_PLACEHOLDER + ANSI_RESET;
    }

    private Map<String, Integer> getFirstAvailablePlaceholder(String[][] board, String identifier, Color color) {
        Map<String, Integer> placeholder = new HashMap<>();
        String colorRow = String.valueOf(Character.forDigit(positionMap.get(color), 10));
        int counter = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (counter == 0) {
                    if (board[i][j].equals(identifier)) {
                        placeholder.put("start_row", i);
                        placeholder.put("start_col", j);
                        counter++;
                    }
                } else if (counter == 1 && !board[i][j].equals(colorRow)) {
                        placeholder.clear();
                        counter = 0;
                } else if (counter == 3) {
                    return placeholder;
                } else {
                    counter++;
                }
            }
        }
        return null;
    }

    private void addStudentToBoard(String[][] board, Color color) {
        Map<String, Integer> placeholder = this.getFirstAvailablePlaceholder(board, "S", color);
        if (placeholder != null) {
            int i = placeholder.get("start_row");
            int j = placeholder.get("start_col");
            board[i][j] = colorsMap.get(color) + board[i][j].toLowerCase();
            board[i][j+2] = board[i][j] + ANSI_RESET;
        } else {
            System.out.println("ERROR. There is no space for a new student."); // To be removed --> new Exception
            //TODO: handle exception
        }
    }

    @Deprecated
    private char[][] rotateBoard(char[][] board) {
        char[][] rotated = new char[board[0].length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (bordersMap.containsKey(board[i][j])) {
                    rotated[j][i] = bordersMap.get(board[i][j]);
                } else if (bordersMap.containsValue(board[i][j])) {
                    for (Map.Entry<Character, Character> entry : bordersMap.entrySet()) {
                        if (entry.getValue().equals(board[i][j])) {
                            rotated[j][i] = entry.getKey();
                        }
                    }
                } else {
                    rotated[j][i] = board[i][j];
                }
            }
        }
        return rotated;
    }

    private String getPrintableBoard(String[][] board, boolean removePlaceholders) {
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        String oldString = null;
        String identifier = null;
        for (String[] row : board) {
            for (String c : row) {
                // To be changed
                if (c.length() > 4) {
                    if (removePlaceholders && (isLetter(c.charAt(5)) || isDigit(c.charAt(5)))) {
                        if (isLetter(c.charAt(5)) && Character.isLowerCase(c.charAt(5))) {
                            identifier = String.valueOf(c.charAt(5));
                            oldString = c;
                        }
                        if (identifier != null) {
                            builder.append(oldString.replace(identifier, String.valueOf(pawnsMap.get(identifier).charAt(counter))));
                            if(counter < 2) {
                                counter++;
                            } else {
                                identifier = null;
                                counter = 0;
                            }
                        } else {
                            builder.append(' ');
                        }
                    } else {
                        builder.append(c);
                    }
                } else {
                    if (removePlaceholders && (isLetter(c.charAt(0)) || isDigit(c.charAt(0)))) {
                        if (isLetter(c.charAt(0)) && Character.isLowerCase(c.charAt(0))) {
                            identifier = c;
                        }
                        if (identifier != null) {
                            builder.append(pawnsMap.get(identifier).charAt(counter));
                            if(counter < 2) {
                                counter++;
                            } else {
                                identifier = null;
                                counter = 0;
                            }
                        } else {
                            builder.append(' ');
                        }
                    } else {
                        builder.append(c);
                    }
                }

            }
            builder.append('\n');
        }
        return builder.toString();
    }

    private String[][] buildSetOfIslands(ArrayList<String[][]> islands, Map<String, String[][]> islandLinkers, ArrayList<Boolean> islandsLinkedToNext) {
        int[][] disposition = {
                {110,   0,      1,      2,      3,      34},
                {11,    -1,     -1,     -1,     -1,     4 },
                {10,    -1,     -1,     -1,     -1,     5 },
                {910,   9,      8,      7,      6,      56},
        };
        /* disposition example:
             ###  ###  ###  ###
        ###                      ###
        ###                      ###
             ###  ###  ###  ###
        */

        int islandH = islands.get(0).length;
        int islandW = islands.get(0)[0].length;

        int spaceH = 1;
        int spaceW = 4;

        int islandsRows = disposition.length;
        int islandsCols = disposition[0].length;
        int row = (islandH + spaceH) * islandsRows - spaceH;
        int col = (islandW + spaceW) * islandsCols - spaceW;

        String[][] set = new String[row][col];

        int islandDispositionRowIndex;
        int islandDispositionColIndex;
        int islandIndex;

        for (int i=0; i<set.length; i++) {
            for (int j=0; j<set[i].length; j++) {

                islandDispositionRowIndex = i / (islandH + spaceH);
                islandDispositionColIndex = j / (islandW + spaceW);
                islandIndex = disposition[islandDispositionRowIndex][islandDispositionColIndex];

                if (i%(islandH+spaceH) < islandH && j%(islandW+spaceW) < islandW) {
                    if (islandIndex >= 0 && islandIndex < 13) {
                        set[i][j] = islands.get(islandIndex)[i%(islandH+spaceH)][j%(islandW+spaceW)];
                    } else {
                        set[i][j] = " ";
                    }

                } else {
                    set[i][j] = " ";
                }

            }
        }

        String[][] linker = null;
        int linkerRowOnSet = 0;
        int linkerColOnSet = 0;

        for (int i=0; i<disposition.length; i++) {
            for (int j=0; j<disposition[i].length; j++) {
                if (disposition[i][j] > 0 && disposition[i][j] < 13) {
                    if (islandsLinkedToNext.get(disposition[i][j])) {
                        linker = islandLinkers.get("tr");

                        for (int linkerRow=0; linkerRow<linker.length; linkerRow++) {
                            for (int linkerCol=0; linkerCol<linker[0].length; linkerCol++) {
                                linkerRowOnSet = i * (islandH + spaceH) + linkerRow;
                                linkerColOnSet = j * (islandW + spaceW) + islandW + linkerCol - 1;
                                set[linkerRowOnSet][linkerColOnSet] = linker[linkerRow][linkerCol];
                            }
                        }

                    }
                }
            }
        }

        return set;
    }

    private String[][] buildGameBoard(ArrayList<String[][]> schools, String[][] setOfIslands) {
        int space = 4;
        int height = Math.max((setOfIslands.length + schools.get(0).length), schools.get(1).length);
        int width = setOfIslands[0].length + schools.get(1)[0].length + space;
        String[][] gameBoard = new String[height][width];

        int mainSchoolOffset = (setOfIslands[0].length - schools.get(0)[0].length) / 2;

        for (int i=0; i<gameBoard.length; i++) {
            for (int j=0; j<gameBoard[i].length; j++) {

                gameBoard[i][j] = " ";

                // Side school
                if (i < schools.get(1).length) {
                    if (i < setOfIslands.length && j < setOfIslands[0].length) {
                        gameBoard[i][j] = setOfIslands[i][j];
                    } else if (j-setOfIslands[0].length >= space && j-setOfIslands[0].length < schools.get(1)[i].length+space) {
                        gameBoard[i][j] = schools.get(1)[i][j-setOfIslands[0].length-space].toString();
                    } else {
                        gameBoard[i][j] = " ";
                    }
                }

                // Main school
                if (i >= setOfIslands.length && j < setOfIslands[0].length) {
                    if (j >= mainSchoolOffset && j-mainSchoolOffset < schools.get(0)[0].length) {
                        gameBoard[i][j] = schools.get(0)[i-setOfIslands.length][j-mainSchoolOffset].toString();
                    } else {
                        gameBoard[i][j] = " ";
                    }
                }

            }
        }

        return gameBoard;
    }

    public void showGameBoard() {
        // Initialize island linkers
        Map<String, String[][]> islandLinkers = this.loader.getIslandLinkers();
        // TODO should be moved into the constructor

        // Create three schools
        String[][] mainSchool = this.loader.getSchool(false);
        String[][] vertSchoolOne = this.loader.getSchool(true);
        String[][] vertSchoolTwo = this.loader.getSchool(true);

        // Add some students to the schools
        this.addStudentToBoard(mainSchool, Color.RED);
        this.addStudentToBoard(mainSchool, Color.RED);
        this.addStudentToBoard(vertSchoolOne, Color.RED);
        this.addStudentToBoard(vertSchoolOne, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.GREEN);

        // Create a list of schools
        ArrayList<String[][]> schools = new ArrayList<>();
        schools.add(mainSchool);
        schools.add(vertSchoolOne);
        schools.add(vertSchoolTwo);

        // Create a list of 12 islands
        ArrayList<String[][]> islands = new ArrayList<>();
        for (int i=0; i < 12; i++) {
            islands.add(this.loader.getIsland());
        }

        // Initialize island links relations
        ArrayList<Boolean> islandsLinkedToNext = new ArrayList<Boolean>();
        for (int i=0; i < 12; i++) {
            islandsLinkedToNext.add(false);
        }

        this.addStudentToBoard(islands.get(0), Color.RED);
        this.addStudentToBoard(islands.get(0), Color.YELLOW);
        this.addStudentToBoard(islands.get(4), Color.BLUE);

        // Add link Island3 <--> Island4 (only this one supported at the moment)
        islandsLinkedToNext.set(3, true);

        // Build set of islands
        String[][] setOfIslands = this.buildSetOfIslands(islands, islandLinkers, islandsLinkedToNext);

        // Build game board
        String[][] gameBoard = this.buildGameBoard(schools, setOfIslands);

        // Print game board with placeholders for debug
        System.out.println("DEBUG GAMEBOARD with PLACEHOLDERS");
        System.out.println(this.getPrintableBoard(gameBoard, false));

        System.out.println("\n\n\n\n");

        // Print game board
        System.out.println("DEBUG GAMEBOARD");
        System.out.println(this.getPrintableBoard(gameBoard, true));

    }

}
