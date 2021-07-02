package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.clientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.events.clientEvents.FinalPlayerState;
import it.polimi.ingsw.events.controllerEvents.matchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.faithTrack.PopeFavorCard;
import it.polimi.ingsw.model.leaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.Pair;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CLI extends UI {
    private static final PrintWriter out = new PrintWriter(System.out, true);
    private static final Scanner in = new Scanner(System.in);

    private FaithTrackView faithTrack;
    private final HashMap<String, PlayerState> playerStates;
    private ArrayList<String> players;
    private DevCardGridView devCardGridView;
    private MarketView market;


    public CLI() {
        players = new ArrayList<>();
        playerStates = new HashMap<>();
    }

    /**
     * Method used to ask the user to confirm their choices after some selection form. It filters affirmative answers of any kind( "Y", "yes"..)
     *
     * @param s The input from the user
     * @return True or false according to the answer of the user.
     */
    public static boolean isAffirmative(String s) {
        return s.equalsIgnoreCase("YES") ||
                s.equalsIgnoreCase("Y");
    }

    /**
     * Method to display a form to ask the user to make some selection among different choices of any kind.
     *
     * @param option_itsColor        The array of pairs of title of the option(String)- color the option in displayed with(String).
     * @param displayPanel           The optional panel may display additional information which might help the user taking the decision about the options.
     * @param numberOfOptionsToChose The exact number of options the user can select.
     * @param message                Additional massage to include in the selection form, to tell the user what the selection is about.
     * @return The array of indexes referring to the selected items in the array of options.
     */
    public static ArrayList<Integer> displaySelectionForm(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int numberOfOptionsToChose, String message) {
        if (numberOfOptionsToChose <= 0) return new ArrayList<>();
        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE ").append(numberOfOptionsToChose).append(" OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(obj.getWidth() + obj.getX() + 1, obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        //  try {
        while (m > 0) {
            inputString = in.nextLine();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS");
                } else if (input < 1 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else {
                    inputs.add(input);
                    m--;
                }
            } else {
                out.println("PLEASE INSERT A NUMBER");
            }
        }
//        }catch(IndexOutOfBoundsException e){
//            System.out.println(" ");
//        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();
        inputs = inputs.stream().map(integer -> integer - 1).collect(Collectors.toCollection(ArrayList::new));

        inputs.forEach(index -> builder2.append(index + 1).append(" -> ").append(option_itsColor.get(index).getValue()).append(option_itsColor.get(index).getKey()).append(Color.reset()).append("\n"));
        out.println(builder2);

        out.println("DO YOU AGREE? yes/no");

        String answer = in.nextLine().toUpperCase();
        if (isAffirmative(answer)) {
            return inputs;
        } else {
            return displaySelectionForm(option_itsColor, displayPanel, numberOfOptionsToChose, message);
        }

    }

    /**
     * Method used to generate a String to display a set of options contained in cells of variable height.
     *
     * @param option_itsColor The array of pairs of title of the option(String)- color the option in displayed with(String).
     * @param resetColor      The default color.
     * @return The string to print for the selection.
     */
    private static String generateForm(ArrayList<Pair<String, String>> option_itsColor, String resetColor) {
        StringBuilder builderTop = new StringBuilder();
        StringBuilder builderBot = new StringBuilder();
        StringBuilder builderEnd = new StringBuilder();

        int maxLines = option_itsColor.stream().map(Pair::getKey).map(s -> s.chars().filter(ch -> ch == '\n').map(ch -> 1).sum()).max(Integer::compareTo).orElse(-1) + 1;
        ArrayList<StringBuilder> buildersMid = new ArrayList<>();
        for (int i = 0; i < maxLines; i++) {
            buildersMid.add(new StringBuilder());
        }

        AtomicInteger n = new AtomicInteger(1);
        option_itsColor.forEach(option -> {
            String[] rows = option.getKey().split("\n");
            int maxLength = Arrays.stream(rows).map(String::length).max(Integer::compare).orElse(0);

            builderTop.append(resetColor).append("╔═").append(resetColor);
            IntStream.range(0, maxLength).forEach(letter -> builderTop.append("═"));
            builderTop.append(resetColor).append("═╗  ");

            String color = option.getValue();
            for (int i = 0; i < maxLines; i++) {
                buildersMid.get(i).append("║ ").append(color);
                if (i < rows.length) {
                    buildersMid.get(i).append(rows[i]);
                    for (int j = 0; j < maxLength - rows[i].length(); j++) {
                        buildersMid.get(i).append(" ");
                    }
                } else {
                    for (int j = 0; j < maxLength; j++) {
                        buildersMid.get(i).append(" ");
                    }
                }
                buildersMid.get(i).append(resetColor).append(" ║  ");
            }

            builderBot.append("╚═").append(resetColor);
            IntStream.range(0, maxLength).forEach(letter -> builderBot.append("═"));
            builderBot.append(resetColor).append("═╝  ");

            builderEnd.append("  ").append(resetColor);
            builderEnd.append(n.get());
            IntStream.range(0, maxLength - 1).forEach(letter -> builderEnd.append(" "));
            builderEnd.append(resetColor).append("    ");
            n.getAndIncrement();
        });
        builderTop.append('\n');
        for (StringBuilder stringBuilder : buildersMid) {
            builderTop.append(stringBuilder).append('\n');
        }
        builderTop.append(builderBot).append('\n');
        builderTop.append(builderEnd);

        return builderTop.toString();
    }

    /**
     * Method to display a form to ask the user to make some selection among different choices of any kind.
     *
     * @param option_itsColor           The array of pairs of title of the option(String)- color the option in displayed with(String).
     * @param displayPanel              The optional panel may display additional information which might help the user taking the decision about the options.
     * @param maxNumberOfOptionsToChose The maximum number of options the user can select.
     * @param message                   Additional massage to include in the selection form, to tell the user what the selection is about.
     * @return The array of indexes referring to the selected items in the array of options.
     */
    public static ArrayList<Integer> displaySelectionFormVariableChoices(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int maxNumberOfOptionsToChose, String message) {
        if (maxNumberOfOptionsToChose <= 0) return new ArrayList<>();
        String resetColor = Color.reset();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE AT MOST ").append(maxNumberOfOptionsToChose).append(" OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(obj.getWidth() + obj.getX() + 1, obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
        int m = maxNumberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS. ENTER '0' OR 'STOP' TO STOP ");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
            inputString = in.next();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS ");
                } else if (input < 0 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 0 AND " + size);
                } else if (input == 0) {
                    m = 0;
                } else {
                    inputs.add(input);
                    m--;
                }
            } else {
                if (inputString.equalsIgnoreCase("STOP")) m = 0;
                else out.println("PLEASE INSERT A NUMBER OR 'STOP'");
            }
        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();
        inputs = inputs.stream().map(integer -> integer - 1).collect(Collectors.toCollection(ArrayList::new));

        inputs.forEach(index -> builder2.append(index + 1).append(" -> ").append(option_itsColor.get(index).getValue()).append(option_itsColor.get(index).getKey()).append(Color.reset()).append("\n"));
        out.println(builder2);

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(response)) {
            return inputs;
        } else {
            return displaySelectionFormVariableChoices(option_itsColor, displayPanel, maxNumberOfOptionsToChose, message);
        }

    }

    /**
     * Method to display a form to ask the user to make some selection among different choices of any kind,where the user can choose the same option multiple times.
     *
     * @param option_itsColor        The array of pairs of title of the option(String)- color the option in displayed with(String).
     * @param displayPanel           The optional panel may display additional information which might help the user taking the decision about the options.
     * @param numberOfOptionsToChose The maximum number of options the user can select.
     * @param message                Additional massage to include in the selection form, to tell the user what the selection is about.
     * @return The array of indexes referring to the selected items in the array of options.
     */
    public static ArrayList<Integer> displaySelectionFormMultipleChoices(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int numberOfOptionsToChose, String message) {
        if (numberOfOptionsToChose <= 0) return new ArrayList<>();
        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE ").append(numberOfOptionsToChose).append(" OF THE FOLLOWING OPTIONS \n(YOU CAN CHOOSE THE SAME OPTION MULTIPLE TIMES) \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(obj.getWidth() + obj.getX() + 1, obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
            inputString = in.next();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (input < 1 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else {
                    inputs.add(input);
                    m--;
                }
            } else {
                out.println("PLEASE INSERT A NUMBER");
            }
        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();
        inputs = inputs.stream().map(integer -> integer - 1).collect(Collectors.toCollection(ArrayList::new));

        inputs.forEach(index -> builder2.append(index + 1).append(" -> ").append(option_itsColor.get(index).getValue()).append(option_itsColor.get(index).getKey()).append(Color.reset()).append("\n"));
        out.println(builder2);

        out.println("DO YOU AGREE? yes/no");
        String answer = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(answer)) {
            return inputs;
        } else {
            return displaySelectionForm(option_itsColor, displayPanel, numberOfOptionsToChose, message);
        }

    }


    /**
     * Method to translate a  production power information into a string to print.
     *
     * @param productionPower The production power to visualize
     * @param color           The visualization color.
     * @return The string to print.
     */
    public static String productionPowerToString(ProductionPower productionPower, String color) {
        StringBuilder build = new StringBuilder();
        build.append(color).append("╔════").append(color).append(color).append("═════════╗").append(Color.reset()).append("\n");
        for (Resource resource : productionPower.getConsumedResources().keySet()) {
            build.append(color).append("║     ").append(CLI.colorResource(resource)).append(productionPower.getConsumedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (productionPower.getRequiredResourceOfChoice() != 0) {
            build.append(color).append("║     ").append(color).append(+productionPower.getRequiredResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
        }
        build.append(color).append("║   ").append(color).append("--->>> ").append(color).append("   ║ ").append(Color.reset()).append("\n");
        for (Resource resource : productionPower.getProducedResources().keySet()) {
            build.append(color).append("║     ").append(CLI.colorResource(resource)).append(productionPower.getProducedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (productionPower.getFaithPointsProduced() != 0) {
            build.append(color).append("║     ").append(color).append(+productionPower.getFaithPointsProduced()).append(" ").append("+").append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (productionPower.getProducedResourceOfChoice() != 0) {
            build.append(color).append("║     ").append(color).append(+productionPower.getProducedResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
        }
        build.append(color).append("╚════").append(color).append(color).append("═════════╝").append(Color.reset()).append("\n");
        return build.toString();
    }

    /**
     * method used to translate some HashMap of <Resources, Integer> into a string for visualization purposes.
     *
     * @param hashMapToDisplay The hashmap to visualize.
     * @return The string to print.
     */
    public String displayResourcesInHashMap(HashMap<Resource, Integer> hashMapToDisplay) {
        if (!hashMapToDisplay.isEmpty()) {
            StringBuilder builder0 = new StringBuilder();
            hashMapToDisplay.keySet().forEach(resource -> {
                String color = colorResource(resource);
                String shape = shapeResource(resource);
                builder0.append(color).append(resource.toString()).append(": ");
                IntStream.range(0, hashMapToDisplay.get(resource)).forEach(n -> builder0.append(color).append(shape).append(" "));
                builder0.append(Color.WHITE.getAnsiCode()).append(" --> ").append(hashMapToDisplay.get(resource)).append("\n");
            });
            return builder0.toString();
        } else return "Empty";
    }

    /**
     * Method used to display the available places where resources can be put.They include warehouse and the extra deposits of the selected leaderPowers in the active leaderCards.
     *
     * @param thisDashboard The Dashboard to which the deposits belong.
     */
    public void displayAvailableDeposits(DashBoardView thisDashboard) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n");
        builder.append("THESE ARE THE AVAILABLE PLACES \n:").append(thisDashboard.warehouseToString()).append("\n\n");
        playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(LeaderCardView::isActive).forEach(card -> builder.append(card.depositPowersToString()));
        DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
        Panel panel = new Panel(obj.getWidth() + obj.getX() + 1, obj.getHeight() + 2, out);
        panel.addItem(obj);
        panel.show();
    }

    /**
     * Method to obtain from the user all the information about were to store resources newly gained.
     *
     * @param resourcesToOrganize The hashmap containing kind of resource-quantity which must be organized.
     * @return The event to send to the controller, containing all the info about the new organization of resources in deposits.
     */
    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resourcesToOrganize) {
        //Parameters to giveback to the NewResourcesOrganizationEvent
        ArrayList<DepotState> depotStates;
        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState = new ArrayList<>();
        HashMap<Resource, Integer> discardedResources = new HashMap<>();
        //-----------------------------------------------------
        resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
        DashBoardView thisDashboard = playerStates.get(thisPlayer).getDashBoard();
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = thisDashboard.getWarehouse();
        //Initially the depotState to give back to model is the current state of this dashboard.
        depotStates = currentDepotStates;
        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = new ArrayList<>(playerStates.get(thisPlayer).getLeaderCards().values());
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        leaderCardViews.forEach(cardView -> {
            if (cardView.isActive()) {
                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
                    if (power instanceof DepositLeaderPower)
                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
                });
            }
        });
        // Initially the DepositLeaderPower states to give back to the model are the current states of leaderPower.

        //display the resources available for storing
        StringBuilder builder0 = new StringBuilder();
        int totalNumberOfResourcesToOrganize = resourcesToOrganize.values().stream().reduce(0, Integer::sum);
        if (totalNumberOfResourcesToOrganize == 0) {
            out.println("THERE ARE NO RESOURCES TO STORE");
        } else {
            displayAvailableDeposits(thisDashboard);
            builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
            builder0.append(displayResourcesInHashMap(resourcesToOrganize));
            out.println(builder0);
            System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE D FOR 'DONE' OTHERWISE TYPE C FOR 'CONTINUE'");
        }
        if (totalNumberOfResourcesToOrganize == 0 || in.next().equalsIgnoreCase("D")) {
            //GENERATE NEW NewResourcesOrganizationEvent
            out.println("Done");


            thisDepositLeaderPowers.forEach(infoPower -> leaderPowersState.add(new DepositLeaderPowerStateEvent(thisPlayer, infoPower.getCardId(), infoPower.powerIndex, infoPower.getPower().getCurrentResources())));

            return new NewResourcesOrganizationEvent(thisPlayer, depotStates, leaderPowersState, discardedResources);

        } else {
            // prepare panel to display current state of all deposits.

            //this is the moment where the user is asked if they want to switch depot.
            boolean allEmpty = depotStates.stream().map(depot -> depot.getCurrentQuantity() == 0).reduce(true, (prev, next) -> (prev && next));
            if (!allEmpty) {
                out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
                String y_n = in.next();
                while (y_n.equalsIgnoreCase("Y")) {
                    int count = 2;
                    int[] indexes = {-1, -1};
                    DepotResultMessage done;

                    out.println("WHICH DEPOTS? INSERT TWO NUMBERS");
                    while (count > 0) {
                        String inp = in.next();
                        if (!inp.matches("-?\\d+")) {
                            out.println("YOU HAVE TO INSERT NUMBERS");
                            continue;
                        }
                        int chosenDepotIndex = Integer.parseInt(inp);
                        if (chosenDepotIndex - 1 < 0 || chosenDepotIndex > depotStates.size()) {
                            out.println("THIS DEPOT DOESN'T EXIST. INSERT A NUMBER BETWEEN 1 AND " + depotStates.size());
                            continue;
                        }
                        indexes[2 - count] = chosenDepotIndex - 1;
                        count--;

                    }
                    out.println(indexes[0] + " " + indexes[1]);
                    done = thisDashboard.switchDepots(indexes[0], indexes[1]);
                    out.println(done.getMessage());
                    out.println(thisDashboard.warehouseToString());
                    out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
                    y_n = in.next();
                }
            }

            // select one resource at a time among those available
            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
            ArrayList<Resource> justResources = new ArrayList<>();

            resourcesToOrganize.keySet().forEach(resType -> IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
                resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
                justResources.add(resType);
            }));
            ArrayList<Integer> inputResources = new ArrayList<>();
            if (totalNumberOfResourcesToOrganize == 1) {
                String white = Color.WHITE.getAnsiCode();
                out.println("YOU HAVE JUST ONE RESOURCE TO STORE: \n" +
                        white + "╔" + white + "═══" + white + "╗\n" +
                        white + "║ " + colorResource(justResources.get(0)) + shapeResource(justResources.get(0)) + white + " ║\n" +
                        white + "╚" + white + "═══" + white + "╝");
                inputResources.add(0);
                // the chosen resource needs to be assigned to one of the deposits
            } else {
                String s = "YOU HAVE " + totalNumberOfResourcesToOrganize + " AVAILABLE FOR STORING:\n";
                // the chosen resource needs to be assigned to one of the deposits
                inputResources = displaySelectionFormVariableChoices(resourcesOptions, null, totalNumberOfResourcesToOrganize, s);
            }

            HashMap<Resource, Integer> resInHashMap = new HashMap<>();
            // organize resources in hashmap
            inputResources.stream().map(justResources::get).forEach(res -> {
                if (resInHashMap.containsKey(res)) resInHashMap.put(res, resInHashMap.get(res) + 1);
                else resInHashMap.put(res, 1);
            });
            //System.out.println("YOU HAVE TO STORE THESE RESOURCES:  ( " + builder.toString() + Color.reset() + " )?");
            resInHashMap.forEach((res, integer) -> {
                String s = colorResource(res) + integer + " " + res.toString() + " " + shapeResource(res) + Color.reset();
                System.out.println("WHERE TO PUT THESE:  ( " + s + " )?");
                // prepare the selection form for deposits.
                ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
                IntStream.range(0, currentDepotStates.size()).forEach(n -> {
                    if (currentDepotStates.get(n).getCurrentQuantity() == 0)
                        depositOptions.add(new Pair<>("DEPOT " + (n + 1), Color.WHITE.getAnsiCode()));
                    else
                        depositOptions.add(new Pair<>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType())));
                });
                IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> depositOptions.add(new Pair<>("LEADER POWER " + (n + 1), Color.WHITE.getAnsiCode())));

                //result of transition
                AtomicBoolean successful = new AtomicBoolean(false);
                //inputDeposit is the result of the selection of one destination among deposits
                int inputDeposit = displaySelectionForm(depositOptions, null, 1, "THESE ARE THE AVAILABLE DEPOSITS\n").get(0);
                //if the selected index belongs to depots...
                if (inputDeposit < currentDepotStates.size()) {
                    DepotResultMessage result = thisDashboard.tryAddResource(res, integer, currentDepotStates.get(inputDeposit));
                    successful.set(result.getSuccessful());
                    out.println(result.getMessage());
                }
                //or if it belongs to leader card extra depots...
                else {
                    int index = inputDeposit - currentDepotStates.size();

                    leaderCardViews.forEach(cardView -> {
                        if (cardView.getIdCard().equals(thisDepositLeaderPowers.get(index).getCardId())) {
                            DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), res, integer);
                            successful.set(result.getSuccessful());
                            out.println(result.getMessage());
                        }
                    });
                }
                if (successful.get()) {
                    resourcesToOrganize.put(res, (resourcesToOrganize.get(res) - integer));
                }
            });

            return getWarehouseDisplacement(resourcesToOrganize);
        }


    }

    /**
     * Utility class useful to gather information about one Leader Power. It is used in the getWarehouseDisplacement() method.
     */
    private static class InfoPower {
        private final DepositLeaderPower power;
        private final String cardId;
        private final int powerIndex;

        /**
         * Constructor
         *
         * @param power      The leader power in question.
         * @param cardId     The Id of the card containing this leader power.
         * @param powerIndex The index in the array of leader powers in this card.
         */
        public InfoPower(DepositLeaderPower power, String cardId, int powerIndex) {
            this.power = power;
            this.powerIndex = powerIndex;
            this.cardId = cardId;
        }

        /**
         * getter
         *
         * @return The leader power
         */
        public DepositLeaderPower getPower() {
            return power;
        }

        /**
         * getter
         *
         * @return The card ID
         */
        public String getCardId() {
            return cardId;
        }

        /**
         * getter
         *
         * @return The power index
         */
        public int getPowerIndex() {
            return powerIndex;
        }


    }

    /**
     * Utility method which associates to each kind of Resource a color.
     *
     * @param res The resource typ.
     * @return the color in ansiCode.
     */
    public static String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.MAGENTA.getAnsiCode();
        if (res == Resource.SHIELD) return Color.BLUE.getAnsiCode();
        if (res == Resource.COIN) return Color.YELLOW.getAnsiCode();
        else return Color.GREY.getAnsiCode();
    }

    public static String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }

    /**
     * Method to print a generic message.
     *
     * @param message The message
     */
    @Override
    public void printMessage(String message) {
        out.println(message);
    }

    /**
     * Method to print an error.
     *
     * @param error The error.
     */
    @Override
    public void printError(String error) {
        out.println(Color.RED.getAnsiCode() + error + Color.reset());
    }

    /**
     * Method to print a warning.
     *
     * @param warning The warning.
     */
    @Override
    public void printWarning(String warning) {
        out.println(Color.YELLOW.getAnsiCode() + warning + Color.reset());
    }


    @Override
    public boolean askSingleplayer() {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        options.add(new Pair<>("Multiplayer", Color.reset()));
        options.add(new Pair<>("Singleplayer", Color.reset()));

        Integer choice = displaySelectionForm(options, null, 1, "WHICH MODE WOULD YOU LIKE TO PLAY?").get(0);

        return choice == 1;
    }

    /**
     * Method to ask the user  the Ip in the initialization fase.
     *
     * @return The address.
     */
    @Override
    public InetAddress askIP() {
        InetAddress inetAddress = null;
        while (inetAddress == null) {
            out.println("Insert the server IP address");
            String IP = in.nextLine();

            if (IP.equals("0")) //TODO only for testing purpose, must be removed
                IP = "127.0.0.1";

            try {
                inetAddress = InetAddress.getByName(IP);
            } catch (UnknownHostException e) {
                printError("Please, insert a valid IP address");
            }
        }
        return inetAddress;
    }

    /**
     * Method which asks the user if they want to join an existing lobby or create a new one
     *
     * @return the selected choice.
     */
    @Override
    public boolean askIfNewLobby() {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        options.add(new Pair<>("Create Lobby", Color.reset()));
        options.add(new Pair<>("Join Lobby", Color.reset()));

        ArrayList<Integer> choices = displaySelectionForm(options, null, 1, "");
        return choices.get(0) == 0;
    }

    /**
     * Method which asks the user  the Id they wants to enroll with.
     *
     * @return the Id.
     */
    @Override
    public String askUserID() {
        out.println("Insert username");
        this.thisPlayer = in.nextLine();
        players.add(this.thisPlayer);
        playerStates.put(this.thisPlayer, new PlayerState());
        faithTrack = new FaithTrackView(players);
        return this.thisPlayer;
    }

    /**
     * Method which asks the user the name of the leader of the lobby they want to join. The default ID is *.
     *
     * @return The leader Id
     */
    @Override
    public String askLeaderID() {
        out.println("Insert the username of the leader of the lobby you want to join");
        out.println("Put * to join a random lobby");
        return in.nextLine();
    }

    /**
     * Method which displays the state current state of the lobby.
     *
     * @param leaderID       The leader Id
     * @param otherPlayersID The array of other players' Ids
     */
    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPlayersID) {
        players = new ArrayList<>();
        players.add(leaderID);
        players.addAll(otherPlayersID);

        System.out.println("THIS IS THE CURRENT STATE OF THE LOBBY:");
        StringBuilder builder = new StringBuilder();
        builder.append(Color.RED.getAnsiCode()).append("╔═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╗  ").append(Color.WHITE.getAnsiCode());

        otherPlayersID.forEach(name -> {
            builder.append("╔═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╗  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode()).append("║ ");
        builder.append(leaderID);
        builder.append(" ║  ").append(Color.WHITE.getAnsiCode());

        otherPlayersID.forEach(name -> {
            builder.append("║ ");
            builder.append(name);
            builder.append(" ║  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode()).append("╚═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╝  ").append(Color.WHITE.getAnsiCode());

        otherPlayersID.forEach(name -> {
            builder.append("╚═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╝  ");
        });

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(obj.getWidth() + obj.getX() + 1, obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        if (this.thisPlayer.equals(leaderID) && players.size() > 1) {
            new Thread(this::askForGameStart).start();
        }
    }

    /**
     * Method which asks the leader to type "start" when they want to start the match.
     */
    private void askForGameStart() {
        out.println("Type \"start\" to start the game");
        String s;
        try {
            do {
                s = in.nextLine().toUpperCase();
            } while (!s.equals("START"));
            client.startMatch();
        } catch (Exception ignore) {
        }
    }

    private ArrayList<String> toSetupPlayers = null;

    /**
     * Method used to tell a user that they have to wait until all other users have finished their setup.
     *
     * @param playerID This user.
     */
    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {
        if (toSetupPlayers == null) toSetupPlayers = new ArrayList<>(players);
        toSetupPlayers.remove(playerID);

        if (toSetupPlayers.isEmpty() || toSetupPlayers.contains(thisPlayer)) return;

        out.println("Waiting for");
        for (String p : toSetupPlayers) out.println(p);
        out.println("to finish their setup");
    }

    /**
     * Initializes data structures needed for representing the match
     */
    @Override
    public void initializeMatchObjects() {
        for (String playerID : players) {
            playerStates.putIfAbsent(playerID, new PlayerState());
        }
        faithTrack = new FaithTrackView(players);
    }

    @Override
    public void invalidateUsername() {

    }

    /**
     * Method which asks the user to choose among a set of leader cards in the setup fase.
     *
     * @param leaderCardsIDs             The possible options.
     * @param numberOFLeaderCardsToChose The number of cards to choose.
     * @return The array of chosen cards.
     */
    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {
        ArrayList<Integer> indexes = leaderCardsIDs.stream().map(name -> Integer.parseInt(name.substring(10))).collect(Collectors.toCollection(ArrayList::new));

        if (numberOFLeaderCardsToChose <= 0) return new ArrayList<>();

        int numberOFLeaderCardsToChoseLeft = numberOFLeaderCardsToChose;

        out.println(thisPlayer.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChoseLeft + " AMONG THESE LEADER CARDS:");
        ArrayList<DrawableObject> objs = new ArrayList<>();
        leaderCardsIDs.forEach(name -> {
            LeaderCardView card = new LeaderCardView(name);
            DrawableObject obj1 = new DrawableObject(card.toString(), 0, 0);
            objs.add(obj1);

        });
        Panel panel = new Panel(objs, out, true);

        panel.show();
        ArrayList<Integer> cardIndexes = new ArrayList<>();
        while (numberOFLeaderCardsToChoseLeft > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChoseLeft + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            String input = in.nextLine();
            boolean validInput = false;
            int chosenNumber = 0;
            while (!validInput) {
                if (!input.matches("-?\\d+")) {
                    out.println("YOU HAVE TO INSERT NUMBERS");
                    input = in.nextLine();
                    continue;
                }
                chosenNumber = Integer.parseInt(input);
                if (!indexes.contains(chosenNumber)) {
                    out.println("WRONG NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                    input = in.nextLine();
                    continue;
                }
                if (cardIndexes.contains(chosenNumber)) {
                    out.println("YOU HAVE ALREADY SELECTED THIS NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                    input = in.nextLine();
                    continue;
                }

                validInput = true;
            }

            cardIndexes.add(chosenNumber);
            numberOFLeaderCardsToChoseLeft--;
        }
        out.println("YOU HAVE CHOSEN THESE LEADER CARDS:");

        HashMap<String, LeaderCardView> leaderCardsChosen = new HashMap<>();
        ArrayList<DrawableObject> objs2 = new ArrayList<>();

        cardIndexes.forEach(index -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + index);
            leaderCardsChosen.put("LeaderCard" + index, card);
            DrawableObject obj1 = new DrawableObject(card.toString(), 0, 0);
            objs2.add(obj1);
        });
        Panel panel2 = new Panel(objs2, out, true);

        panel2.show();

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (isAffirmative(response)) {
            playerStates.get(thisPlayer).leaderCards.putAll(leaderCardsChosen);
            return new ArrayList<>(leaderCardsChosen.keySet());
        } else return choseInitialLeaderCards(leaderCardsIDs, numberOFLeaderCardsToChose);
    }

    /**
     * Method to set a personal production power in the dashboard of a given player.
     *
     * @param playerId                The player id.
     * @param personalProductionPower The personal Production power.
     */
    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {
        if (playerStates.get(playerId).dashBoard == null) {
            playerStates.get(playerId).dashBoard = new DashBoardView(new ArrayList<>(), new HashMap<>(), new ArrayList<>(), playerId);
        }
        playerStates.get(playerId).dashBoard.setPersonalProductionPower(personalProductionPower);
    }

    /**
     * Method to update teh faithTrack visualization.
     *
     * @param playerID       The id of the player who changed its position.
     * @param position       The new position.
     * @param popeFavorCards The current state of favor pope cards for each player.
     */
    @Override
    public void updateFaithTrack(String playerID, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        faithTrack.updatePlayerPosition(playerID, position);
        faithTrack.updateFavorPopeCard(popeFavorCards);
    }

    /**
     * Method which updates the visualization of the dashboard.
     *
     * @param playerId    The id of this dashboard's player.
     * @param topDevCards The state of the development card in the slots.
     * @param strongBox   The current state of the strongbox.
     * @param warehouse   The current state of depots in the warehouse.
     */
    @Override
    public void updateDashboard(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        DashBoardView dashBoardView = playerStates.get(playerId).dashBoard;
        if (dashBoardView == null) {
            playerStates.get(playerId).dashBoard = new DashBoardView(topDevCards, strongBox, warehouse, playerId);
        } else {
            dashBoardView.updateTopDevCards(topDevCards);
            dashBoardView.updateStrongBox(strongBox);
            dashBoardView.updateWarehouse(warehouse);
        }
    }

    /**
     * Method which updates the state of Leader Cards.
     *
     * @param playerId    The id of the player to which the cards belong.
     * @param leaderCards The hashmap containing the id of the leader card as key, and the boolean telling if the card is active or not as value.
     */
    @Override
    public void updateLeaderCardsState(String playerId, HashMap<String, Boolean> leaderCards) {
        HashMap<String, LeaderCardView> leaderCardsViews = playerStates.get(playerId).leaderCards;


        Set<String> newLeaderCards = leaderCards.keySet();

        Set<String> toRemove = new HashSet<>();
        for (String leaderCardID : leaderCardsViews.keySet()) {
            if (!newLeaderCards.contains(leaderCardID))
                toRemove.add(leaderCardID);
        }
        for (String s : toRemove)
            leaderCardsViews.remove(s);

        for (String leaderCardID : newLeaderCards) {
            LeaderCardView leaderCardView = leaderCardsViews.get(leaderCardID);
            if (leaderCardView == null) {
                leaderCardsViews.put(leaderCardID, new LeaderCardView(leaderCardID));
                leaderCardView = leaderCardsViews.get(leaderCardID);
            }
            leaderCardView.setActive(leaderCards.get(leaderCardID));
        }
    }

    /**
     * Method which updates the state of market.
     *
     * @param rows         The number of rows.
     * @param cols         The number of columns.
     * @param marketStatus The matrix of marbles.
     * @param marbleLeft   The marble left aside.
     */
    @Override
    public void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft) {
        market = new MarketView(marbleLeft, marketStatus, rows, cols);
    }

    /**
     * Method to update the visualization of the current state of DevCardGrid.
     *
     * @param topDevCardIDs The matrix of development card.
     */

    @Override
    public void updateDevCardGrid(String[][] topDevCardIDs) {
        devCardGridView = new DevCardGridView(topDevCardIDs);
    }

    /**
     * Method which asks the user to select a row or column from the market.
     *
     * @return The BuyResourcesEvent containing all the information about the result of user's choice.
     */
    public BuyResourcesEvent askForMarketRow() {
        Direction dir = null;
        int index = 0;
        String output = "YOU HAVE TO CHOOSE THE RESOURCES FROM ONE ROW OR ONE COLUMN OF THE MARKET\n" +
                "WOULD YOU LIKE A ROW OR A COLUMN? ";
        ArrayList<Pair<String, String>> direction = new ArrayList<>();
        direction.add(new Pair<>(Direction.ROW.toString(), Color.WHITE.getAnsiCode()));
        direction.add(new Pair<>(Direction.COLUMN.toString(), Color.WHITE.getAnsiCode()));
        out.println(market.toString());
        int result = displaySelectionForm(direction, null, 1, output).get(0);
        if (result == 0) {//ROW
            dir = Direction.ROW;
            ArrayList<Pair<String, String>> indexRow = new ArrayList<>();
            for (int i = 0; i < market.getRows(); i++) {
                indexRow.add(new Pair<>("ROW " + (i + 1), Color.WHITE.getAnsiCode()));

            }
            index = displaySelectionForm(indexRow, null, 1, " ").get(0);
        }
        if (result == 1) {//COLUMN
            dir = Direction.COLUMN;
            ArrayList<Pair<String, String>> indexColumn = new ArrayList<>();
            for (int i = 0; i < market.getCols(); i++) {
                indexColumn.add(new Pair<>("COLUMN " + (i + 1), Color.WHITE.getAnsiCode()));

            }
            index = displaySelectionForm(indexColumn, null, 1, " ").get(0);
        }
        assert dir != null;
        out.println("YOU HAVE CHOSEN " + dir + (index + 1));
        return new BuyResourcesEvent(thisPlayer, dir, index);
    }

    /**
     * method used to ask the user  which dev card they want to buy from the devCardGrid and which slot in the warehouse to put it.
     *
     * @return BuyDevCardEvent containing the information required to by the card.
     */
    public BuyDevCardsEvent askForDevCard() {
        int slot;
        String devCard;

        devCardGridView.display();
        ArrayList<Pair<String, String>> gridSelection = new ArrayList<>();
        String[][] grid = devCardGridView.getTopDevCardIDs();
        for (String[] strings : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (strings[j] != null) {
                    gridSelection.add(new Pair<>(strings[j], Color.WHITE.getAnsiCode()));
                }
            }
        }
        int input1 = displaySelectionForm(gridSelection, null, 1, "DEVELOPMENT CARDS AVAILABLE : \n").get(0);
        devCard = gridSelection.get(input1).getKey();

        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        ArrayList<Pair<String, String>> selectionArray = new ArrayList<>();
        AtomicInteger slotIndex = new AtomicInteger(1);
        playerStates.get(thisPlayer).getDashBoard().getTopDevCards().forEach(n -> {
            selectionArray.add(new Pair<>("SLOT " + slotIndex, Color.WHITE.getAnsiCode()));
            slotIndex.getAndIncrement();
        });
        slot = displaySelectionForm(selectionArray, null, 1, "THESE ARE THE SLOTS TO WHICH YOU CAN ADD THE BOUGHT CARD : \n").get(0);


        return new BuyDevCardsEvent(thisPlayer, devCard, slot);
    }

    /**
     * Method used to ask the user which production power they want to activate.
     *
     * @return ActivateProductionEvent  with all the information required to activate production.
     */
    public ActivateProductionEvent askForProductionPowersToUse() {
        playerStates.get(thisPlayer).getDashBoard().displayPersonalProductionPower(out);
        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();

        ArrayList<Pair<String, String>> opt = new ArrayList<>();
        opt.add(new Pair<>("Personal production power", Color.reset()));
        for (String devCardID : playerStates.get(thisPlayer).getDashBoard().getTopDevCards()) {
            if (devCardID != null) opt.add(new Pair<>(devCardID, Color.reset()));
        }

        ArrayList<Integer> powerChosen = displaySelectionFormVariableChoices(opt, null, opt.size(), "Choose which production power to activate");

        return new ActivateProductionEvent(thisPlayer, powerChosen.stream().filter(x -> x != 0).map(i -> opt.get(i).getKey()).collect(Collectors.toCollection(ArrayList::new)), powerChosen.contains(0));
    }

    /**
     * Method to ask the user which LeaderCard to discard.
     *
     * @return The id of the card to discard.
     * @throws NotPresentException If there aren't leader cards to discard.
     */
    public String askForLeaderCardToDiscard() throws NotPresentException {
        Collection<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values();
        if (leaderCardViews.size() == 0)
            throw new NotPresentException("No leader card can be discarded because you have no leader card");

        leaderCardViews = leaderCardViews.stream().filter(lcv -> !lcv.isActive()).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0)
            throw new NotPresentException("No leader card can be discarded because you're leader cards are already active");

        ArrayList<Pair<String, String>> choices = leaderCardViews.stream().map(LeaderCardView::getIdCard).map(s -> new Pair<>(s, Color.reset())).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        int choice = displaySelectionForm(choices, new Panel(drawableLeaderCards, out, true), 1, "Choose a leader card to discard").get(0);

        return choices.get(choice).getKey();
    }

    /**
     * Method to ask the user which leader card to activate.
     *
     * @return The id of the card to activate.
     * @throws NotPresentException If there aren't any cards or all the leader card are already active.
     */
    public String askForLeaderCardToActivate() throws NotPresentException {
        Collection<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values();
        if (leaderCardViews.size() == 0)
            throw new NotPresentException("No leader card can be activated because you have no leader card");

        leaderCardViews = leaderCardViews.stream().filter(lcv -> !lcv.isActive()).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0)
            throw new NotPresentException("No leader card can be activated because you're leader cards are already active");

        ArrayList<Pair<String, String>> choices = leaderCardViews.stream().map(LeaderCardView::getIdCard).map(s -> new Pair<>(s, Color.reset())).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        int choice = displaySelectionForm(choices, new Panel(drawableLeaderCards, out, true), 1, "Choose a leader card to activate").get(0);

        return choices.get(choice).getKey();
    }

    /**
     * Method to ask the user if they want to select or deselect leader powers in a leader card.
     *
     * @return The array of LeaderPower whose state "selected" has changed
     * @throws NotPresentException if no leader power is selectable since all the cards are not active.
     */
    public ArrayList<LeaderPowerSelectStateEvent> askForLeaderCardToSelectOrDeselect() throws NotPresentException {
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(LeaderCardView::isActive).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0) throw new NotPresentException("No leader card is active");

        ArrayList<Pair<String, String>> options = new ArrayList<>();
        ArrayList<String> leaderCardIDs = new ArrayList<>();
        ArrayList<Integer> leaderPowerIndexes = new ArrayList<>();
        ArrayList<Boolean> leaderPowersSelectedState = new ArrayList<>();

        for (LeaderCardView leaderCardView : leaderCardViews) {
            for (int i : leaderCardView.getSelectablePowersIndexes()) {
                options.add(new Pair<>(leaderCardView.getIdCard() + "\nPower " + i, Color.reset()));
                leaderCardIDs.add(leaderCardView.getIdCard());
                leaderPowerIndexes.add(i);
                leaderPowersSelectedState.add(leaderCardView.getSelected(i));
            }
        }
        if (options.size() == 0) throw new NotPresentException("No leader power is selectable");

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> choices = displaySelectionFormVariableChoices(options, new Panel(drawableLeaderCards, out, true), options.size(), "Choose the leader powers to select\n");

        ArrayList<LeaderPowerSelectStateEvent> ret = new ArrayList<>();
        for (int choice : choices) {
            ret.add(new LeaderPowerSelectStateEvent(thisPlayer, leaderCardIDs.get(choice), leaderPowerIndexes.get(choice), !leaderPowersSelectedState.get(choice)));
        }
        return ret;
    }

    /**
     * Utility method to display any possible information of the match, concerning the user or the other players.
     *
     * @return a generic int to the caller method.
     */
    public int displayOthers() {

        ArrayList<Pair<String, String>> options;
        options = Arrays.stream(DisplayOptions.values()).map(option -> new Pair<>(option.getTitle(), Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
        options.add(new Pair<>("NOTHING", Color.WHITE.getAnsiCode()));
        int input = displaySelectionForm(options, null, 1, "WOULD YOU LIKE TO SEE SOMETHING ABOUT THE GAME ?\n").get(0);

        if (input < DisplayOptions.values().length) {
            out.println(DisplayOptions.values()[input].getMessage());
            switch (DisplayOptions.values()[input]) {
                case DISPLAY_MARKET -> out.println(market.toString());
                case DISPLAY_DASHBOARD -> {
                    int inputPlayer;
                    if (players.size() != 1) {
                        ArrayList<Pair<String, String>> allPlayers;
                        allPlayers = players.stream().map(player -> {
                            if (player.equals(thisPlayer)) return "YOURS";
                            else return player;
                        }).map(player -> new Pair<>(player, Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
                        inputPlayer = displaySelectionForm(allPlayers, null, 1, "WHOSE DASHBOARD WOULD YOU LIKE TO SEE? \n").get(0);
                    } else {
                        inputPlayer = 0;
                    }
                    playerStates.get(players.get(inputPlayer)).getDashBoard().displayAll(players.get(inputPlayer));
                }
                case DISPLAY_FAITH_TRACK -> this.faithTrack.display("Check the incremented positions and \n acquired PopeFavorCards !", thisPlayer);
                case DISPLAY_LEADER_CARD -> {
                    int inputPlayer;
                    if (players.size() != 1) {
                        ArrayList<Pair<String, String>> allPlayers;
                        allPlayers = players.stream().map(player -> {
                            if (player.equals(thisPlayer)) return "YOURS";
                            else return player;
                        }).map(player -> new Pair<>(player, Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
                        inputPlayer = displaySelectionForm(allPlayers, null, 1, "WHOSE LEADER CARDS WOULD YOU LIKE TO SEE? \n").get(0);
                    } else {
                        inputPlayer = 0;
                    }

                    out.println("[31;1;4m" + players.get(inputPlayer) + "'S SET OF LEADER CARDS\n" + Color.reset());
                    ArrayList<DrawableObject> objs = new ArrayList<>();
                    playerStates.get(players.get(inputPlayer)).getLeaderCards().forEach((cardId, cardView) -> objs.add(new DrawableObject(cardView.toString(), 0, 0)));
                    Panel cardPanel = new Panel(objs, out, true);
                    cardPanel.show();
                }
                case DISPLAY_DEV_CARD_GRID -> devCardGridView.display();
            }

            out.println("WOULD YOU LIKE TO SEE MORE? Y/N");
            String answer = in.next().toUpperCase();
            in.nextLine();
            if (answer.equals("YES") || answer.equals("Y")) {
                return displayOthers();
            } else {
                out.println("done");
                return 1;
            }
        } else {
            out.println("YOU CHOSE TO SEE ANYTHING");
            return 1;
        }
    }

    /**
     * Method which gives the user a set of options about which next action to do. It could be a main action (between buy resources from market, buy dev card and produce) or it could be a leader action( discard, activate a card) or the possibility to see some other player's state or the possibility to select or deselect a leader power.
     *
     * @param playerID  this player Id.
     * @param lastRound If it is the last round.
     * @param turnState One of the possible state of one turn: according to which state of the turn the player is in, possible actions to do are different.
     * @return The array of Events this method generates.
     */
    @Override
    public ArrayList<Event> askForNextAction(String playerID, boolean lastRound, TurnState turnState) {

        ArrayList<Event> events = new ArrayList<>();
        if (!thisPlayer.equals(playerID)) {
//            thread.interrupt();
//            thread= new Thread(() -> {
//                out.println("[31;1;4m" + playerID + " " + turnState.getDescription() + "\033[0m \n\n");
//                displayOthers();
//            });
//            thread.start();
//            boolean stopThread = false;
//            while (!stopThread) {
//                long startTime = System.currentTimeMillis();
//                long elapsedTime = 0;
//
//                while (elapsedTime < 2 * 60 * 1000) {
//                    elapsedTime = (new Date()).getTime() - startTime;
//                }
//                stopThread =thisPlayer.equals(playerID);
//            }
//
//
//                        stopThread = true;
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (!stopThread) {  do something }
//                        displayOthers();
//                    }
//                }
//            }).start();
            out.println("[31;1;4m" + playerID + " " + turnState.getDescription() + "\033[0m \n\n");
            displayOthers();
        } else {
            Action selectedAction = null;
            switch (turnState) {
                case START -> {
                    int inputIndex;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).filter(action -> action != Action.END_TURN).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair<>(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);
                    selectedAction = allActions.get(inputIndex);
                    out.println(selectedAction.getDescription());
                }


                case AFTER_LEADER_CARD_ACTION -> {
                    int inputIndex;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).filter(action -> action != Action.LEADER_ACTION && action != Action.END_TURN).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair<>(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);
                    selectedAction = allActions.get(inputIndex);
                    out.println(selectedAction.getDescription());
                }


                case AFTER_MAIN_ACTION -> {
                    int inputIndex;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).filter(action -> action == Action.LEADER_ACTION || action == Action.DISPLAY_SMTH || action == Action.END_TURN).collect(Collectors.toCollection(ArrayList::new));

                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair<>(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);

                    selectedAction = allActions.get(inputIndex);
                    out.println(selectedAction.getDescription());
                }


                case END_OF_TURN -> {
                    out.println("WOULD YOU LIKE TO SEE SOME OTHER PLAYER'S/ YOUR STATE? Y/N");
                    String response = in.next().toUpperCase();
                    if (isAffirmative(response)) {
                        displayOthers();
                    }
                    out.println("YOUR TURN HAS ENDED");

                    events.add(new EndTurnEvent(thisPlayer));
                }


                case WAITING_FOR_SOMETHING -> {
                }

                case MATCH_ENDED -> out.println("MATCH HAS ENDED");
            }

            if (selectedAction != null) {
                switch (selectedAction) {
                    case BUY_DEVCARD -> events.add(askForDevCard());
                    case TAKE_RESOURCES_FROM_MARKET -> events.add(askForMarketRow());
                    case PRODUCE -> events.add(askForProductionPowersToUse());
                    case LEADER_ACTION -> {
                        ArrayList<Pair<String, String>> options = new ArrayList<>();
                        options.add(new Pair<>("DISCARD LEADER CARD", Color.WHITE.getAnsiCode()));
                        options.add(new Pair<>("ACTIVATE LEADER CARD", Color.WHITE.getAnsiCode()));

                        int chosenAction = displaySelectionForm(options, null, 1, "THESE ARE THE POSSIBLE LEADER ACTIONS YOU CAN DO: \n").get(0);

                        if (chosenAction == 0) {
                            try {
                                String leaderCardToDiscard = askForLeaderCardToDiscard();
                                events.add(new DiscardLeaderCardEvent(thisPlayer, leaderCardToDiscard));
                            } catch (NotPresentException e) {
                                printWarning(e.getMessage().toUpperCase());
                                events.addAll(askForNextAction(playerID, lastRound, turnState));
                            }
                        } else {
                            try {
                                String leaderCardToActivate = askForLeaderCardToActivate();
                                events.add(new ActivateLeaderCardEvent(thisPlayer, leaderCardToActivate));
                            } catch (NotPresentException e) {
                                printWarning(e.getMessage().toUpperCase());
                                events.addAll(askForNextAction(playerID, lastRound, turnState));
                            }
                        }
                    }
                    case DISPLAY_SMTH -> {
                        displayOthers();
                        events.addAll(askForNextAction(playerID, lastRound, turnState));
                    }
                    case SELECT_LEADER_CARD -> {
                        try {
                            events.addAll(askForLeaderCardToSelectOrDeselect());
                        } catch (NotPresentException e) {
                            printWarning(e.getMessage().toUpperCase());
                            events.addAll(askForNextAction(playerID, lastRound, turnState));
                        }
                    }
                    case END_TURN -> events.add(new EndTurnEvent(thisPlayer));
                }
            }

        }
        out.println("returned event");
        return events;
    }

    /**
     * Method used to update the DepositLeaderPowers
     *
     * @param playerID         This Player
     * @param leaderCardID     The id of the leader card  where deposits need to be updated.
     * @param leaderPowerIndex The index of the leader power in the array of powers of this card.
     * @param storedResources  New organization of resources in the depositLeaderPower.
     */
    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<
            Resource, Integer> storedResources) {
        playerStates.get(playerID).getLeaderCards().get(leaderCardID).updateDepositLeaderPower(leaderPowerIndex, storedResources);
    }

    /**
     * Method to update the state ( selected/ deselected ) of leader powers in one given leader card.
     *
     * @param playerId            this player id
     * @param leaderCardID        the leader card in question.
     * @param powerSelectedStates The array of booleans telling for each leader power whether it is selected or not.
     */
    @Override
    public void updateLeaderPowersSelectedState(String playerId, String
            leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        LeaderCardView leaderCardView = playerStates.get(playerId).getLeaderCards().get(leaderCardID);
        for (int i = 0; i < powerSelectedStates.size(); i++) {
            leaderCardView.setPowerSelectionState(i, powerSelectedStates.get(i));
        }
    }

    /**
     * Method to display the grid of final scores in the end of the game.
     *
     * @param finalPlayerStates Array of FinalPlayerStates, with player- number of points gained, number of resources gained.
     */
    public void displayEndOfGame(ArrayList<FinalPlayerState> finalPlayerStates) {
        int maxNameLength = finalPlayerStates.stream().map(finalPS -> finalPS.getPlayerID().length()).reduce(0, Integer::max);

        StringBuilder builder = new StringBuilder();
        builder.append("\033[31;1;4mFINAL SCORES\033[0m \n");
        builder.append(buildBorderOfLength(maxNameLength + 2, "╔", "╦"));
        builder.append(buildBorderOfLength("Total Scores".length() + 1, "═", "╦"));
        builder.append(buildBorderOfLength("Total Resources".length() + 1, "═", "╗\n"));

        builder.append("║ ").append(buildContent(maxNameLength, "")).append(" ║ Total Scores ║ Total Resources ║\n");
        builder.append(buildBorderOfLength(maxNameLength + 2, "╠", "╬"));
        builder.append(buildBorderOfLength("Total Scores".length() + 1, "═", "╬"));
        builder.append(buildBorderOfLength("Total Resources".length() + 1, "═", "╣\n"));

        for (int i = 0; i < finalPlayerStates.size() - 1; i++) {
            FinalPlayerState FPstate = finalPlayerStates.get(i);
            builder.append("║ ").append(buildContent(maxNameLength, FPstate.getPlayerID()));
            if (FPstate.getVictoryPoints() < 10 && FPstate.getTotalResources() < 10)
                builder.append(" ║      ").append(FPstate.getVictoryPoints()).append("       ║        ").append(FPstate.getTotalResources()).append("        ║\n");
            else if (FPstate.getVictoryPoints() >= 10 && FPstate.getTotalResources() < 10)
                builder.append(" ║      ").append(FPstate.getVictoryPoints()).append("      ║        ").append(FPstate.getTotalResources()).append("        ║\n");
            else if (FPstate.getVictoryPoints() < 10 && FPstate.getTotalResources() >= 10)
                builder.append(" ║      ").append(FPstate.getVictoryPoints()).append("       ║        ").append(FPstate.getTotalResources()).append("       ║\n");
            else
                builder.append(" ║      ").append(FPstate.getVictoryPoints()).append("      ║        ").append(FPstate.getTotalResources()).append("       ║\n");
            builder.append(buildBorderOfLength(maxNameLength + 2, "╠", "╬"));
            builder.append(buildBorderOfLength("Total Scores".length() + 1, "═", "╬"));
            builder.append(buildBorderOfLength("Total Resources".length() + 1, "═", "╣\n"));
        }
        FinalPlayerState lastFPstate = finalPlayerStates.get(finalPlayerStates.size() - 1);
        builder.append("║ ").append(buildContent(maxNameLength, lastFPstate.getPlayerID()));
        if (lastFPstate.getVictoryPoints() < 10 && lastFPstate.getTotalResources() < 10)
            builder.append(" ║      ").append(lastFPstate.getVictoryPoints()).append("       ║        ").append(lastFPstate.getTotalResources()).append("        ║\n");
        else if (lastFPstate.getVictoryPoints() >= 10 && lastFPstate.getTotalResources() < 10)
            builder.append(" ║      ").append(lastFPstate.getVictoryPoints()).append("      ║        ").append(lastFPstate.getTotalResources()).append("        ║\n");
        else if (lastFPstate.getVictoryPoints() < 10 && lastFPstate.getTotalResources() >= 10)
            builder.append(" ║      ").append(lastFPstate.getVictoryPoints()).append("       ║        ").append(lastFPstate.getTotalResources()).append("       ║\n");
        else
            builder.append(" ║      ").append(lastFPstate.getVictoryPoints()).append("      ║        ").append(lastFPstate.getTotalResources()).append("       ║\n");
        builder.append(buildBorderOfLength(maxNameLength + 2, "╚", "╩"));
        builder.append(buildBorderOfLength("Total Scores".length() + 1, "═", "╩"));
        builder.append(buildBorderOfLength("Total Resources".length() + 1, "═", "╝\n"));
        out.println(builder);

        out.println("WHEN YOU WANT TO QUIT THE GAME, TYPE 'QUIT'\n");
        String answer = in.nextLine();
        while (!answer.equalsIgnoreCase("QUIT")) {
            answer = in.nextLine();
        }
        System.out.println("Game Ended");

    }

    /**
     * Utility method which counts the total quantity of resources in the DepositLeaderPower
     *
     * @return The hashmap with ResourceType- Quantity.
     */
    public HashMap<Resource, Integer> getDepositLeaderPowerTotalResources() {
        HashMap<Resource, Integer> totalRes = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(LeaderCardView::isActive).map(LeaderCardView::getTotalResourcesInDepositLeaderPowers).forEach(hashMap -> hashMap.forEach((key, value) -> totalRes.put(key, totalRes.get(key) + value)));
        return totalRes;
    }

    /**
     * Utility method to build a  string resembling a border,  delimited by some given symbols,  and of given length.
     *
     * @param length           The length of the string.
     * @param typeOfBorderInit The symbol to put at the beginning of the string.
     * @param typeOfBorderEnd  The symbol to put at the end of the string.
     * @return The border given as a string.
     */
    public static String buildBorderOfLength(int length, String typeOfBorderInit, String typeOfBorderEnd) {
        return typeOfBorderInit +
                "═".repeat(Math.max(0, length)) +
                typeOfBorderEnd;

    }

    /**
     * Utility method to count the difference between the length of one string and a given  length  and replace this difference with empty space.
     *
     * @param length  The given length.
     * @param content the given string
     * @return the  string of empty spaces.
     */
    public static String buildContent(int length, String content) {
        return content +
                " ".repeat(Math.max(0, length - content.length()));
    }

    /**
     * this method checks whether an hashmap of resources is empty or not.
     */
    public static boolean isEmptyHashMap(HashMap<Resource, Integer> map) {
        if (!map.isEmpty()) return map.keySet().stream().map(key -> map.get(key) == 0).reduce(true, (a, b) -> a && b);
        else return true;
    }

    /**
     * utility method which treats case 2 of switch in askWhereToTakeResourcesFrom
     *
     * @param res                   resource type
     * @param chosenNumber          number of resources to take
     * @param strongBoxHashMap      hashmap containing the current state of strongBox up until now
     * @param chosenFromStrongbox   hashmap containing all the resources chosen from strongbox un until now
     * @param leaderDepositsHashMap hashmap containing the current state of extra deposits
     * @param allChosen             hashmap containing all the resources chosen until now
     * @param required              hashmap containing the required resources to take from deposits
     * @return true if the operation was successful otherwise false
     */
    private boolean doCase2InSwitch(Resource res, int chosenNumber, HashMap<Resource, Integer> strongBoxHashMap, HashMap<Resource, Integer> chosenFromStrongbox, HashMap<Resource, Integer> leaderDepositsHashMap, HashMap<Resource, Integer> allChosen, HashMap<Resource, Integer> required) {
        DashBoardView thisDashboard = playerStates.get(thisPlayer).getDashBoard();
        boolean successful;
        if (!thisDashboard.getStrongBox().containsKey(res)) {
            out.println(DepotResultMessage.INVALID_RES_STRONGBOX.getMessage());
            successful = DepotResultMessage.INVALID_RES_STRONGBOX.getSuccessful();
            out.println("QUANTITY OF RESOURCES IN THE STRONGBOX REMAINS THE SAME\n");
        } else if (chosenNumber > strongBoxHashMap.get(res)) {
            out.println(DepotResultMessage.REACHED_MIN_CAP_STRONGBOX.getMessage());
            successful = DepotResultMessage.REACHED_MIN_CAP_STRONGBOX.getSuccessful();
            out.println("QUANTITY OF RESOURCES IN THE STRONGBOX REMAINS THE SAME\n");
        } else {
            out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
            successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessful();
            allChosen.put(res, allChosen.get(res) + chosenNumber);
            chosenFromStrongbox.put(res, chosenFromStrongbox.get(res) + chosenNumber);
            leaderDepositsHashMap.put(res, required.get(res) - chosenNumber);

        }
        out.println("WHAT REMAINS IN STRONGBOX:\n");
        out.println(displayResourcesInHashMap(strongBoxHashMap));
        return successful;
    }

    /**
     * @param arrayOfInfoPowers the array containing all the information of all the extra deposits of leader cards
     * @param res               resource type
     * @return if in the deposits there is the given resource
     */
    private boolean leaderDepositContainsResourceType(ArrayList<InfoPower> arrayOfInfoPowers, Resource res) {
        return arrayOfInfoPowers.stream().map(infoPower -> infoPower.getPower().getMaxResources().containsKey(res)).reduce(false, (a, b) -> a || b);
    }

    /**
     * Method to ask the user where to take some resources from either the warehouse or  the deposit leader power if available and selected or the strongbox.
     *
     * @param required0            resources to take.
     * @param freeChoicesResources optional additional resources to take.
     * @return The event which contains all the necessary information to take resources from deposits, for example to buy a devCard or to activate a leader card.
     */
    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required0,
                                                            int freeChoicesResources) {
        HashMap<Resource, Integer> required = new HashMap<>();
        //remove empty keys
        required0.forEach((key, value) -> {
            if (value != 0) required.put(key, value);
        });
        //clone required
        HashMap<Resource, Integer> clonedRequired = new HashMap<>(required);
        HashMap<Resource, Integer> emptyHashmap = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(re -> emptyHashmap.put(re, 0));
        //prepare parameters to build event to return
        HashMap<Resource, Integer> allChosen = new HashMap<>(emptyHashmap);
        HashMap<Resource, Integer> chosenFromWarehouse = new HashMap<>(emptyHashmap);
        HashMap<Resource, Integer> chosenFromStrongbox = new HashMap<>(emptyHashmap);
        HashMap<Resource, Integer> chosenFromLeaderPower = new HashMap<>(emptyHashmap);

        DashBoardView thisDashboard = playerStates.get(thisPlayer).getDashBoard();
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = thisDashboard.getWarehouse();

        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = new ArrayList<>(playerStates.get(thisPlayer).getLeaderCards().values());
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        leaderCardViews.forEach(cardView -> {
            if (cardView.isActive()) {
                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
                    if (power instanceof DepositLeaderPower)
                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
                });
            }
        });
        //display all deposits
//        out.println("THIS IS THE CURRENT STATE OF DEPOSITS: \n");
//        displayAvailableDeposits(thisDashboard);
//        out.println(thisDashboard.strongBoxToString());


        //hashmaps that take count of total resources in each category of deposit(warehouse, leader deposit, strongbox)
        HashMap<Resource, Integer> warehouseHashMap = thisDashboard.totalResourcesInWarehouse();
        HashMap<Resource, Integer> leaderDepositsHashMap = getDepositLeaderPowerTotalResources();
        HashMap<Resource, Integer> strongBoxHashMap = new HashMap<>(thisDashboard.getStrongBox());

        //display how many resources the user has to take
        StringBuilder builder = new StringBuilder();
        boolean emptyRequired = isEmptyHashMap(required);
        if (!emptyRequired) {
            out.println("YOU HAVE TO TAKE THE FOLLOWING RESOURCES FROM YOUR DEPOSITS: \n");
            out.println(displayResourcesInHashMap(required) + "\n");
        }
        if (freeChoicesResources != 0) {
            out.println("YOU HAVE " + freeChoicesResources + " OPTIONAL RESOURCES YOU CAN CHOOSE:\n");
            out.println("HAVE A LOOK AT THE STATE OF YOUR DEPOSITS:\n");
            if (isEmptyHashMap(warehouseHashMap)) out.println("Warehouse is empty\n");
            else {
                out.println("Warehouse: \n");
                out.println(displayResourcesInHashMap(warehouseHashMap));
            }
            if (isEmptyHashMap(leaderDepositsHashMap)) out.println("LeaderDeposit is empty\n");
            else {
                out.println("Leader deposits: \n");
                out.println(displayResourcesInHashMap(leaderDepositsHashMap));
            }
            if (isEmptyHashMap(strongBoxHashMap)) out.println("Strongbox is empty\n");
            else {
                out.println("Strongbox: \n");
                out.println(displayResourcesInHashMap(strongBoxHashMap));
            }

            boolean validResourcesChosen = false;
            while (!validResourcesChosen) {
                HashMap<Resource, Integer> temp = new HashMap<>(required);
                chooseResources(freeChoicesResources, Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new))).forEach((key, value) -> {
                    if (value != 0) {
                        if (temp.containsKey(key)) temp.put(key, temp.get(key) + value);
                        else temp.put(key, value);
                    }
                });
                //check if there are enough resources in the deposits
                HashMap<Resource, Integer> totalResources = new HashMap<>(warehouseHashMap);
                leaderDepositsHashMap.forEach((key, value) -> {
                    if (totalResources.containsKey(key)) totalResources.put(key, totalResources.get(key) + value);
                    else totalResources.put(key, value);
                });
                strongBoxHashMap.forEach((key, value) -> {
                    if (totalResources.containsKey(key)) totalResources.put(key, totalResources.get(key) + value);
                    else totalResources.put(key, value);
                });

                boolean hasAllTheRequiredResources = temp.keySet().stream().map(key -> {
                    if (!totalResources.containsKey(key)) return false;
                    else return totalResources.get(key) >= temp.get(key);
                }).reduce(true, (a, b) -> a && b);

                if (!hasAllTheRequiredResources) {
                    out.println("YOU DON'T HAVE ENOUGH OF THESE OPTIONAL CHOSEN RESOURCES IN YOUR DEPOSITS, WOULD YOU LIKE TO RETRY OR QUIT? INSERT RETRY/ QUIT \n");
                    String input = in.next();
                    if (input.equalsIgnoreCase("QUIT")) {
                        validResourcesChosen = true;
                    }
                } else {
                    required.putAll(temp);
                    validResourcesChosen = true;
                }
            }
        }

        if (!emptyRequired && freeChoicesResources != 0) {
            builder.append("SO NOW, THE RESOURCES YOU HAVE TO TAKE FROM THE DEPOSITS HAVE BECOME THE FOLLOWING: \n");
            builder.append(displayResourcesInHashMap(required));
        }
        out.println(builder);


        //until there will be resources to take, the user will be asked to chose where to take from
        while (!isEmptyHashMap(required)) {
            //ASK WHICH RESOURCE
            ArrayList<Resource> justResources = new ArrayList<>(required.keySet());
            ArrayList<Pair<String, String>> finalResourcesOptions = justResources.stream().map(res -> new Pair<>(res.toString() + " " + shapeResource(res), colorResource(res))).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> inputs2 = displaySelectionFormVariableChoices(finalResourcesOptions, null, finalResourcesOptions.size(), "CHOOSE WHICH RESOURCES TO TAKE \n ");

            inputs2.forEach(index -> {
                //ask where to take resources from
                ArrayList<Pair<String, String>> depositCategory = new ArrayList<>();
                depositCategory.add(new Pair<>("WAREHOUSE", Color.WHITE.getAnsiCode()));
                if (thisDepositLeaderPowers.size() > 0 && leaderDepositContainsResourceType(thisDepositLeaderPowers, justResources.get(index)))
                    depositCategory.add(new Pair<>("EXTRA DEPOSITS", Color.WHITE.getAnsiCode()));
                if (!isEmptyHashMap(strongBoxHashMap) && strongBoxHashMap.containsKey(justResources.get(index)) && strongBoxHashMap.get(justResources.get(index)) != 0)
                    depositCategory.add(new Pair<>("STRONGBOX", Color.WHITE.getAnsiCode()));
                ArrayList<Integer> inputs = displaySelectionFormVariableChoices(depositCategory, null, depositCategory.size(), "WHERE WOULD YOU LIKE TO TAKE THE "+ colorResource(justResources.get(index)) + justResources.get(index).toString() + " " + shapeResource(justResources.get(index)) + Color.reset() +" FROM?\n ");

                inputs.forEach(input -> {
                    if (required.containsKey(justResources.get(index))) {
                        out.print("HAVE A LOOK AT THE CURRENT TOTAL QUANTITY OF RESOURCES IN THE " + Color.RED.getAnsiCode() + depositCategory.get(input).getKey() + Color.reset() + "\n");

                        switch (input) {
                            case 0 -> out.println(displayResourcesInHashMap(warehouseHashMap));
                            case 1 -> {
                                if (thisDepositLeaderPowers.size() > 0 && leaderDepositContainsResourceType(thisDepositLeaderPowers, justResources.get(index)))
                                    out.println(displayResourcesInHashMap(leaderDepositsHashMap));
                                else out.println(displayResourcesInHashMap(strongBoxHashMap));
                            }
                            case 2 -> out.println(displayResourcesInHashMap(strongBoxHashMap));
                        }
                        out.println("HOW MANY " + colorResource(justResources.get(index)) + justResources.get(index).toString() + " " + shapeResource(justResources.get(index)) + Color.reset() + " WOULD YOU LIKE TO REMOVE FROM THE " + Color.RED.getAnsiCode() + depositCategory.get(input).getKey() + Color.reset() + "?  INSERT A NUMBER\n");
                        int quantityToTake = required.get(justResources.get(index));
                        boolean successful;
                        boolean validInput = false;
                        int chosenNumber = -1;
                        String inp = in.nextLine();
                        while (!validInput) {
                            if (!inp.matches("-?\\d+")) {
                                out.println("YOU HAVE TO INSERT NUMBERS");
                                inp = in.nextLine();
                                continue;
                            }
                            chosenNumber = Integer.parseInt(inp);
                            if (chosenNumber < 0 || quantityToTake - chosenNumber < 0) {
                                out.println("YOU HAVE " + quantityToTake + " " + finalResourcesOptions.get(index).getKey() + " SO CHOSE A NUMBER BETWEEN 0 AND " + quantityToTake + "\n");
                                inp = in.nextLine();
                                continue;
                            }
                            validInput = true;
                        }

                        switch (input) {
                            case 0 -> {//WAREHOUSE

                                if (!currentDepotStates.stream().map(depot -> depot.getCurrentQuantity() != 0 && depot.getResourceType() == justResources.get(index)).reduce(false, (prev, foll) -> prev || foll)) {
                                    out.println(DepotResultMessage.INVALID_RES_WAREHOUSE.getMessage());
                                    successful = DepotResultMessage.INVALID_RES_WAREHOUSE.getSuccessful();
                                    out.println("QUANTITY OF RESOURCES IN THE WAREHOUSE REMAINS THE SAME\n");

                                } else if (chosenNumber > warehouseHashMap.get(justResources.get(index))) {
                                    out.println(DepotResultMessage.REACHED_MIN_CAP_WAREHOUSE.getMessage());
                                    successful = DepotResultMessage.REACHED_MIN_CAP_WAREHOUSE.getSuccessful();
                                    out.println("QUANTITY OF RESOURCES IN THE WAREHOUSE REMAINS THE SAME\n");

                                } else {
                                    out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
                                    successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessful();
                                    chosenFromWarehouse.put(justResources.get(index), chosenFromWarehouse.get(justResources.get(index)) + chosenNumber);
                                    allChosen.put(justResources.get(index), allChosen.get(justResources.get(index)) + chosenNumber);
                                    warehouseHashMap.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);

                                }
                                out.println("WHAT REMAINS IN WAREHOUSE:\n");
                                out.println(displayResourcesInHashMap(warehouseHashMap));
                            }
                            case 1 -> {//DEPOSIT LEADER POWER
                                if (thisDepositLeaderPowers.size() > 0 && leaderDepositContainsResourceType(thisDepositLeaderPowers, justResources.get(inputs2.get(0)))) {
                                    if (chosenNumber > leaderDepositsHashMap.get(justResources.get(index))) {
                                        out.println(DepotResultMessage.UNSUCCESSFUL_SUB_FROM_LEADER.getMessage());
                                        successful = DepotResultMessage.UNSUCCESSFUL_SUB_FROM_LEADER.getSuccessful();
                                        out.println("QUANTITY OF RESOURCES IN THE LEADER DEPOSITS REMAINS THE SAME\n");
                                    } else {
                                        out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
                                        successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessful();
                                        chosenFromWarehouse.put(justResources.get(index), chosenFromWarehouse.get(justResources.get(index)) + chosenNumber);
                                        allChosen.put(justResources.get(index), allChosen.get(justResources.get(index)) + chosenNumber);
                                        leaderDepositsHashMap.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);
                                    }
                                    out.println("WHAT REMAINS IN DEPOSIT LEADER POWER:\n");
                                    out.println(displayResourcesInHashMap(leaderDepositsHashMap));
                                } else {
                                    successful = doCase2InSwitch(justResources.get(index), chosenNumber, strongBoxHashMap, chosenFromStrongbox, leaderDepositsHashMap, allChosen, required);
                                }
                            }
                            case 2 -> //STRONGBOX
                                    successful = doCase2InSwitch(justResources.get(index), chosenNumber, strongBoxHashMap, chosenFromStrongbox, leaderDepositsHashMap, allChosen, required);

                            default -> throw new IllegalStateException("Unexpected value: " + input);
                        }//end of switch
                        if (successful)
                            required.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);
                        if (required.get(justResources.get(index)) == 0) required.remove(justResources.get(index));

                    }

                });
            });

            if (required.keySet().stream().map(resource -> required.get(resource) == 0).reduce(true, (prev, foll) -> prev && foll))
                out.println("YOU'RE DONE, ALL RESOURCES HAVE BEEN TAKEN\n");
            else {
                out.println("YOU STILL HAVE SOME RESOURCES LEFT TO TAKE FROM DEPOSITS");
                out.println(displayResourcesInHashMap(required));
            }
        }


        out.println("THIS IS THE FINAL RESULT OF YOUR CHOICE :\n");
        out.println("Resources chosen from Warehouse: \n");
        out.println(displayResourcesInHashMap(chosenFromWarehouse));
        out.println("Resources chosen from Leader deposits: \n");
        out.println(displayResourcesInHashMap(chosenFromLeaderPower));
        out.println("Resources chosen from Strongbox: \n");
        out.println(displayResourcesInHashMap(chosenFromStrongbox));
        out.println(" ARE YOU SATISFIED WITH YOUR CHOICES AND WANT TO MOVE FORWARD? Y/N\n");
        String answer = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(answer)) {
            return new ChosenResourcesEvent(thisPlayer, allChosen, chosenFromWarehouse, chosenFromLeaderPower);
        } else return askWhereToTakeResourcesFrom(clonedRequired, freeChoicesResources);
    }

    /**
     * Method used to choose resources from a set of possible types of resources and given a quantity that must be chosen.
     *
     * @param requiredResourcesOFChoice quantity of resources to chose.
     * @param allowedResourcesTypes     allowed types of resources.
     * @return hashmap with resource chosen-quantity.
     */
    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<
            Resource> allowedResourcesTypes) {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        for (Resource r : allowedResourcesTypes) {
            options.add(new Pair<>(shapeResource(r), colorResource(r)));
        }

        ArrayList<Integer> choices = displaySelectionFormMultipleChoices(options, null, requiredResourcesOFChoice, "");

        HashMap<Resource, Integer> ret = new HashMap<>();
        for (Integer i : choices) {
            Resource r = allowedResourcesTypes.get(i);
            ret.put(r, ret.getOrDefault(r, 0) + 1);
        }
        return ret;
    }

    /**
     * Method used to print the description of an action taken from the enum Action.
     *
     * @param action the Action.
     */
    @Override
    public void displayIAAction(SoloActionToken action) {
        out.println(action.description());
    }

    /**
     * Method to display the  end of game in single player match
     */
    @Override
    public void displaySinglePlayerLost() {
        out.println(Color.RED.getAnsiCode() + "YOU LOST, LORENZO IL MAGNIFICO WON!" + Color.reset());
    }

    /**
     * Method to update Lorenzo' position in the single player match.
     *
     * @param position the  new position.
     */
    @Override
    public void updateLorenzoPosition(int position) {
        faithTrack.updateLorenzoPosition(position);
    }

    //DisplayCantAffordError
  /*public static void main(String[] args) {
        DepotState depot = new DepotState(Resource.COIN, 3, 2);
        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
        ArrayList<DepotState> totalLevels = new ArrayList();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>();
        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add("DevCard16");

        String player = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, player);
        String id= "DevCard10";
        out.println("LISA"+ ", IT SEEMS YOU DON'T HAVE THE REQUIRED RESOURCES TO BUY "+id.toUpperCase()+", TRY AGAIN");
        DrawableObject obj= new DrawableObject(new DevCardView(id).toString(),50, 0);
        DrawableObject resources= new DrawableObject(d.resourceNumberToString(),0, (int)obj.getHeight()+3 );
        Panel panel= new Panel(250, (int)obj.getHeight()+(int)resources.getHeight()+4, out);
        panel.addItem(obj);
        panel.addItem(resources);
        panel.show();
    }*/

    //DisplaySelectionForm
    /*private static String generateFormORIGINAL(ArrayList<Pair<String, String>> option_itsColor, String resetColor) {
        StringBuilder builder = new StringBuilder();
        AtomicInteger n = new AtomicInteger(1);
        option_itsColor.stream().forEach(option -> {

            builder.append("╔═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╗  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            String color = option.getValue();
            builder.append("║ " + color);
            builder.append(option.getKey());
            builder.append(resetColor + " ║  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            builder.append("╚═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╝  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            builder.append("  " + resetColor);
            builder.append(n.get());
            IntStream.range(0, option.getKey().length() - 1).forEach(letter -> builder.append(" "));
            builder.append(resetColor + "    ");
            n.getAndIncrement();
        });
        builder.append("\n");

        return builder.toString();
    }*/


// test of displaySelectionForm()

   /* public static void main(String[] args) {
        int numberOfOptionsToChose = 1;
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        ArrayList<String> resourceTypes = new ArrayList<>();
//        options.add(new Pair<String, String>(shapeResource(Resource.COIN), colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>(shapeResource(Resource.SHIELD), colorResource(Resource.SHIELD)));
//        options.add(new Pair<String, String>(shapeResource(Resource.SERVANT), colorResource(Resource.SERVANT)));
//        options.add(new Pair<String, String>(shapeResource(Resource.COIN), colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>(shapeResource(Resource.ROCK), colorResource(Resource.ROCK)));
//
//        options.add(new Pair<String, String>("FRANK", colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>("GIO", colorResource(Resource.SHIELD)));
//        options.add(new Pair<String, String>("PETER 200", colorResource(Resource.SERVANT)));
//        options.add(new Pair<String, String>("lIZ", colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>("katy100", colorResource(Resource.ROCK)));

        Panel displayPanel = new Panel(1000, 15, System.out);
        IntStream.range(1, 4).forEach(n -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + n);
            options.add(new Pair<String, String>("LeaderCard" + n, Color.BLUE.getAnsiCode()));
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n - 1)+30, 0);
            displayPanel.addItem(obj1);
        });


        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append("YOU HAVE TO CHOOSE " + numberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        AtomicInteger n = new AtomicInteger(1);
        options.stream().forEach(option -> {

            builder.append("╔═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╗  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            String color = option.getValue();
            builder.append("║ " + color);
            builder.append(option.getKey());
            builder.append(resetColor + " ║  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            builder.append("╚═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╝  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            builder.append("  " + resetColor);
            builder.append(n.get());
            IntStream.range(0, option.getKey().length() - 1).forEach(letter -> builder.append(" "));
            builder.append(resetColor + "    ");
            n.getAndIncrement();
        });
        builder.append("\n");

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = n.intValue() - 1;
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        int input;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
                input = in.nextInt();
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
                } else if (input < 1 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else {
                    inputs.add(input);
                    m--;
                }

        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();

        inputs.forEach(index -> {
            builder2.append(index + " -> " + options.get(index-1).getKey() + "\n");
        });
        System.out.println(builder2.toString());

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (isAffirmative(response)) {
            //generate response event
        }

    }*/

    // displayFaithTrack
  /*  public static void main(String[] args) {
        String player = "PAOLO";

        ArrayList<String> ids = new ArrayList<>();
        ids.add("LeaderCard10");
        ids.add("LeaderCard11");
        ids.add("LeaderCard12");
        ids.add("LeaderCard13");

        ArrayList<Integer> indexes= ids.stream().map(name->Integer.parseInt(name.substring(10))).collect(Collectors.toCollection(ArrayList::new));

        int numberOFLeaderCardsToChose=3;

        out.println(player.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChose + " AMONG THESE LEADER CARDS:");
        Panel panel = new Panel(1000, 15, out);
        AtomicInteger n = new AtomicInteger();
        ids.stream().forEach(name -> {
            LeaderCardView card = new LeaderCardView(name);
            DrawableObject obj1 = new DrawableObject(card.toString(), 50 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel.addItem(obj1);
        });
        panel.show();
        ArrayList<Integer> cardIndexes = new ArrayList<Integer>();
        while (numberOFLeaderCardsToChose > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChose + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            int input=in.nextInt();
            while(input<indexes.get(0) || input > indexes.get(indexes.size()-1)){
                out.println("WRONG NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input=in.nextInt();
            }
            if(cardIndexes.contains(input)){
                out.println("YOU HAVE ALREADY SELECTED THIS NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input=in.nextInt();
            }
            cardIndexes.add(input);
            numberOFLeaderCardsToChose--;
        }
        out.println("YOU HAVE CHOSEN THIS LEADER CARDS:");
        Panel panel2 = new Panel(1000, 15, out);
        n.set(0);
        ArrayList<LeaderCardView> leaderCardsChosen = new ArrayList<>();
        cardIndexes.stream().forEach(index -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + index);
            leaderCardsChosen.add(card);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel2.addItem(obj1);
        });
        panel2.show();

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (isAffirmative(response)) {
           System.out.println("good");
        } else  System.out.println("bad");
    }*/


    /*  public static void main(String[] args) {
          String thisPlayer = "paolo";
          int numberOFResources = 7;
          ArrayList<Resource> resourceType = new ArrayList<>();
          resourceType.add(Resource.COIN);
          resourceType.add(Resource.ROCK);
         // resourceType.add(Resource.SERVANT);
          resourceType.add(Resource.SHIELD);

          HashMap<Resource, Integer> initialResources = new HashMap<>();
          for (Resource res : resourceType) {
              initialResources.put(res, 0);
          }
          out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResources + " RESOURCES AMONG:");
          resourceType.stream().forEach(el->out.println( colorResource(el)+ el.toString().toUpperCase()+"\n"+ Color.reset()));

          for (Resource res : resourceType) {
              if (res != resourceType.get(resourceType.size() - 1) && numberOFResources > 0) {
                  out.println("HOW MANY " +colorResource(res) + res.toString()+ Color.reset()  + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResources + " LEFT)");
                  int n = in.nextInt();
                  while (numberOFResources - n < 0) {
                      out.println("RETRY, HOW MANY "+colorResource(res) + res.toString()+ Color.reset() + "S WOULD YOU LIKE? INSERT A NUMBER");
                      n = in.nextInt();
                  }
                  initialResources.put(res, n);
                  numberOFResources = numberOFResources - n;
              }
          }
          if (numberOFResources >= 0) {
              if (numberOFResources == 0) out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT");
              else {
                  out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT: THESE WILL BE " +colorResource(resourceType.get(resourceType.size() - 1))+ resourceType.get(resourceType.size() - 1).toString()+ Color.reset());
                  initialResources.put(resourceType.get(resourceType.size() - 1), numberOFResources);
              }
          }
          out.println("YOU HAVE CHOSEN THIS RESOURCES:");
          StringBuilder builder = new StringBuilder();

          initialResources.keySet().forEach(resource -> {
              String color = colorResource(resource);
              String shape = shapeResource(resource);
              builder.append(color + resource.toString() + ": ");
              IntStream.range(0, initialResources.get(resource)).forEach(n -> builder.append(color + shape + " "));
              builder.append(Color.reset() + "\n");
          });
          out.println(builder.toString());
          out.println("DO YOU AGREE? yes/no");
          String response = in.next().toUpperCase();
          if (isAffirmative(response)) {
              //put in dashboard event
              out.println("resources put in dashboard");
          }

      }
  */

/* displayLeaderCardNotActiveError
    public static void main(String[] args) {
        String leaderID = "Steve100";
        ArrayList<String> otherPlayersID = new ArrayList<>();
        otherPlayersID.add("Michael");
        otherPlayersID.add("FILIP");
        otherPlayersID.add("GIAN20034");


        System.out.println("THIS IS THE CURRENT STATE OF THE LOBBY:");
        StringBuilder builder = new StringBuilder();
        builder.append(Color.RED.getAnsiCode() + "╔═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╗  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╔═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╗  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "║ ");
        builder.append(leaderID);
        builder.append(" ║  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("║ ");
            builder.append(name);
            builder.append(" ║  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "╚═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╝  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╚═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╝  ");
        });

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(500, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();
    }
*/

    // getWarehouseDisplacement
//    public static void main(String[] args) {
//        HashMap<String, DashBoardView> dashboards = new HashMap<>();
//        DepotState depot = new DepotState(Resource.COIN, 3, 3);
//        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
//        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
//        ArrayList<DepotState> totalLevels = new ArrayList();
//        totalLevels.add(depot);
//        totalLevels.add(depot2);
//        totalLevels.add(depot3);
//
//        HashMap<Resource, Integer> str = new HashMap<>();
//        str.put(Resource.COIN, 6);
//        str.put(Resource.ROCK, 7);
//
//        ArrayList<String> cards = new ArrayList<>();
//        cards.add("DevCard10");
//        cards.add("DevCard40");
//        cards.add("DevCard16");
//
//        String thisPlayer = "PAOLO";
//
//        DashBoardView d = new DashBoardView(cards, str, totalLevels, thisPlayer);
//        dashboards.put(thisPlayer, d);
//
//        HashMap<String, HashMap<String, LeaderCardView>> leaderCards = new HashMap<>();
//        HashMap<String, LeaderCardView> leaderCardsSet = new HashMap<>();
//        LeaderCardView card1 = new LeaderCardView("LeaderCard5");
//        card1.setSelected(true);
//        leaderCardsSet.put("LeaderCard5", card1);
//        LeaderCardView card2 = new LeaderCardView("LeaderCard6");
//        card2.setSelected(true);
//        leaderCardsSet.put("LeaderCard6", card2);
//        leaderCards.put(thisPlayer, leaderCardsSet);
//
//        HashMap<Resource, Integer> resourcesToOrganize = new HashMap<>();
//        resourcesToOrganize.put(Resource.SHIELD, 3);
//        resourcesToOrganize.put(Resource.ROCK, 5);
//        resourcesToOrganize.put(Resource.COIN, 2);
//        resourcesToOrganize.put(Resource.SERVANT, 4);
//
//
//        //Parameters to giveback to the NewResourcesOrganizationEvent
//        ArrayList<DepotState> depotStates;
//        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
//        HashMap<Resource, Integer> discardedResources = new HashMap<>();
//        //-----------------------------------------------------
//        //Image of the current state of depots
//        ArrayList<DepotState> currentDepotStates = dashboards.get(thisPlayer).getWarehouse();
//        //Initially the depotState to give back to model is the current state of this dashboard.
//        depotStates = currentDepotStates;
//        //Takes only the active LeaderCards
//        ArrayList<LeaderCardView> leaderCardViews = leaderCards.get(thisPlayer).values().stream().collect(Collectors.toCollection(ArrayList::new));
//        //Selects only DepositLeaderPowers active
//        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();
//
//        leaderCardViews.stream().forEach(cardView -> {
//            if (cardView.getSelected()) {
//                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
//                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
//                    if (power instanceof DepositLeaderPower)
//                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
//                });
//            }
//        });
//        // Initially the DepositLeaderPower states to give back to the model are the current states of leaderPower.
//
//        //display the resources available for storing
//        StringBuilder builder0 = new StringBuilder();
//        builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
//        resourcesToOrganize.keySet().forEach(resource -> {
//            String color = colorResource(resource);
//            String shape = shapeResource(resource);
//            builder0.append(color + resource.toString() + ": ");
//            IntStream.range(0, resourcesToOrganize.get(resource)).forEach(n -> builder0.append(color + shape + " "));
//            builder0.append(Color.WHITE.getAnsiCode() + " --> " + resourcesToOrganize.get(resource) + "\n");
//        });
//        out.println(builder0.toString());
//        System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE D FOR 'DONE' OTHERWISE TYPE C FOR 'CONTINUE'");
//        if (in.next().toUpperCase().equals("D")) {
//            //GENERATE NEW NewResourcesOrganizationEvent
//            out.println("Done");
//            out.println(depotStates.size());
//            out.println(thisDepositLeaderPowers.size());
//            resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
//            out.println(discardedResources.size());
//
//        } else {
//            // prepare panel to display current state of all deposits.
//            StringBuilder builder = new StringBuilder();
//            builder.append("\n\n");
//            builder.append("THESE ARE THE AVAILABLE PLACES \n:" + dashboards.get(thisPlayer).warehouseToString() + "\n\n");
//            leaderCards.get(thisPlayer).values().forEach(card -> builder.append(card.depositPowersToString()));
//            DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
//            Panel panel = new Panel(1000, (int) obj.getHeight() + 2, out);
//            panel.addItem(obj);
//            panel.show();
//            //this is the moment where the user is asked if they want to switch depot.
//            out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
//            String y_n = in.next();
//            while (y_n.toUpperCase().equals("Y")) {
//                int count = 2;
//                int[] indexes = {-1, -1};
//                DepotResultMessage done;
//
//                out.println("WHICH DEPOTS? INSERT TWO NUMBERS");
//                while (count > 0) {
//                    String inp = in.next();
//                    if (!inp.matches("-?\\d+")) {
//                        out.println("YOU HAVE TO INSERT NUMBERS");
//                        continue;
//                    }
//                    int chosenDepotIndex = Integer.parseInt(inp);
//                    if (chosenDepotIndex - 1 < 0 || chosenDepotIndex > depotStates.size()) {
//                        out.println("THIS DEPOT DOESN'T EXIST. INSERT A NUMBER BETWEEN 1 AND " + depotStates.size());
//                        continue;
//                    }
//                    indexes[2 - count] = chosenDepotIndex - 1;
//                    count--;
//
//                }
//                out.println(indexes[0] + " " + indexes[1]);
//                done = d.switchDepots(indexes[0], indexes[1]);
//                out.println(done.getMessage());
//                out.println(d.warehouseToString());
//                out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
//                y_n = in.next();
//            }
//
//            // select one resource at a time among those available
//            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
//            ArrayList<Resource> justResources = new ArrayList<>();
//
//            resourcesToOrganize.keySet().forEach(resType -> {
//                IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
//                    resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
//                    justResources.add(resType);
//                });
//            });
//            String s = "THESE ARE THE RESOURCES AVAILABLE FOR STORING, LET'S STORE ONE AT A TIME\n";
//            // the chosen resource needs to be assigned to one of the deposits
//            int inputResource = displaySelectionForm(resourcesOptions, null, 1, s).get(0);
//            System.out.println("WHERE DO YOU WANT TO PUT THIS RESOURCE ( " + resourcesOptions.get(inputResource).getValue() + resourcesOptions.get(inputResource).getKey() + Color.reset() + " )?");
//
//            // prepare the selection form for deposits.
//            ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
//            IntStream.range(0, currentDepotStates.size()).forEach(n -> depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType()))));
//
//            IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> {
//                depositOptions.add(new Pair<String, String>("LEADER POWER " + (n + 1), Color.WHITE.getAnsiCode()));
//            });
//
//            //result of transition
//            AtomicBoolean successful = new AtomicBoolean(false);
//            //inputDeposit is the result of the selection of one destination among deposits
//            int inputDeposit = displaySelectionForm(depositOptions, null, 1, "THESE ARE THE AVAILABLE DEPOSITS\n").get(0);
//            //if the selected index belongs to depots...
//            if (inputDeposit < currentDepotStates.size()) {
//                DepotResultMessage result = currentDepotStates.get(inputDeposit).tryAddResource(justResources.get(inputResource));
//                successful.set(result.getSuccessful());
//                out.println(result.getMessage());
//            }
//            //or if it belongs to leader card extra depots...
//            else {
//                int index = inputDeposit - currentDepotStates.size();
//                HashMap<Resource, Integer> resInput = new HashMap<>();
//                resInput.put(justResources.get(inputResource), 1);
//
//                String cardId = thisDepositLeaderPowers.get(index).getCardId();
//                leaderCardViews.stream().forEach(cardView -> {
//                    if (cardView.getIdCard() == thisDepositLeaderPowers.get(index).getCardId()) {
//                        DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), justResources.get(inputResource));
//                        successful.set(result.getSuccessful());
//                        out.println(result.getMessage());
//                    }
//                });
//            }
//            if (successful.get()) {
//                resourcesToOrganize.put(justResources.get(inputResource), (resourcesToOrganize.get(justResources.get(inputResource)) - 1));
//                //displayOrganizeResourcesForm( resourcesToOrganize);
//                out.println(resourcesToOrganize.toString());
//                out.println(currentDepotStates.get(0).getCurrentQuantity());
//                out.println(currentDepotStates.get(1).getCurrentQuantity());
//                out.println(currentDepotStates.get(2).getCurrentQuantity());
//                out.println(card1.getLeaderPowersActive().get(0));
//                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
//                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());
//
//
//            } else {
//                out.println(resourcesToOrganize.toString());
//                out.println(currentDepotStates.get(0).getCurrentQuantity());
//                out.println(currentDepotStates.get(1).getCurrentQuantity());
//                out.println(currentDepotStates.get(2).getCurrentQuantity());
//                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
//                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());
//            }
//        }
//
//    }

    //askForDevCard
    /*public static void main(String[] args) {
        HashMap<String, DashBoardView> dashboards = new HashMap<>();
        DepotState depot = new DepotState(Resource.COIN, 3, 3);
        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
        ArrayList<DepotState> totalLevels = new ArrayList();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>();
        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add("DevCard16");

        String thisPlayer = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, thisPlayer);
        dashboards.put(thisPlayer, d);

        HashMap<String, LeaderCardView> leaderCardsSet = new HashMap<>();
        LeaderCardView card1 = new LeaderCardView("LeaderCard5");
        card1.setSelected(true);
        leaderCardsSet.put("LeaderCard5", card1);
        LeaderCardView card2 = new LeaderCardView("LeaderCard6");
        card2.setSelected(true);
        leaderCardsSet.put("LeaderCard6", card2);

        HashMap<String, PlayerState> playerStates = new HashMap<>();

        PlayerState PL1 = new PlayerState(d, leaderCardsSet);
        PlayerState PL2 = new PlayerState(d, leaderCardsSet);
        PlayerState PL3 = new PlayerState(d, leaderCardsSet);

        playerStates.put("LISA", PL1);
        playerStates.put("PAOLO", PL2);
        playerStates.put("GIGI", PL3);

        String[][] grid0 = {
                {"DevCard1", "DevCard2", "DevCard3", "DevCard10"},
                {"DevCard4", "DevCard5", "DevCard6", "DevCard11"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"},
                {"DevCard13", "DevCard14", "DevCard15", "DevCard16"}
        };
        DevCardGridView devCardGridView = new DevCardGridView(grid0);


        int slot = -1;
        String devCard = "";

        devCardGridView.display();
        ArrayList<Pair<String, String>> gridSelection = new ArrayList<>();
        String[][] grid = devCardGridView.getTopDevCardIDs();
        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                gridSelection.add(new Pair<String, String>(grid[i][j], Color.WHITE.getAnsiCode()));
            }
        }
        int input1 = displaySelectionForm(gridSelection, null, 1, "  DEVELOPMENT CARDS AVAILABLE : \n").get(0);
        devCard = gridSelection.get(input1).getKey();

        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        ArrayList<Pair<String, String>> selectionArray = new ArrayList<>();
        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        AtomicInteger slotIndex = new AtomicInteger(1);
        playerStates.get(thisPlayer).getDashBoard().getTopDevCards().stream().forEach(n -> {
            selectionArray.add(new Pair<String, String>("SLOT " + slotIndex, Color.WHITE.getAnsiCode()));
            slotIndex.getAndIncrement();
        });
        int input2 = displaySelectionForm(selectionArray, null, 1, " THESE ARE THE SLOTS TO WHICH YOU CAN ADD THE BOUGHT CARD : \n").get(0);
        slot = input2;
        BuyDevCardsEvent event = new BuyDevCardsEvent(thisPlayer, devCard, slot);
        out.println(event.getPlayerId() + "\n" + event.getCardSlot() + "\n" + event.getDevCardID());
        out.println("event sent");


    }*/

    // displayEndOfGame


}


