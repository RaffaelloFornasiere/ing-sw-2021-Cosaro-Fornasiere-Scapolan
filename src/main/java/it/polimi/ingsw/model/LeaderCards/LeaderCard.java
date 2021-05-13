package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that represents a leader card
 */
public class LeaderCard implements Serializable {

    private int victoryPoints;
    private ArrayList<Requirement> activationRequirements;
    private ArrayList<LeaderPowerOwnership> powers;

    private class LeaderPowerOwnership{
        protected LeaderPower power;
        protected boolean selected;
    }

    /**
     * Constructor for the class
     * @param victoryPoints amount of points awarded to the player at the end of the match if the card is active
     * @param activationRequirements requirement that must be fulfilled to activate the card
     * @param powers effects of the card
     */
    public LeaderCard(int victoryPoints, ArrayList<Requirement> activationRequirements, ArrayList<LeaderPower> powers){
        this.victoryPoints = victoryPoints;
        this.activationRequirements = (ArrayList<Requirement>)activationRequirements.clone();
        this.powers = new ArrayList<>();
        LeaderPowerOwnership lpo;
        for(LeaderPower lp: powers){
            lpo = new LeaderPowerOwnership();
            lpo.power = lp;
            lpo.selected = false;
            this.powers.add(lpo);
        }
    }

    /**
     * getter for the victory points
     * @return the value of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * getter for the requirements
     * @return the requirements
     */
    public ArrayList<Requirement> getActivationRequirement() {
        return (ArrayList<Requirement>)activationRequirements.clone();
    }

    /**
     * getter for all the powers
     * @return the powers of this card
     */
    public ArrayList<LeaderPower> getLeaderPowers() {
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for(LeaderPowerOwnership lpo: powers)
            ret.add(lpo.power);
        return ret;
    }

    /**
     * getter for all the selected powers
     * @return the powers of this card
     */
    public ArrayList<LeaderPower> getSelectedLeaderPowers(){
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for(LeaderPowerOwnership lpo: powers)
            if(lpo.selected)
                ret.add(lpo.power);
        return ret;
    }

    /**
     * Methods that selects a leader power
     * @param leaderPower the leader power to select
     * @throws NotPresentException if the leader power passed does not belong to the card
     * @throws IllegalOperation if the leader power was already selected
     */
    public void selectLeaderPower(LeaderPower leaderPower) throws NotPresentException, IllegalOperation {
        for (LeaderPowerOwnership lpo : powers) {
            if (leaderPower == lpo.power) {
                if (lpo.selected) throw new IllegalOperation("Leader power already selected");
                lpo.selected = true;
                return;
            }
        }

        throw new NotPresentException("The selected leader power does not belong to this card");
    }

    /**
     * Methods that deselects a leader power
     * @param leaderPower the leader power to deselect
     * @throws NotPresentException if the leader power passed does not belong to the card
     * @throws IllegalOperation if the leader power was not selected
     */
    public void deselectLeaderPower(LeaderPower leaderPower) throws NotPresentException, IllegalOperation {
        for (LeaderPowerOwnership lpo : powers) {
            if (leaderPower == lpo.power) {
                if (!lpo.selected) throw new IllegalOperation("Leader power already not selected");
                lpo.selected = false;
                return;
            }
        }

        throw new NotPresentException("The selected leader power does not belong to this card");
    }

    /*public static void main(String[] args) {
        //index and victory points
        int lcIndex = 64;
        int vp = 4;

        //requirements
        //Requirement r1 = new LevellessCardRequirement(CardColor.VIOLET, 2);
        //Requirement r2 = new LevellessCardRequirement(CardColor.GREEN, 1);
        //HashMap<Resource, Integer> rr = new HashMap<>();
        //rr.put(Resource.SHIELD, 5);
        //Requirement r3 = new ResourcesRequirement(rr);
        Requirement r4 = new LevelCardRequirement(CardColor.GREEN, 2, 1);

        ArrayList<Requirement> r = new ArrayList<>();
        //r.add(r1);
        //r.add(r2);
        //r.add(r3);
        r.add(r4);

        //power
        //HashMap<Resource, Integer> discount = new HashMap<>();
        //discount.put(Resource.COIN, 1);
        //LeaderPower lp1 = new DiscountLeaderPower(discount);
        //HashMap<Resource, Integer> storage = new HashMap<>();
        //storage.put(Resource.COIN, 2);
        //LeaderPower lp2 = new DepositLeaderPower(storage);
        //LeaderPower lp3 = new ExtraResourceLeaderPower(Resource.COIN);
        HashMap<Resource, Integer> productionRequirement = new HashMap<>();
        productionRequirement.put(Resource.COIN, 1);
        LeaderPower lp4 = new ProductionLeaderPower(new ProductionPower(productionRequirement, new HashMap<>(), 0, 1, 1));

        ArrayList<LeaderPower> lp = new ArrayList<>();
        //lp.add(lp1);
        //lp.add(lp2);
        //lp.add(lp3);
        lp.add(lp4);

        //creation and save
        LeaderCard lc = new LeaderCard(vp, r, lp);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();

        String JSONLeaderCard = gson.toJson(lc);

        System.out.println(JSONLeaderCard);

        File file = new File("C:\\Users\\Leo\\IdeaProjects\\ing-sw-2021-Cosaro-Fornasiere-Scapolan\\src\\main\\resources\\LeaderCard" + lcIndex + ".json");
        try {
            FileWriter w = new FileWriter(file);
            w.write(JSONLeaderCard);
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
