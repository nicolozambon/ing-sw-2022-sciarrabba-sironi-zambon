package it.polimi.ingsw.client.view;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.*;

import it.polimi.ingsw.model.Model;

public class ModelView implements Serializable {
    private static final long serialVersionUID = 987654321L;
    private Model model;



    public ModelView(Model model) {

    }

    //Informazioni del model -> hashmap <Island, HashMap<Color, numStudents>>



    public void readModelSerialized (ByteArrayOutputStream os) throws IOException, ClassNotFoundException {

    }
}
