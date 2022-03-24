package it.polimi.ingsw.model.component;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.TowerColor;

public class Tower extends Pawn<TowerColor>{

    public Tower (TowerColor color){
        super(color);
    }

}
