package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.util.*;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class CLI {
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
        this.put(TowerColor.GREY, "\u001B[90m");
        this.put(TowerColor.WHITE, "\u001B[37m");
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
        this.put("s", "███");
        this.put("p", "PRO");
        this.put("t", "TOW");
        this.put("m", "MON");
    }};

    private Map<String, Integer> getFirstAvailablePlaceholder(String[][] board, String identifier, Object color) {
        Map<String, Integer> placeholder = new HashMap<>();
        String colorRow = "0";
        if (identifier.equals("S") || identifier.equals("P")) {
            colorRow = String.valueOf(Character.forDigit(positionMap.get(color), 10));
        }
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

    private void addPawnToBoard(String[][] board, Object color, String identifier) {
        Map<String, Integer> placeholder = null;
        if (!(color instanceof Color) && !(color instanceof TowerColor)) {
            //TODO: handle exception
            System.out.println("Error: not valid color.");
        }
        if (pawnsMap.containsKey(identifier.toLowerCase())) {
            placeholder = this.getFirstAvailablePlaceholder(board, identifier, color);
        }
        if (placeholder != null) {
            int i = placeholder.get("start_row");
            int j = placeholder.get("start_col");
            board[i][j] = colorsMap.get(color) + board[i][j].toLowerCase();
            board[i][j+2] = board[i][j] + ANSI_RESET;
        } else {
            System.out.println("ERROR. There is no space for a new pawn. (Identifier=" + identifier + ")."); // To be removed --> new Exception

            //TODO: handle exception
        }
    }

    private void addStudentToBoard(String[][] board, Color color) {
        this.addPawnToBoard(board, color, "S");
    }

    private void addProfessorToBoard(String[][] board, Color color) {
        this.addPawnToBoard(board, color, "P");
    }

    private void addTowerToBoard(String[][] board, TowerColor color) {
        this.addPawnToBoard(board, color, "T");
    }

    private void addMotherNatureToBoard(String[][] board) {
        this.addPawnToBoard(board, Color.RED, "M");
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
                if (disposition[i][j] >= 0 && disposition[i][j] < 13) {
                    if (islandsLinkedToNext.get(disposition[i][j])) {

                        int linkerBaseRowOnSet = 0;
                        int linkerBaseColOnSet = 0;

                        if (i == 1 && j == 0) {
                            linker = islandLinkers.get("tl");
                            linkerBaseRowOnSet = i * (islandH + spaceH) - (islandH + spaceH);
                            linkerBaseColOnSet = j * (islandW + spaceW);
                        } else if (i == 0 && j == disposition[i].length-2) {
                            linker = islandLinkers.get("tr");
                            linkerBaseRowOnSet = i * (islandH + spaceH);
                            linkerBaseColOnSet = j * (islandW + spaceW) + islandW - 1;
                        } else if (i == disposition.length-1 && j == 1) {
                            linker = islandLinkers.get("bl");
                            linkerBaseRowOnSet = i * (islandH + spaceH) + islandH - 1 - (islandH + spaceH);
                            linkerBaseColOnSet = j * (islandW + spaceW) - (islandW + spaceW);
                        } else if (i == disposition.length-2 && j == disposition[i].length-1) {
                            linker = islandLinkers.get("br");
                            linkerBaseRowOnSet = i * (islandH + spaceH) + islandH - 1;
                            linkerBaseColOnSet = j * (islandW + spaceW) - spaceW - 1;
                        } else if (i == 0 || i == disposition.length-1) {
                            linker = islandLinkers.get("hr");
                            linkerBaseRowOnSet = i * (islandH + spaceH);
                            linkerBaseColOnSet = j * (islandW + spaceW) + islandW - 1;
                            if (i == disposition.length-1) {
                                linkerBaseColOnSet -= (islandW + spaceW);
                            }
                        } else if (j == 0 || j == disposition[i].length-1) {
                            linker = islandLinkers.get("vr");
                            linkerBaseRowOnSet = i * (islandH + spaceH) + islandH - 1;
                            linkerBaseColOnSet = j * (islandW + spaceW);
                            if (j == 0) {
                                linkerBaseRowOnSet -= (islandH + spaceH);
                            }
                        }

                        System.out.print(i);
                        System.out.print(' ');
                        System.out.print(j);
                        System.out.print('\n');

                        for (int linkerRow=0; linkerRow<linker.length; linkerRow++) {
                            for (int linkerCol=0; linkerCol<linker[0].length; linkerCol++) {
                                linkerRowOnSet = linkerBaseRowOnSet + linkerRow;
                                linkerColOnSet = linkerBaseColOnSet + linkerCol;
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
        // Initialize island islands_linkers
        Map<String, String[][]> islandLinkers = this.loader.getIslandLinkers();
        // TODO should be moved into the constructor

        // Create three schools
        String[][] mainSchool = this.loader.getSchool(false);
        String[][] vertSchoolOne = this.loader.getSchool(true);
        String[][] vertSchoolTwo = this.loader.getSchool(true);

        // Add some students to the schools
        this.addStudentToBoard(mainSchool, Color.RED);
        this.addStudentToBoard(mainSchool, Color.RED);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.GREEN);
        this.addStudentToBoard(mainSchool, Color.GREEN);
        this.addStudentToBoard(mainSchool, Color.GREEN);
        this.addStudentToBoard(mainSchool, Color.GREEN);
        this.addStudentToBoard(mainSchool, Color.PINK);
        this.addStudentToBoard(mainSchool, Color.PINK);
        this.addStudentToBoard(mainSchool, Color.PINK);
        this.addStudentToBoard(mainSchool, Color.PINK);
        this.addStudentToBoard(mainSchool, Color.PINK);
        this.addStudentToBoard(mainSchool, Color.BLUE);
        this.addStudentToBoard(mainSchool, Color.BLUE);
        this.addStudentToBoard(mainSchool, Color.BLUE);
        this.addStudentToBoard(mainSchool, Color.BLUE);
        this.addStudentToBoard(mainSchool, Color.BLUE);

        this.addStudentToBoard(vertSchoolOne, Color.RED);
        this.addStudentToBoard(vertSchoolOne, Color.YELLOW);
        this.addStudentToBoard(mainSchool, Color.GREEN);

        // Add some professors
        this.addProfessorToBoard(mainSchool, Color.RED);

        // Create a list of schools
        ArrayList<String[][]> schools = new ArrayList<>();
        schools.add(mainSchool);
        schools.add(vertSchoolOne);
        schools.add(vertSchoolTwo);

        // Create a list of 12 islands
        ArrayList<String[][]> islands = new ArrayList<>();
        for (int i=0; i<12; i++) {
            islands.add(this.loader.getIsland());
        }

        // Initialize island links relations
        ArrayList<Boolean> islandsLinkedToNext = new ArrayList<Boolean>();
        for (int i=0; i<12; i++) {
            islandsLinkedToNext.add(false);
        }

        this.addTowerToBoard(islands.get(0), TowerColor.WHITE);
        this.addTowerToBoard(islands.get(1), TowerColor.GREY);
        this.addTowerToBoard(islands.get(2), TowerColor.BLACK);

        this.addMotherNatureToBoard(islands.get(1));

        this.addStudentToBoard(islands.get(0), Color.RED);
        this.addStudentToBoard(islands.get(0), Color.YELLOW);
        this.addStudentToBoard(islands.get(0), Color.GREEN);
        this.addStudentToBoard(islands.get(0), Color.PINK);
        this.addStudentToBoard(islands.get(0), Color.BLUE);

        this.addStudentToBoard(islands.get(1), Color.RED);
        this.addStudentToBoard(islands.get(1), Color.GREEN);
        this.addStudentToBoard(islands.get(1), Color.BLUE);

        this.addStudentToBoard(islands.get(4), Color.RED);
        this.addStudentToBoard(islands.get(4), Color.YELLOW);
        this.addStudentToBoard(islands.get(4), Color.BLUE);

        this.addStudentToBoard(islands.get(5), Color.RED);
        this.addStudentToBoard(islands.get(5), Color.GREEN);
        this.addStudentToBoard(islands.get(5), Color.BLUE);

        this.addStudentToBoard(islands.get(9), Color.YELLOW);
        this.addStudentToBoard(islands.get(9), Color.GREEN);
        this.addStudentToBoard(islands.get(9), Color.PINK);
        this.addStudentToBoard(islands.get(9), Color.BLUE);

        // Add link Island3 <--> Island4 (only this one supported at the moment)

        // top_right
        islandsLinkedToNext.set(3, true);

        // top_left
        islandsLinkedToNext.set(11, true);

        // bottom_right
        islandsLinkedToNext.set(5, true);

        // bottom_left
        islandsLinkedToNext.set(9, true);

        // horizontal
        islandsLinkedToNext.set(0, true);
        islandsLinkedToNext.set(1, true);
        islandsLinkedToNext.set(2, true);
        islandsLinkedToNext.set(6, true);
        islandsLinkedToNext.set(7, true);
        islandsLinkedToNext.set(8, true);

        // vertical
        islandsLinkedToNext.set(4, true);
        islandsLinkedToNext.set(10, true);



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
