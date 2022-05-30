package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.model.card.AssistantCard;
import it.polimi.ingsw.model.card.CharacterCard;
import org.fusesource.jansi.AnsiConsole;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Math.min;

public class CLI {

    private final Map<String, String[][]> islandLinkers;
    private final List<String[][]> schools = new ArrayList<>();
    private final List<String[][]> islands = new ArrayList<>();
    private final List<String[][]> clouds = new ArrayList<>();
    private final List<Boolean> islandsLinkedToNext = new ArrayList<>();

    private final String[][] characterCardTest;
    private final String[][] assistantCardTest;
    private final List<CharacterCard> characterCards = new ArrayList<>();
    private final List<AssistantCard> assistantCards = new ArrayList<>();
    private final List<AssistantCard> lastPlayedAssistantCards = new ArrayList<>();
    private final String[][] lastPlayedAssistantCardsContainer;
    private final int playersNumber;
    private int boardCoins;

    private final List<String> nicknames;

    /*
    private ArrayList<AssistantCardContent> = new ArrayList<>();
    */


    public CLI(List<String> nicknames, List<Integer> coins) {
        this.playersNumber = nicknames.size();

        final AssetsLoader loader = new AssetsLoader();
        this.islandLinkers = loader.getIslandLinkers();

        this.nicknames = nicknames;

        // Create schools
        boolean isMain = false;
        String[][] school;
        for (int i=0; i<playersNumber; i++) {
            school = loader.getSchool(isMain);
            school = this.addCaptionToSchool(school, this.nicknames.get(i), coins.get(i));
            this.schools.add(school);
            isMain = true;
        }

        // Create twelve islands
        String[][] island;
        for (int i=1; i<=12; i++) {
            island = loader.getIsland();
            island = this.addNumberToIsland(island, i);
            this.islands.add(island);
            this.islandsLinkedToNext.add(false);
        }

        // Create three clouds
        for (int i=0; i<3; i++) {
            this.clouds.add(loader.getCloud());
        }

        this.characterCardTest = loader.getCharacterCardContainer();
        this.assistantCardTest = loader.getAssistantCardContainer();
        this.lastPlayedAssistantCardsContainer = loader.getLastPlayedAssistantCards();

        this.boardCoins = 0;

    }

    public void setBoardCoins(int boardCoins) {
        this.boardCoins = boardCoins;
    }

    public void addLinkToNextIsland(int islandId) {
        this.islandsLinkedToNext.set(islandId, true);
    }

    public static final String ANSI_RESET = "\u001B[0m";

    private static final Map<Object, String> colorsMap = new HashMap<>(){{
        this.put(Color.YELLOW, "\u001B[33m");
        this.put(Color.GREEN, "\u001B[32m");
        this.put(Color.BLUE, "\u001B[34m");
        this.put(Color.RED, "\u001B[31m");
        this.put(Color.PINK, "\u001B[95m");
        this.put(TowerColor.BLACK, "\u001B[0;30;47m");
        this.put(TowerColor.GREY, "\u001B[0;90m");
        this.put(TowerColor.WHITE, "\u001B[0;37m");
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
        this.put("s", "●");
        this.put("p", "■");
        this.put("t", "♜");
        this.put("n", "♟");
    }};

    private Map<String, Integer> getFirstAvailablePlaceholder(String[][] board, String identifier, Object color, boolean isAllColorsArea) {
        Map<String, Integer> placeholder = new HashMap<>();
        String colorRow = "0";
        if (identifier.equals("S") || identifier.equals("P")) {
            colorRow = String.valueOf(Character.forDigit(positionMap.get(color), 10));
        }
        if (isAllColorsArea) {
            colorRow = "A";
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

    private void addPawnToBoard(String[][] board, Object color, String identifier, boolean isAllColorsArea) {
        Map<String, Integer> placeholder = null;
        if (!(color instanceof Color) && !(color instanceof TowerColor)) {
            //TODO: handle exception
            System.out.println("Error: not valid color.");
        }
        if (pawnsMap.containsKey(identifier.toLowerCase())) {
            placeholder = this.getFirstAvailablePlaceholder(board, identifier, color, isAllColorsArea);
        }
        if (placeholder != null) {
            int i = placeholder.get("start_row");
            int j = placeholder.get("start_col");
            board[i][j] =  " " + colorsMap.get(color) + pawnsMap.get(identifier.toLowerCase()) + ANSI_RESET + " ";
            board[i][j+1] = "";
            board[i][j+2] = "";

        } else {
            System.out.println("ERROR. There is no space for a new pawn. (Identifier=" + identifier + ")."); // To be removed --> new Exception

            //TODO: handle exception
        }
    }

    private String[][] findBoardBy(int id, List<String[][]> boardList) {
        return boardList.get(id);
    }

    protected void addCharacterCard(CharacterCard card) {
        this.characterCards.add(card);
    }

    protected void addAssistantCard(AssistantCard card) {
        this.assistantCards.add(card);
    }

    protected void addLastPlayedAssistantCard(AssistantCard card) {
        this.lastPlayedAssistantCards.add(card);
    }

    // SCHOOLS METHODS
    protected void addStudentToSchoolDiningRoom(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "S", false);
    }
    protected void addStudentToSchoolEntrance(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "S", true);
    }
    protected void addProfessorToSchool(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "P", false);
    }
    protected void addTowerToSchool(int id, TowerColor color) {
        String[][] board = this.findBoardBy(id, this.schools);
        this.addPawnToBoard(board, color, "T", false);
    }

    // ISLANDS METHODS
    /*
    protected void addStudentToIsland(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, color, "S", false);
    }
    */

    protected void addStudentsToIsland(int id, Color color, int amount) {
        String[][] board = this.findBoardBy(id, this.islands);
        board = this.addStudentsCounterToIsland(board, color, amount);
        this.addPawnToBoard(board, color, "S", false);

    }

    protected void addMotherNatureToIsland(int id) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, Color.RED, "N", false);
    }
    protected void addTowerToIsland(int id, TowerColor color) {
        String[][] board = this.findBoardBy(id, this.islands);
        this.addPawnToBoard(board, color, "T", false);
    }

    // CLOUDS METHODS
    protected void addStudentToCloud(int id, Color color) {
        String[][] board = this.findBoardBy(id, this.clouds);
        this.addPawnToBoard(board, color, "S", true);
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

    private String[][] writeTextInMatrix(String[][] board, String text, int offset_x, int offset_y) {
        board[offset_y][offset_x] = text;
        for (int i=offset_x+1; i<offset_x+text.length(); i++) {
            board[offset_y][i] = "";
        }
        return board;
    }

    private String[][] addStudentsCounterToIsland(String[][] island, Color color, int counter) {
        String label = String.valueOf(counter);
        while (label.length() < 2) {
            label = "0" + label;
        }
        label = "x" + label;
        int offset_x = 5;
        int offset_y = positionMap.get(color) + 1;
        island = this.writeTextInMatrix(island, label, offset_x, offset_y);
        return island;
    }

    private String[][] addNumberToIsland(String[][] island, int id) {
        String label = String.valueOf(id);
        while (label.length() < 2) {
            label = "0" + label;
        }
        int offset_x = 18;
        int offset_y = 1;
        island = this.writeTextInMatrix(island, label, offset_x, offset_y);
        return island;
    }

    private String[][] addCaptionToSchool(String[][] school, String nickname, int coins) {
        String label = String.valueOf(coins);
        while (label.length() < 2) {
            label = "0" + label;
        }
        label = "$:" + label;
        while (label.length() + nickname.length() < school[0].length - 6) {
            label = " " + label;
        }
        label = nickname + label;

        int offset_x = 3;
        int offset_y = school.length - 2;
        school = this.writeTextInMatrix(school, label, offset_x, offset_y);
        return school;
    }

    private String[][] removePlaceholdersFromBoard(String[][] board) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (board[i][j].length() == 1 && ((isLetter(board[i][j].charAt(0)) && Character.isUpperCase(board[i][j].charAt(0))) || isDigit(board[i][j].charAt(0)))) {
                    board[i][j] = " ";
                }
            }
        }
        return board;
    }

    private String boardMatrixToString(String[][] board) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                builder.append(board[i][j]);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String[][] addCharacterCardsToBoard(String[][] board) {
        String cardTitle;
        String cardText;
        int cardNumber = 0;
        int cardTextWidth = 18;
        int cardTextHeight = 13;

        String id;
        String coins;

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (Objects.equals(board[i][j], "$")) {
                    id = String.valueOf(this.characterCards.get(cardNumber).getId());
                    coins = String.valueOf(this.characterCards.get(cardNumber).getCoins());
                    while (id.length() < 2) {
                        id = "0" + id;
                    }
                    while (coins.length() < 2) {
                        coins = "0" + coins;
                    }
                    //cardTitle = "CARD:" + id + "   COINS:" + coins;
                    cardTitle = "CARD:" + id + "       $:" + coins;
                    this.writeTextInMatrix(board, cardTitle, j, i);
                    /*
                    for (int k=0; k<cardTitle.length(); k++) {
                        board[i][j+k] = String.valueOf(cardTitle.charAt(k));
                    }

                     */
                    cardNumber++;
                    if (cardNumber > 2) cardNumber = 0;
                }
                if (Objects.equals(board[i][j], "!")) {
                    cardText = this.characterCards.get(cardNumber).getEffect();
                    Pattern p = Pattern.compile(".{1," + cardTextWidth + "}(\\s+|$)");
                    Matcher m = p.matcher(cardText);
                    ArrayList<String> cardTextLines = new ArrayList<>();
                    while(m.find()) {
                        cardTextLines.add(m.group().trim());
                    }
                    for (int k=0; k<min(cardTextHeight, cardTextLines.size()); k++) {
                        for (int h=0; h<min(cardTextWidth, cardTextLines.get(k).length()); h++) {
                            board[i+k][j+h] = String.valueOf(cardTextLines.get(k).charAt(h));
                        }
                    }
                    cardNumber++;
                    if (cardNumber > 2) cardNumber = 0;
                }
            }
        }
        return board;
    }

    private String getPrintableBoard(String[][] board, boolean removePlaceholders) {
        if (removePlaceholders) {
            this.removePlaceholdersFromBoard(board);
        }
        this.addCharacterCardsToBoard(board);
        return this.boardMatrixToString(board);
    }

    private String[][] addLinkersBetweenIslands(String[][] set, int[][] disposition, int islandH, int islandW, int spaceH, int spaceW) {
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
        return set;
    }

    private String[][] addCharacterCardsInIslandsSet(String[][] set, int[][] disposition) {
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
        return set;
    }

    private String[][] addCloudsToIslandsSet(String[][] set, int[][] disposition) {
        int cloudH = this.clouds.get(0).length;
        int cloudW = this.clouds.get(0)[0].length;
        int cloudVerticalSpace = 1;
        int cloudCounter = 0;
        boolean blankLine = false;
        int cloudRectangleRows = (cloudH + cloudVerticalSpace) * this.clouds.size() - cloudVerticalSpace;
        String[][] cloudsRectangle = new String[cloudRectangleRows][cloudW];
        for (int i=0; i<cloudsRectangle.length; i++) {
            for (int j=0; j<cloudsRectangle[i].length; j++) {
                if (i%(cloudH+cloudVerticalSpace) < cloudH) {
                    if (blankLine) {
                        cloudCounter++;
                        blankLine = false;
                    }
                    cloudsRectangle[i][j] = this.clouds.get(cloudCounter)[i%(cloudH+cloudVerticalSpace)][j];
                } else {
                    cloudsRectangle[i][j] = " ";
                    blankLine = true;
                }
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

    private String[][] buildSetOfIslands() {
        int[][] disposition = {
                {110,   0,      1,      2,      3,      34},
                {11,    -1,     -2,     -3,     -5,     4 },
                {10,    -9,     -9,     -9,     -9,     5 },
                {910,   9,      8,      7,      6,      56}
        };
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
        this.addLinkersBetweenIslands(set, disposition, islandH, islandW, spaceH, spaceW);
        this.addCharacterCardsInIslandsSet(set, disposition);
        this.addCloudsToIslandsSet(set, disposition);
        return set;
    }

    private String[][] addAssistantCardsDeckToGameBoard(String[][] board) {
        // Assistant card
        int cardRowOnSet;
        int cardColOnSet;
        int cardBaseRowOnSet = 39;
        int cardBaseColOnSet = 151;
        String[][] card = this.assistantCardTest;
        int offset = 0;
        int assistantCardIndex = 0;
        String value;
        String steps;

        for (int cardRow=0; cardRow<card.length; cardRow++) {
            for (int cardCol=0; cardCol<card[0].length; cardCol++) {
                if (Objects.equals(card[cardRow][cardCol], "^")) {
                    if (assistantCardIndex < this.assistantCards.size()) {
                        value = String.valueOf(this.assistantCards.get(assistantCardIndex).getValue());
                        steps = String.valueOf(this.assistantCards.get(assistantCardIndex).getSteps());
                        while (value.length() < 2) {
                            value = "0" + value;
                        }
                        while (steps.length() < 2) {
                            steps = "0" + steps;
                        }
                        String label = "VAL:" + value + "   S:" +steps;
                        this.writeTextInMatrix(card, label, 2 + offset, cardRow);
                        offset += label.length() + 3;
                        assistantCardIndex++;
                    } else {
                        String label = "-------------";
                        this.writeTextInMatrix(card, label, 2 + offset, cardRow);
                        offset += label.length() + 3;
                    }
                }
            }
            offset = 0;
        }

        for (int cardRow=0; cardRow<card.length; cardRow++) {
            for (int cardCol=0; cardCol<card[0].length; cardCol++) {
                cardRowOnSet = cardBaseRowOnSet + cardRow;
                cardColOnSet = cardBaseColOnSet + cardCol;
                board[cardRowOnSet][cardColOnSet] = card[cardRow][cardCol];
            }
        }
        return board;
    }

    private String[][] addLastPlayedAssistantCardsToGameBoard(String[][] board) {
        // Assistant card
        int cardRowOnSet;
        int cardColOnSet;
        int cardBaseRowOnSet = 41;
        int cardBaseColOnSet = 0;
        String[][] block = this.lastPlayedAssistantCardsContainer;

        String title = "Last played assistant cards".toUpperCase();
        String label = String.valueOf(this.boardCoins);
        while (label.length() < 2) {
            label = "0" + label;
        }
        label = "$:" + label;
        while (label.length() + title.length() < block[0].length - 6) {
            label = " " + label;
        }
        label = title + label;
        int offset_x = 3;
        int offset_y = 1;
        block = this.writeTextInMatrix(block, label, offset_x, offset_y);


        int offset = 0;
        for (String nickname : this.nicknames) {
            StringBuilder nicknameBuilder = new StringBuilder(nickname);
            while (nicknameBuilder.length() < 12) {
                nicknameBuilder.append(" ");
            }
            nickname = nicknameBuilder.toString();
            block = this.writeTextInMatrix(block, nickname, 2 + offset, 3);
            offset += 16;
        }

        offset = 0;
        int assistantCardIndex = 0;
        String value;
        String steps;

        for (int cardRow=0; cardRow<block.length; cardRow++) {
            for (int cardCol=0; cardCol<block[0].length; cardCol++) {
                if (Objects.equals(block[cardRow][cardCol], "^")) {
                    if (assistantCardIndex < this.lastPlayedAssistantCards.size() && this.lastPlayedAssistantCards.get(assistantCardIndex) != null) {
                        value = String.valueOf(this.lastPlayedAssistantCards.get(assistantCardIndex).getValue());
                        steps = String.valueOf(this.lastPlayedAssistantCards.get(assistantCardIndex).getSteps());
                        while (value.length() < 2) {
                            value = "0" + value;
                        }
                        while (steps.length() < 2) {
                            steps = "0" + steps;
                        }
                        label = "VAL:" + value + "   S:" +steps;
                        this.writeTextInMatrix(block, label, 2 + offset, cardRow);
                        offset += label.length() + 3;
                    } else {
                        label = "-------------";
                        this.writeTextInMatrix(block, label, 2 + offset, cardRow);
                        offset += label.length() + 3;
                    }
                    assistantCardIndex++;
                }
            }
            offset = 0;
        }

        for (int cardRow=0; cardRow<block.length; cardRow++) {
            for (int cardCol=0; cardCol<block[0].length; cardCol++) {
                cardRowOnSet = cardBaseRowOnSet + cardRow;
                cardColOnSet = cardBaseColOnSet + cardCol;
                board[cardRowOnSet][cardColOnSet] = block[cardRow][cardCol];
            }
        }
        return board;
    }


    private String[][] buildGameBoard(String[][] setOfIslands) {
        int space = 4;
        int height = Math.max((setOfIslands.length + this.schools.get(0).length), this.schools.get(1).length);

        int width = setOfIslands[0].length + (this.schools.get(1)[0].length + space) * 2;

        String[][] gameBoard = new String[height][width];
        for (int i=0; i<gameBoard.length; i++) {
            for (int j=0; j<gameBoard[i].length; j++) {
                gameBoard[i][j] = " ";
            }
        }

        int mainSchoolOffset;

        this.addAssistantCardsDeckToGameBoard(gameBoard);
        this.addLastPlayedAssistantCardsToGameBoard(gameBoard);

        for (int i=0; i<gameBoard.length; i++) {
            for (int j=0; j<gameBoard[i].length; j++) {
                if (i < this.schools.get(1).length) {
                    if (j < this.schools.get(1)[i].length) {
                        gameBoard[i][j] = this.schools.get(1)[i][j];
                    } else if (j >= this.schools.get(1)[i].length + space && j - this.schools.get(1)[i].length - space < setOfIslands[0].length && i < setOfIslands.length) {
                        gameBoard[i][j] = setOfIslands[i][j - this.schools.get(1)[i].length - space];
                    } else if (j >= (this.schools.get(1)[i].length + space) + setOfIslands[0].length + space && j - this.schools.get(1)[i].length - space - setOfIslands[0].length - space < this.schools.get(1)[i].length && i < this.schools.get(1).length) {
                        if (this.schools.size() == 3) {
                            gameBoard[i][j] = this.schools.get(2)[i][j - this.schools.get(1)[i].length - space - setOfIslands[0].length - space];
                        } else {
                            gameBoard[i][j] = " ";
                        }
                    }
                }
                mainSchoolOffset = this.schools.get(1)[0].length + space + (setOfIslands[0].length - this.schools.get(0)[0].length) / 2;
                if (i >= setOfIslands.length && j >= mainSchoolOffset && j < setOfIslands[0].length + space + this.schools.get(1)[0].length) {
                    if (j - mainSchoolOffset < this.schools.get(0)[0].length) {
                        gameBoard[i][j] = this.schools.get(0)[i - setOfIslands.length][j - mainSchoolOffset];
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

        AnsiConsole.systemInstall();

        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Print game board
        System.out.println(this.getPrintableBoard(gameBoard, true));

        AnsiConsole.systemUninstall();
    }

}
