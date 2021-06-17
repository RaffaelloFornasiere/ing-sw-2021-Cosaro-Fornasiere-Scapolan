package it.polimi.ingsw.model.DevCards;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DevCardGridTest {
    ArrayList<DevCard> cards;
    Gson gson = new Gson();
    @Before
    public void init() {

        cards = new ArrayList<>();
        try {
            for (int i = 1; i < 49; i++) {
                String cardJSON = Files.readString(Paths.get("src\\main\\resources\\DevCard" + i + ".json"));
                cards.add(gson.fromJson(cardJSON, DevCard.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDevCardGrid() {
        try {
            DevCardGrid grid = new DevCardGrid(null);
            Assert.fail();
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test
    public void getDecks() {
        DevCardGrid grid = new DevCardGrid(cards);
        if (cards == null || cards.size() <= 0)
            throw new IllegalArgumentException("can construct grid from a null or a void array");
        int levels = cards.stream().mapToInt(DevCard::getLevel).max().getAsInt();
        DevDeck[][] decks = new DevDeck[levels][CardColor.values().length];

        IntStream.range(1, levels + 1).forEach(i -> {
            for (CardColor color : CardColor.values()) {
                decks[i - 1][color.getCode()] =
                        new DevDeck(cards.stream().filter(c -> c.getLevel() == i && c.getColor() == color).collect(Collectors.toCollection(ArrayList::new)));
            }
        });

        if(!Arrays.deepEquals(decks, grid.getDecks()))
            Assert.fail();
    }

    @Test
    public void getColumnsNumber() {
        DevCardGrid grid = new DevCardGrid(cards);
        Assert.assertEquals(4, grid.getColumnsNumber());

    }

    @Test
    public void getRowsNumber() {
        DevCardGrid grid = new DevCardGrid(cards);

        Assert.assertEquals(3, grid.getRowsNumber());
    }

    @Test
    public void testPushPop() {
        DevCardGrid grid = new DevCardGrid(cards);

        DevCard newCard = null;
        try {
             newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard3.json")), DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        grid.push(newCard, 0, 0);
        grid.push(newCard, new Pair<>(0, 0));
        try {
            grid.push(newCard, 100, 1);

        }catch (IllegalArgumentException ignore){}
        try {
            grid.push(newCard, 1, 100);
        }catch (IllegalArgumentException ignore){}




        try {
            Assert.assertEquals(newCard, grid.pop(0, 0));
            Assert.assertEquals(newCard, grid.pop(new Pair<>(0,0)));
        } catch (NotPresentException ignore) {
            Assert.fail();
        }
    }

    @Test
    public void testPop() {
        DevCardGrid grid = new DevCardGrid(cards);
        try {
            grid.pop(10, 2);
            Assert.fail();
        } catch (NotPresentException | IllegalArgumentException ignore) {
        }

        try {
            grid.pop(1, 10);
        } catch (NotPresentException | IllegalArgumentException ignore) {
        }
    }

    @Test
    public void testTopCard() {
        DevCardGrid grid = new DevCardGrid(cards);
        try {
            grid.topCard(new Pair<>(10, 2));
            Assert.fail();
        } catch (IllegalArgumentException | NotPresentException ignore) {
        }

        try {
            grid.topCard(1, 10);
        } catch (NotPresentException | IllegalArgumentException ignore) {
        }


        try {
            DevCard newCard = null;
            try {
                newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard3.json")), DevCard.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            grid.push(newCard, 0, 0);
            Assert.assertEquals(grid.topCard(0, 0), newCard);
        } catch (NotPresentException e) {
            Assert.fail();
        }
    }


    @Test
    public void testGetRowColOfCardFromID() {
        String cardJSON = null;
        Gson gson = new Gson();
        try {
            cardJSON = Files.readString(Paths.get("src\\main\\resources\\DevCard3.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DevCard card = gson.fromJson(cardJSON, DevCard.class);
        DevCardGrid grid = new DevCardGrid(cards);
        grid.push(card, 0, 0);
        try {
            Assert.assertEquals(grid.getRowColOfCardFromID("DevCard3"), new Pair<>(0, 0));
        } catch (NotPresentException ignore) {
            Assert.fail();
        }
        try {
            Assert.assertEquals(grid.getRowColOfCardFromID("DevCard4"), new Pair<>(0, 0));
            Assert.fail();
        } catch (NotPresentException ignore) {
        }


    }
}