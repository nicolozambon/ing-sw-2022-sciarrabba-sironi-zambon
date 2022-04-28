package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class OptionStringifier {

    private Map<String, String> dictionary;

    public OptionStringifier() {
        String path = "src/main/resources/config/options_stringifier.json";
        Gson gson = new Gson();
        try {
            this.dictionary = gson.fromJson(new FileReader(path), new TypeToken<Map<String, String>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String stringify(Map<Integer, String> options) {
        StringBuilder string = new StringBuilder();
        for (Integer i : options.keySet()) {
            string.append("\n").append(i).append(". ").append(dictionary.get(options.get(i)));
        }
        return string.toString();
    }

    public String stringify() {
        StringBuilder string = new StringBuilder();
        for (String s : dictionary.keySet()) {
            string.append("\n").append(dictionary.get(s));
        }
        return string.toString();
    }
}
