package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Math.min;

public class CLI {

    private class CharacterCardContent {
        public CharacterCardContent(int id, int coins, String text) {

        }
    }

    private class AssistantCardContent {
        public AssistantCardContent(int value, int steps) {

        }
    }

    private final Map<String, String[][]> islandLinkers;
    private final ArrayList<String[][]> schools = new ArrayList<>();
    private final ArrayList<String[][]> islands = new ArrayList<>();
    private final ArrayList<String[][]> clouds = new ArrayList<>();
    private final ArrayList<Boolean> islandsLinkedToNext = new ArrayList<>();
    private final String[][] characterCardTest;

    private ArrayList<String[]> characterCards = new ArrayList<>();
    /*
    private ArrayList<AssistantCardContent> = new ArrayList<>();


     */
    public CLI(Integer playersNumber) {
        final AssetsLoader loader = new AssetsLoader();
        this.islandLinkers = loader.getIslandLinkers();

        // Create schools
        boolean isMain = false;
        for (int i=0; i<playersNumber; i++) {
            this.schools.add(loader.getSchool(isMain));
            isMain = true;
        }

        // Create twelve islands
        for (int i=0; i<12; i++) {
            this.islands.add(loader.getIsland());
            this.islandsLinkedToNext.add(false);
        }

        // Create three clouds
        for (int i=0; i<3; i++) {
            this.clouds.add(loader.getCloud());
        }

        this.characterCardTest = loader.getCharacterCardContainer();

    }

    public ArrayList<String[][]> getSchools() {
        return this.schools;
    }

    public ArrayList<String[][]> getClouds() {
        return this.clouds;
    }

    public ArrayList<String[][]> getIslands() {
        return this.islands;
    }

    public void addLinkToNextIsland(int islandId) {
        this.islandsLinkedToNext.set(islandId, true);
    }

    public void removeLinkToNextIsland(int islandId) {
        this.islandsLinkedToNext.set(islandId, false);
    }

    public static final String ANSI_RESET = "\u001B[0m";

    private static final Map<Object, String> colorsMap = new HashMap<>(){{
        this.put(Color.YELLOW, "\u001B[93m");
        this.put(Color.GREEN, "\u001B[32m");
        this.put(Color.BLUE, "\u001B[34m");
        this.put(Color.RED, "\u001B[31m");
        this.put(Color.PINK, "\u001B[95m");
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
        this.put("s", " ● ");
        this.put("p", " ⬢ ");
        this.put("t", " ♜ ");
        this.put("n", " ♟ ");
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

    private String[][] findBoardBy(int id, ArrayList<String[][]> boardList) {
        return boardList.get(id);
    }

    /*
    protected void addCharacterCard(String text, int id, int coins) {
        this.characterCards.add();
    }
    */

    // SCHOOLS METHODS
    protected void addStudentToSchoolDiningRoom(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "S");
    }
    protected void addStudentToSchoolEntrance(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "E");
    }
    protected void addProfessorToSchool(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "P");
    }
    protected void addTowerToSchool(int id, TowerColor color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "T");
    }

    // ISLANDS METHODS
    protected void addStudentToIsland(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, color, "S");
    }
    protected void addMotherNatureToIsland(int id) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, Color.RED, "N");
    }
    protected void addTowerToIsland(int id, TowerColor color) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, color, "T");
    }

    // CLOUDS METHODS
    protected void addStudentToCloud(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.clouds);
        this.addPawnToBoard(board, color, "S");
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

        int cardTextWidth = 18;
        int cardTextHeight = 13;
        int index = 0;

        int builderRowWidth;
        int previousBuilderRowWidth = 0;
        boolean isFirst = true;
        ArrayList<Integer> builderRowsWidth = new ArrayList<>();
        for (int i=0; i<board.length; i++) {
            if (isFirst) {
                builderRowWidth = builder.indexOf("\n") + 1;
                isFirst = false;
            } else {
                builderRowWidth = builder.indexOf("\n", previousBuilderRowWidth) + 1;
            }
            builderRowsWidth.add(builderRowWidth-previousBuilderRowWidth);
            previousBuilderRowWidth = builderRowWidth;
        }

        String cardTitle = "CARD: 1   COINS: 3";

        String cardText = "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.";

        Pattern p = Pattern.compile(".{1," + Integer.toString(cardTextWidth) + "}(\\s+|$)");
        Matcher m = p.matcher(cardText);
        ArrayList<String> cardTextLines = new ArrayList<>();
        while(m.find()) {
            cardTextLines.add(m.group().trim());
        }

        int charPositionInCard;

        for (int c=0; c<builder.length(); c++) {

            if (builder.charAt(c) == '$') {
                for (int j=0; j<cardTitle.length(); j++) {
                    builder.setCharAt(c+j, cardTitle.charAt(j));
                }
            }

            if (builder.charAt(c) == '!') {

                for (int i=0; i<min(cardTextHeight, cardTextLines.size()); i++) {


                    for (int j=0; j<min(cardTextWidth, cardTextLines.get(i).length()); j++) {

                        charPositionInCard = i * cardTextWidth + j;
                        // index = c + j + charPositionInCard/cardTextWidth * builderRowsWidth.get(i+11);
                        // index = (c + j) + (i * builderRowsWidth.get(i+11));
                        index = (c + j);
                        for (int k=11; k<i+11; k++) {
                            index += builderRowsWidth.get(k+1);
                        }
                        char selectedChar = cardTextLines.get(i).charAt(j);

                        builder.setCharAt(index, selectedChar);


                    }

                }

            }

        }

        return builder.toString();
    }

    private String[][] buildSetOfIslands() {
        int[][] disposition = {
                {110,   0,      1,      2,      3,      34},
                {11,    -1,     -2,     -3,     -5,     4 },
                {10,    -9,     -9,     -9,     -9,     5 },
                {910,   9,      8,      7,      6,      56}
        };

        /* disposition example:
             ###  ###  ###  ###
        ###                      ###
        ###                      ###
             ###  ###  ###  ###
        */

        int islandH = this.islands.get(0).length;
        int islandW = this.islands.get(0)[0].length;

        int spaceH = 1;
        int spaceW = 2;

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
                        set[i][j] = this.islands.get(islandIndex)[i%(islandH+spaceH)][j%(islandW+spaceW)];
                    } else {
                        set[i][j] = " ";
                    }

                } else {
                    set[i][j] = " ";
                }

            }
        }

        // Add two extra row in middle
        String[][] expandedSet = new String[row+2][col];
        for (int i=0; i<expandedSet.length; i++) {
            for (int j=0; j<expandedSet[i].length; j++) {
                if (i < row/2) {
                    expandedSet[i][j] = set[i][j];
                } else if (i == row/2 || i == (row/2) + 1) {
                    expandedSet[i][j] = " ";
                } else {
                    expandedSet[i][j] = set[i-2][j];
                }
            }
        }
        set = expandedSet;


        String[][] linker = null;
        int linkerRowOnSet = 0;
        int linkerColOnSet = 0;

        for (int i=0; i<disposition.length; i++) {
            for (int j=0; j<disposition[i].length; j++) {
                if (disposition[i][j] >= 0 && disposition[i][j] < 13) {
                    if (this.islandsLinkedToNext.get(disposition[i][j])) {

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
                            linkerBaseRowOnSet = i * (islandH + spaceH) + islandH - 1 - (islandH + spaceH) + 2;
                            linkerBaseColOnSet = j * (islandW + spaceW) - (islandW + spaceW);
                        } else if (i == disposition.length-2 && j == disposition[i].length-1) {
                            linker = islandLinkers.get("br");
                            linkerBaseRowOnSet = i * (islandH + spaceH) + islandH - 1 + 2;
                            linkerBaseColOnSet = j * (islandW + spaceW) - spaceW - 1;
                        } else if (i == 0 || i == disposition.length-1) {
                            linker = islandLinkers.get("hr");
                            linkerBaseRowOnSet = i * (islandH + spaceH);
                            if (i > disposition.length/2) {
                                linkerBaseRowOnSet += 2;
                            }
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

        for (int i=0; i<disposition.length; i++) {
            for (int j=0; j<disposition[i].length; j++) {

                int cardRowOnSet;
                int cardColOnSet;
                int cardBaseRowOnSet = 8;
                int cardBaseColOnSet = 0;

                boolean printCard = false;

                if (disposition[i][j] == -1) {
                    cardBaseColOnSet = 24;
                    printCard = true;
                } else if (disposition[i][j] == -2) {
                    cardBaseColOnSet = 48;
                    printCard = true;
                } else if (disposition[i][j] == -3) {
                    cardBaseColOnSet = 72;
                    printCard = true;
                }

                String[][] card = this.characterCardTest;

                if (printCard) {
                    for (int cardRow=0; cardRow<card.length; cardRow++) {
                        for (int cardCol=0; cardCol<card[0].length; cardCol++) {
                            cardRowOnSet = cardBaseRowOnSet + cardRow;
                            cardColOnSet = cardBaseColOnSet + cardCol;
                            set[cardRowOnSet][cardColOnSet] = card[cardRow][cardCol];
                        }
                    }
                }

            }
        }

        // Construct clouds rectangle

        int cloudH = this.clouds.get(0).length;
        int cloudW = this.clouds.get(0)[0].length;
        int cloudVerticalSpace = 1;

        int cloudRectangleRows = (cloudH + cloudVerticalSpace) * this.clouds.size() - cloudVerticalSpace;

        String[][] cloudsRectangle = new String[cloudRectangleRows][cloudW];

        for (int i=0; i<cloudsRectangle.length; i++) {
            for (int j=0; j<cloudsRectangle[i].length; j++) {

                if (i%(cloudH+cloudVerticalSpace) < cloudH) {
                    cloudsRectangle[i][j] = this.clouds.get(0)[i%(cloudH+cloudVerticalSpace)][j];
                } else {
                    cloudsRectangle[i][j] = " ";
                }

                /*
                islandDispositionRowIndex = i / (islandH + spaceH);
                islandDispositionColIndex = j / (islandW + spaceW);
                islandIndex = disposition[islandDispositionRowIndex][islandDispositionColIndex];

                if (i%(islandH+spaceH) < islandH && j%(islandW+spaceW) < islandW) {
                    if (islandIndex >= 0 && islandIndex < 13) {
                        set[i][j] = this.islands.get(islandIndex)[i%(islandH+spaceH)][j%(islandW+spaceW)];
                    } else {
                        set[i][j] = " ";
                    }

                } else {
                    set[i][j] = " ";
                }

                 */

            }
        }


        for (int i=0; i<disposition.length; i++) {
            for (int j=0; j<disposition[i].length; j++) {

                int cloudRowOnSet;
                int cloudColOnSet;
                int cloudBaseRowOnSet = 8;
                int cloudBaseColOnSet = 0;

                if (disposition[i][j] == -5) {
                    cloudBaseColOnSet = 96;

                    for (int cloudRow=0; cloudRow<cloudsRectangle.length; cloudRow++) {
                        for (int cloudCol=0; cloudCol<cloudsRectangle[0].length; cloudCol++) {
                            cloudRowOnSet = cloudBaseRowOnSet + cloudRow;
                            cloudColOnSet = cloudBaseColOnSet + cloudCol;
                            set[cloudRowOnSet][cloudColOnSet] = cloudsRectangle[cloudRow][cloudCol];
                        }
                    }
                }

            }
        }

        return set;
    }

    private String[][] buildGameBoard(String[][] setOfIslands) {
        int space = 4;
        int height = Math.max((setOfIslands.length + this.schools.get(0).length), this.schools.get(1).length);

        // int width = setOfIslands[0].length + this.schools.get(1)[0].length + space;
        int width = setOfIslands[0].length + this.schools.get(1)[0].length + space + this.schools.get(2)[0].length + space;

        String[][] gameBoard = new String[height][width];

        int mainSchoolOffset;

        for (int i=0; i<gameBoard.length; i++) {
            for (int j=0; j<gameBoard[i].length; j++) {

                gameBoard[i][j] = " ";

                mainSchoolOffset = (setOfIslands[0].length - schools.get(0)[0].length) / 2;

                if (this.schools.size() == 2) {
                    if (i < setOfIslands.length && j < setOfIslands[0].length) {
                        gameBoard[i][j] = setOfIslands[i][j];
                    } else if (j-setOfIslands[0].length >= space && j-setOfIslands[0].length < this.schools.get(1)[i].length+space) {
                        gameBoard[i][j] = this.schools.get(1)[i][j-setOfIslands[0].length-space].toString();
                    } else {
                        gameBoard[i][j] = "#";
                    }

                    // Main school
                    if (i >= setOfIslands.length && j < setOfIslands[0].length) {
                        if (j >= mainSchoolOffset && j-mainSchoolOffset < this.schools.get(0)[0].length) {
                            gameBoard[i][j] = this.schools.get(0)[i-setOfIslands.length][j-mainSchoolOffset].toString();
                        } else {
                            gameBoard[i][j] = "+";
                        }
                    }

                } else {

                    if (i < this.schools.get(1).length) {


                        if (j < this.schools.get(1)[i].length) {
                            gameBoard[i][j] = this.schools.get(1)[i][j];
                        } else if (j >= this.schools.get(1)[i].length + space && j - this.schools.get(1)[i].length - space < setOfIslands[0].length && i < setOfIslands.length) {
                            gameBoard[i][j] = setOfIslands[i][j - this.schools.get(1)[i].length - space];
                        } else if (j >= (this.schools.get(1)[i].length + space) + setOfIslands[0].length + space && j - this.schools.get(1)[i].length - space - setOfIslands[0].length - space < this.schools.get(1)[i].length && i < this.schools.get(1).length) {

                            gameBoard[i][j] = this.schools.get(2)[i][j - this.schools.get(1)[i].length - space - setOfIslands[0].length - space];
                        } else {
                            gameBoard[i][j] = " ";
                        }

                        /*
                        if (i >= setOfIslands.length && j < setOfIslands[0].length) {
                            if (j >= mainSchoolOffset && j-mainSchoolOffset < this.schools.get(0)[0].length) {
                                gameBoard[i][j] = this.schools.get(0)[i-setOfIslands.length][j-mainSchoolOffset].toString();
                            } else {
                                gameBoard[i][j] = "+";
                            }
                        }
                         */
                    }
                    mainSchoolOffset = this.schools.get(1)[0].length + space + (setOfIslands[0].length - this.schools.get(0)[0].length) / 2;
                    if (i >= setOfIslands.length && j >= mainSchoolOffset && j < setOfIslands[0].length + space + this.schools.get(1)[0].length) {
                        if (j - mainSchoolOffset < this.schools.get(0)[0].length) {
                            gameBoard[i][j] = this.schools.get(0)[i - setOfIslands.length][j - mainSchoolOffset].toString();
                        } else {
                            gameBoard[i][j] = " ";
                        }
                    }

                }

            }
        }

        return gameBoard;
    }

    public void showGameBoard() {
        // Build set of islands
        String[][] setOfIslands = this.buildSetOfIslands();
        // Build game board
        String[][] gameBoard = this.buildGameBoard(setOfIslands);
        // Print game board
        System.out.println(this.getPrintableBoard(gameBoard, true));
    }

}
