package it.polimi.ingsw.client.view.cli;


import java.util.HashMap;
import java.util.Map;

public class DemoLauncher {

    public static void main(String[] args) throws Exception {
        /*List<String> nicknames = new ArrayList<>(List.of("player0", "player1", "player2"));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerSerializer());
        gsonBuilder.registerTypeAdapter(Handler.class, new HandlerDeserializer());
        Gson gson = gsonBuilder.create();

        Model model = new ModelBuilder().buildModel(nicknames, true);
        Controller controller = model.getController();

        controller.chooseWizard(0, 0);
        controller.chooseWizard(1, 1);
        controller.chooseWizard(2, 2);

        controller.playAssistantCard(0, 1);
        controller.playAssistantCard(1, 2);
        controller.playAssistantCard(2, 3);

        controller.playCharacterCard(0, 7);


        Path path = Path.of("./saves");
        Files.createDirectory(path);
        File file = new File("saves/model.json");

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(gson.toJson(model));
        fileWriter.close();*/

        Map<Integer, String> map = new HashMap<>();
        String str1 = "pippo";
        String str2 = "pluto";
        String str3 = "cip";
        map.put(1, str1);
        map.put(2, str2);
        map.put(3, str3);
        System.out.println(map.values().stream().sorted().toList());
    }

}
