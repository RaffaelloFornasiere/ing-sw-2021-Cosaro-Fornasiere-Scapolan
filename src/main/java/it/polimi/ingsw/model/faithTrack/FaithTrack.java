package it.polimi.ingsw.model.faithTrack;

import java.util.ArrayList;
import java.util.stream.IntStream;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static ArrayList<AbstractCell> arrayOfCells;

    /**
     * constructor
     *
     * @param num           length of faithTrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @throws IndexOutOfBoundsException if the number of cells is different from the length of the array of victory points for the cells
     */
    private FaithTrack(int num, ArrayList<CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException {
        arrayOfCells = new ArrayList<>(num);
        if (num == victoryPoints.size()) {
            IntStream.range(0, num).forEach(n -> arrayOfCells.add(n, new Cell(n, victoryPoints.get(n))));
        } else {
            throw new IllegalArgumentException("length of faithTrack doesn't coincide with victoryPoint array length");
        }

        effects.forEach(e -> {
            if (e.getIndex() > arrayOfCells.size())
                throw new IllegalArgumentException("cell index is out of array length");
            arrayOfCells.set(e.getIndex(), e);
        });
    }

    /**
     * Constructor
     *
     * @param arrayOfCells The complete array of cells, which might be of type Cell, PopeCell, LastCell.
     * @throws IllegalArgumentException If the index of one cell in the array doesn't coincide with private attribute index of the given cell.
     */
    private FaithTrack(ArrayList<AbstractCell> arrayOfCells) throws IllegalArgumentException {
        for (int i = 0; i < arrayOfCells.size(); i++) {
            if (arrayOfCells.get(i).getIndex() != i)
                throw new IllegalArgumentException("The cell with index " + i + " is in the wrong position");
        }

        FaithTrack.arrayOfCells = arrayOfCells;
    }


    /**
     * @param num           length of faithTrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @return the instance of faithTrack
     */
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static FaithTrack initFaithTrack(int num, ArrayList<CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException {
        if (instance == null) instance = new FaithTrack(num, effects, victoryPoints);
        return instance;
    }

    /**
     * @param arrayOfCells The complete array of cells, which might be of type Cell, PopeCell, LastCell.
     * @return the only instance of FaithTrack
     * @throws IllegalArgumentException If the index of one cell in the array doesn't coincide with private attribute index of the given cell.
     */
    public static FaithTrack initFaithTrack(ArrayList<AbstractCell> arrayOfCells) throws IllegalArgumentException {
        if (instance == null) //noinspection InstantiationOfUtilityClass
            instance = new FaithTrack(arrayOfCells);
        return instance;
    }

    /**
     * the length of the faithTrack
     *
     * @return the length of faithTrack
     */
    public static int size() {
        return arrayOfCells.size();
    }


    /**
     * Getter of array of Cells of FaithTrack
     *
     * @return array of Cells of FaithTrack
     */
    public static ArrayList<AbstractCell> getArrayOfCells() {
        return new ArrayList<>(arrayOfCells);
    }

    /**
     * method only used for testing
     */
    public static void resetForTest(){
        instance=null;
    }

    /*public static void main(String[] args) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        ArrayList<CellWithEffect> cellsWithEffectArray = new ArrayList<>();
        try {
            String cellsEffectJSON = Files.readString(Paths.get("src/main/resources/CellsWithEffectArray.json"));
            cellsEffectJSON = cellsEffectJSON.substring(1,cellsEffectJSON.length()-1);
            String[] cells = cellsEffectJSON.split("(,)(?=/{)");

            for (String s : cells) {
                CellWithEffect cell = (CellWithEffect)gson.fromJson(s, AbstractCell.class);
                cellsWithEffectArray.add(cell);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> victoryPoints = new ArrayList<>();
        try {
            String victoryPointsJSON = Files.readString(Paths.get("src/main/resources/VictoryPoints.json"));
            Type integerList = new TypeToken<ArrayList<Integer>>(){}.getType();
            victoryPoints= gson.fromJson(victoryPointsJSON, integerList);
        } catch (IOException e) {
            e.printStackTrace(); //use default configuration
        }
        FaithTrack faithTrack = FaithTrack.initFaithTrack(victoryPoints.size(), cellsWithEffectArray, victoryPoints);

        StringBuilder s= new StringBuilder();
        s.append("["+ gson.toJson(FaithTrack.getArrayOfCells().get(0),AbstractCell.class));
        for(int i=1; i< FaithTrack.getArrayOfCells().size(); i++){
            s.append(","+ gson.toJson(FaithTrack.getArrayOfCells().get(i),AbstractCell.class));
        }
        s.append("]");
        System.out.println(s.toString());
        String path = "src/main/resources/CompleteFaithTrack.json";
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            fw.write(s.toString());
            fw.flush();
            fw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();

        ArrayList<AbstractCell> arrayOfCells = new ArrayList<>();
        try {
            String faithTrackJSON = Files.readString(Paths.get("src/main/resources/CompleteFaithTrack.json"));
            arrayOfCells = gson.fromJson(faithTrackJSON, new TypeToken<ArrayList<AbstractCell>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace(); //use default configuration
        }

        for(AbstractCell c: arrayOfCells){
            System.out.println(c.getIndex() + " " + c.getVictoryPoints());
        }
    }*/
}