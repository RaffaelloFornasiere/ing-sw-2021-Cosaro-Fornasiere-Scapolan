package it.polimi.ingsw.model;

import it.polimi.ingsw.model.FaithTrack.FaithTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class DashBoard {
    private HashMap<Resource, Integer> strongBox;
    private Stack<Integer>[] cardSlots;
    private  ArrayList<Depot> warehouse;
    //private ProductionPower personalPower;
    private FaithTrack faithTrack;
}
