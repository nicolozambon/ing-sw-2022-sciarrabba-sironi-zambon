package it.polimi.ingsw.client.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AssetsLoader {

    final String FOLDER_PATH = "src/main/resources/assets/cli/";

    private Character[][] readMatrixFromFile(String filename) {
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
        Character[][] matrix = new Character[rowsNumber][colsNumber];
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < colsNumber; j++) {
                matrix[i][j] = string.charAt(i*colsNumber+j);
            }
        }
        return matrix;
    }

    protected Character[][] getIsland() {
        return this.readMatrixFromFile("island");
    }

    protected Map<String, Character[][]> getIslandLinkers() {
        Map<String, Character[][]> linkers = new HashMap<>();
        linkers.put("tr", this.readMatrixFromFile("pseudo_island_tr"));
        return linkers;
    }

    protected Character[][] getSchool(boolean rotated) {
        if (!rotated) {
            return this.readMatrixFromFile("school");
        } else {
            return this.readMatrixFromFile("school_vertical");
        }
    }

}
