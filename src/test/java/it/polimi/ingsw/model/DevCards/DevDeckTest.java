package it.polimi.ingsw.model.DevCards;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DevDeckTest {

    ArrayList<DevCard> cards;
    Gson gson = new Gson();

    @Before
    public void init() {

        cards = new ArrayList<>();
        try {
            for (int i = 1; i < 49; i++) {
                String cardJSON = Files.readString(Paths.get("src\\main\\resources\\DevCard" + i + ".json"));
                if (cardJSON.contains("\"level\":2") && cardJSON.contains("\"color\":\"VIOLET\""))
                    cards.add(gson.fromJson(cardJSON, DevCard.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DevDeckConstructorTest() {
        new DevDeck(cards);
        DevCard newCard = null;
        try {
            newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard21.json")), DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cards.add(newCard);
        try {
            new DevDeck(cards);
        } catch (IllegalArgumentException ignore) {
        }
        cards.remove(newCard);
        try {
            newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard2.json")), DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cards.add(newCard);
        try {
            new DevDeck(cards);
        } catch (IllegalArgumentException ignore) {
        }

    }

    @Test
    public void testEquals() {
        DevDeck devDeck = new DevDeck(cards);
        Assert.assertEquals(devDeck, new DevDeck(cards));

    }

    @Test
    public void testPushPopRemove() {
        DevDeck devDeck = new DevDeck(cards);
        DevCard newCard;
        try {
            newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard2.json")), DevCard.class);
            try {
                devDeck.push(newCard);
                Assert.fail();
            } catch (IllegalArgumentException ignore) {
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard21.json")), DevCard.class);
            try {
                devDeck.push(newCard);
                Assert.fail();
            } catch (IllegalArgumentException ignore) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            newCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard26.json")), DevCard.class);
            try {
                devDeck.push(newCard);
                Assert.assertEquals(devDeck.pop(), newCard);
            } catch (IllegalArgumentException | NotPresentException ignore) {
                Assert.fail();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    @Test
    public void testTopCard() {
        DevDeck devDeck = new DevDeck(cards);
        try {
            Assert.assertEquals(cards.get(cards.size()-1), devDeck.topCard());
        } catch (NotPresentException e) {
            e.printStackTrace();
        }
        while(devDeck.size() > 0) {
            try {
                devDeck.pop();
            } catch (NotPresentException e) {
                e.printStackTrace();
            }
        }
        try {
            devDeck.topCard();
            Assert.fail();
        } catch (NotPresentException ignore) {

        }
    }

    @Test
    public void testSize() {
        DevDeck devDeck = new DevDeck(cards);
        Assert.assertEquals(devDeck.size(), cards.size());
    }

    @Test
    public void testGetLevel() {
        DevDeck devDeck = new DevDeck(cards);
        Assert.assertEquals(2, devDeck.getLevel());
    }

    @Test
    public void testGetColor() {
        DevDeck devDeck = new DevDeck(cards);
        Assert.assertEquals(CardColor.VIOLET, devDeck.getColor());
    }
}