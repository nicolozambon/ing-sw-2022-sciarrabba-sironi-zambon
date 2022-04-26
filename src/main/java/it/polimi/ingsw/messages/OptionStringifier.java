package it.polimi.ingsw.messages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public String stringify(Map<String, Integer> options) {
        String string = "";
        int i = 1;
        for (String s : options.keySet()) {
            if (options.get(s) > 0) {
                string = string + "\n" + i + ". " + dictionary.get(s);
                i++;
            }
        }
        return string;
    }

    public String stringify() {
        String string = "";
        for (String s : dictionary.keySet()) {
            string = string + "\n" + dictionary.get(s);
        }
        return string;
    }
}
