package it.polimi.ingsw.client.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AssetsLoader {

    final String FOLDER_PATH = "src/main/resources/assets/cli/";

    private String[][] readMatrixFromFile(String filename) {
        StringBuilder builder = new StringBuilder();
        String string = null;
        try {
            File file = new File(this.FOLDER_PATH + filename + ".txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String row = reader.nextLine();
                row += '\n';
                builder.append(row.replace('?', ' '));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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

    protected String[][] getIsland() {
        return this.readMatrixFromFile("island");
    }

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

    protected String[][] getSchool(boolean rotated) {
        if (!rotated) {
            return this.readMatrixFromFile("school");
        } else {
            return this.readMatrixFromFile("school_vertical");
        }
    }

}