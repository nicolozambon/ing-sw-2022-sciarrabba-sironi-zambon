package it.polimi.ingsw.client.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class OptionLister {

    private Map<String, String> dictionary;

    public OptionLister() {
        String path = "src/main/resources/config/options_listing.json";
        Gson gson = new Gson();
        try {
            this.dictionary = gson.fromJson(new FileReader(path), new TypeToken<Map<String, String>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String list(List<String> options) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (String string : options) {
            stringBuilder.append("\n").append(i).append(". ").append(dictionary.get(string));
            i++;
        }
        return stringBuilder.toString();
    }

    public String list(String option) {
        return dictionary.get(option);
    }

    public String list() {
        StringBuilder string = new StringBuilder();
        for (String s : dictionary.keySet()) {
            string.append("\n").append(dictionary.get(s));
        }
        return string.toString();
    }
}
