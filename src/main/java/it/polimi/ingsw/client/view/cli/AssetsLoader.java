package it.polimi.ingsw.client.view.cli;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AssetsLoader {

    /**
     * Path of the folder of the assets
     */
    final String FOLDER_PATH = "/assets/cli/";

    /**
     * Read the matrix from a file
     * @param filename filename of the file
     * @return the matrix of the requested file
     */
    private String[][] readMatrixFromFile(String filename) {
        StringBuilder builder = new StringBuilder();
        String string;
        InputStream is = getClass().getResourceAsStream(FOLDER_PATH + filename + ".txt");
        Scanner reader = new Scanner(is, "UTF-8");
        while (reader.hasNextLine()) {
            String row = reader.nextLine();
            row += '\n';
            builder.append(row.replace('?', ' '));
        }
        reader.close();
        string = builder.toString();
        int colsNumber = string.indexOf("\n");
        string = string.replace("\n", "");
        int rowsNumber = string.length() / colsNumber;
        String[][] matrix = new String[rowsNumber][colsNumber];
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < colsNumber; j++) {
                matrix[i][j] = String.valueOf(string.charAt(i*colsNumber+j));
            }
        }
        return matrix;
    }

    /**
     * Getter for island
     * @return matrix of an island
     */
    protected String[][] getIsland() {
        return this.readMatrixFromFile("island");
    }

    /**
     * Getter for island linkers
     * @return map that links each identifier with its matrix
     */
    protected Map<String, String[][]> getIslandLinkers() {
        Map<String, String[][]> linkers = new HashMap<>();
        linkers.put("tr", this.readMatrixFromFile("islands_linkers/top_right"));
        linkers.put("tl", this.readMatrixFromFile("islands_linkers/top_left"));
        linkers.put("br", this.readMatrixFromFile("islands_linkers/bottom_right"));
        linkers.put("bl", this.readMatrixFromFile("islands_linkers/bottom_left"));
        linkers.put("hr", this.readMatrixFromFile("islands_linkers/horizontal"));
        linkers.put("vr", this.readMatrixFromFile("islands_linkers/vertical"));
        return linkers;
    }

    /**
     * Getter for CharacterCardContainer
     * @return matrix of CharacterCardContainer
     */
    protected String[][] getCharacterCardContainer() {
        return this.readMatrixFromFile("character_card");
    }

    /**
     * Getter for AssistantCardContainer
     * @return matrix of AssistantCardContainer
     */
    protected String[][] getAssistantCardContainer() {
        return this.readMatrixFromFile("assistant_card");
    }

    /**
     * Getter for last played AssistantCards
     * @return matrix of last played AssistantCards
     */
    protected String[][] getLastPlayedAssistantCards() {
        return this.readMatrixFromFile("last_played_assistant_cards");
    }

    /**
     * Getter of cloud
     * @return matrix of cloud
     */
    protected String[][] getCloud() {
        return this.readMatrixFromFile("cloud");
    }

    /**
     * Getter for school
     * @param rotated if vertical true else false
     * @return matrix of school
     */
    protected String[][] getSchool(boolean rotated) {
        if (!rotated) {
            return this.readMatrixFromFile("school");
        } else {
            return this.readMatrixFromFile("school_vertical");
        }
    }

}
