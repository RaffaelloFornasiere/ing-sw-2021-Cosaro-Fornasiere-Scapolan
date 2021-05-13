package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class EffectOfPopeCell extends EffectOfCell {


    @Override
    public void activate(CellWithEffect cell, MatchState matchState){
        PopeCell popeCell=(PopeCell)cell;
        int first=cell.getIndex()-popeCell.getVaticanReportSection()+1;

        ArrayList<Player> p=  matchState.getPlayers();
        p.parallelStream()
                .filter(f->(f.getDashBoard().getFaithTrackData().getPosition()>=first))
                .forEach((g)-> g.getDashBoard().getFaithTrackData().addPopeFavorCard(popeCell.getIndex(), popeCell.getCard()));
    }
}
