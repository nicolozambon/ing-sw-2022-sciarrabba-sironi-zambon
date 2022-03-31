package it.polimi.ingsw.controller.round.handler;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.component.Island;
import it.polimi.ingsw.model.component.Professor;
import it.polimi.ingsw.model.component.Tower;
import it.polimi.ingsw.model.component.card.InfluenceCharacterCard;

import java.util.List;

public class InfluenceModifier extends Handler {

    /* Card has this params:
    private int towerInfluence;
    private int extraInfluence;
    private boolean colorWithoutInfluence;
    */

    private InfluenceCharacterCard card;

    public InfluenceModifier(Player player, InfluenceCharacterCard card) {
        super(player);
        this.card = card;
    }

    @Override
    public Player getMostInfluentialPlayerOnIsland(List<Player> otherPlayers, Island island) {
        Player mip = null;
        Tower tower = island.getTower();
        int actualPlayerInfluence = this.card.getExtraInfluence();
        int previousInfluence = 0;
        int influence = 0;

        // In .super() --> TODO add a feature: necessary to return null player if the players has equal influence
        // TODO add colorWithoutInfluence rules


        if (tower != null) {
            if (super.getActualPlayer().equals(tower.getOwner())) {
                influence++;
            }
        }

        for (Player player : otherPlayers) {
            influence = 0;
            if (tower != null) {
                if (player.equals(tower.getOwner())) {
                    influence = influence + this.card.getTowerInfluence();
                }
            }
            for (Professor professor : player.getSchool().getProfessorsTable().getPawns()) {
                influence += island.countStudentsByColor(professor.getColor());
            }
            if (influence > previousInfluence) {
                mip = player;
            }
            previousInfluence = influence;
        }

        for (Professor professor : super.getActualPlayer().getSchool().getProfessorsTable().getPawns()) {
            actualPlayerInfluence += island.countStudentsByColor(professor.getColor());
        }
        if (actualPlayerInfluence > influence) {
            mip = super.getActualPlayer();
        }

        return mip;
    }
}
