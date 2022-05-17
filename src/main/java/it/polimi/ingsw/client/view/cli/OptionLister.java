package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class OptionLister {

    private Map<String, String> dictionary;

    public OptionLister() {
        Gson gson = new Gson();
        InputStream inputStream = getClass().getResourceAsStream("/config/options_listing.json");
        this.dictionary = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<Map<String, String>>(){}.getType());
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
