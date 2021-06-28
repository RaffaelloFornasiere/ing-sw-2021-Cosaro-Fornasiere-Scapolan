package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;

/**
 * Class that represents a leader card
 */
public class LeaderCard extends Observable {

    private final String cardID;
    private final int victoryPoints;
    private final ArrayList<Requirement> activationRequirements;
    private final ArrayList<Pair<LeaderPower, Boolean>> powers;

    /**
     * Constructor for the class
     * @param cardID                 The Id of the card
     * @param victoryPoints          Amount of points awarded to the player at the end of the match if the card is active
     * @param activationRequirements Requirement that must be fulfilled to activate the card
     * @param powers                 Effects of the card
     */
    public LeaderCard(String cardID, int victoryPoints, ArrayList<Requirement> activationRequirements, ArrayList<LeaderPower> powers) {
        this.cardID = cardID;
        this.victoryPoints = victoryPoints;
        this.activationRequirements = new ArrayList<>(activationRequirements);
        this.powers = new ArrayList<>();
        for (LeaderPower lp : powers)
            this.powers.add(new Pair<>(lp, false));
    }

    /**
     * Getter for the ID of the card
     * @return The ID of the card
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * Getter for the victory points
     * @return The value of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter for the requirements
     * @return The requirements
     */
    public ArrayList<Requirement> getActivationRequirement() {
        return new ArrayList<>(activationRequirements);
    }

    /**
     * Getter for all the powers
     * @return The powers of this card
     */
    public ArrayList<LeaderPower> getLeaderPowers() {
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for (Pair<LeaderPower, Boolean> lpo : powers)
            ret.add(lpo.getKey());
        return ret;
    }

    /**
     * Getter for all the powers, with respective selected state
     * @return The powers of this card with respective selected state
     */
    public ArrayList<Pair<LeaderPower, Boolean>> getBooleanPowers() {
        return new ArrayList<>(powers);
    }


    /**
     * Getter for all the selected powers
     * @return The powers of this card
     */
    public ArrayList<LeaderPower> getSelectedLeaderPowers() {
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for (Pair<LeaderPower, Boolean> lpo : powers)
            if (lpo.getValue())
                ret.add(lpo.getKey());
        return ret;
    }

    /**
     * Methods that selects a leader power
     * @param leaderPower The leader power to select
     * @throws NotPresentException If the leader power passed does not belong to the card
     * @throws IllegalOperation    If the leader power was already selected
     */
    public void selectLeaderPower(LeaderPower leaderPower) throws NotPresentException, IllegalOperation {
        for (int i = 0, powersSize = powers.size(); i < powersSize; i++) {
            Pair<LeaderPower, Boolean> lpo = powers.get(i);
            if (leaderPower == lpo.getKey()) {
                if (lpo.getValue()) throw new IllegalOperation("Leader power already selected");
                this.powers.remove(i);
                this.powers.add(i, new Pair<>(lpo.getKey(), !lpo.getValue()));
                notifyObservers();
                return;
            }
        }

        throw new NotPresentException("The selected leader power does not belong to this card");
    }

    /**
     * Methods that deselects a leader power
     * @param leaderPower The leader power to deselect
     * @throws NotPresentException If the leader power passed does not belong to the card
     * @throws IllegalOperation    If the leader power was not selected
     */
    public void deselectLeaderPower(LeaderPower leaderPower) throws NotPresentException, IllegalOperation {
        for (int i = 0, powersSize = powers.size(); i < powersSize; i++) {
            Pair<LeaderPower, Boolean> lpo = powers.get(i);
            if (leaderPower == lpo.getKey()) {
                if (!lpo.getValue()) throw new IllegalOperation("Leader power already not selected");
                this.powers.remove(i);
                this.powers.add(i, new Pair<>(lpo.getKey(), !lpo.getValue()));
                notifyObservers();
                return;
            }
        }

        throw new NotPresentException("The selected leader power does not belong to this card");
    }

    /*public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        try {
            for (int i = 5; i <= 16; i++) {
                String name = "LeaderCard" + i;
                File file = new File("C:\\Users\\Leo\\IdeaProjects\\ing-sw-2021-Cosaro-Fornasiere-Scapolan\\src\\main\\resources\\" + name + ".json");
                FileReader r = new FileReader(file);
                Scanner scanner = new Scanner(r);
                StringBuilder s = new StringBuilder();
                while (scanner.hasNext())
                    s.append(scanner.nextLine());
                DummyLeaderCard c = gson.fromJson(s.toString(), DummyLeaderCard.class);
                ArrayList<LeaderPower> lps = new ArrayList<>();
                for (DummyLeaderPower dlp : c.powers)
                    lps.add(dlp.power);
                LeaderCard lc = new LeaderCard(c.cardID, c.victoryPoints, c.activationRequirements, lps);
                String JSONDevCard = gson.toJson(lc, LeaderCard.class);
                FileWriter w = new FileWriter(file);
                w.write(JSONDevCard);
                w.flush();
                w.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        for(int i=1; i<=16; i++){
            String name = "LeaderCard" + i;
            File file = new File("C:\\Users\\Leo\\IdeaProjects\\ing-sw-2021-Cosaro-Fornasiere-Scapolan\\src\\main\\resources\\" + name + ".json");
            try {
                FileReader r = new FileReader(file);
                Scanner scanner = new Scanner(r);
                StringBuilder s = new StringBuilder();
                while(scanner.hasNext())
                    s.append(scanner.nextLine());
                LeaderCard c = gson.fromJson(s.toString(), LeaderCard.class);
                c.cardID = name;
                String JSONDevCard = gson.toJson(c, LeaderCard.class);
                FileWriter w = new FileWriter(file);
                w.write(JSONDevCard);
                w.flush();
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

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

/*class DummyLeaderCard {
    public String cardID;
    public int victoryPoints;
    public ArrayList<Requirement> activationRequirements;
    public ArrayList<DummyLeaderPower> powers;
}

class DummyLeaderPower {
    public LeaderPower power;
    public boolean selected;
}*/