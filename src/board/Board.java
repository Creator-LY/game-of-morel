package board;

import java.util.ArrayList;

import cards.Basket;
import cards.BirchBolete;
import cards.Butter;
import cards.Card;
import cards.CardType;
import cards.Chanterelle;
import cards.Cider;
import cards.HenOfWoods;
import cards.HoneyFungus;
import cards.LawyersWig;
import cards.Morel;
import cards.Pan;
import cards.Porcini;
import cards.Shiitake;
import cards.TreeEar;

public class Board {
    private static CardPile forestCardsPile;
    private static CardList forest;
    private static ArrayList<Card> decayPile;

    public static void initialisePiles() {
        forestCardsPile = new CardPile();
        forest = new CardList();
        decayPile = new ArrayList<Card>();
    }

    public static void setUpCards() {
        //day mushrooms
        for (int i = 0; i < 10; i++) {
            forestCardsPile.addCard(new HoneyFungus(CardType.DAYMUSHROOM));
        }
        for (int i = 0; i < 8; i++) {
            forestCardsPile.addCard(new TreeEar(CardType.DAYMUSHROOM));
        }
        for (int i = 0; i < 6; i++) {
            forestCardsPile.addCard(new LawyersWig(CardType.DAYMUSHROOM));
        }
        for (int i = 0; i < 5; i++) {
            forestCardsPile.addCard(new Shiitake(CardType.DAYMUSHROOM));
            forestCardsPile.addCard(new HenOfWoods(CardType.DAYMUSHROOM));
        }
        for (int i = 0; i < 4; i++) {
            forestCardsPile.addCard(new BirchBolete(CardType.DAYMUSHROOM));
            forestCardsPile.addCard(new Porcini(CardType.DAYMUSHROOM));
            forestCardsPile.addCard(new Chanterelle(CardType.DAYMUSHROOM));
        }
        for (int i = 0; i < 3; i++) {
            forestCardsPile.addCard(new Morel(CardType.DAYMUSHROOM));
        }

        //night mushrooms
        forestCardsPile.addCard(new HoneyFungus(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new TreeEar(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new LawyersWig(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new Shiitake(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new HenOfWoods(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new BirchBolete(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new Porcini(CardType.NIGHTMUSHROOM));
        forestCardsPile.addCard(new Chanterelle(CardType.NIGHTMUSHROOM));

        //other cards
        for (int i = 0; i < 3; i++) {
            forestCardsPile.addCard(new Butter());
            forestCardsPile.addCard(new Cider());
        }
        for (int i = 0; i < 11; i++) {
            forestCardsPile.addCard(new Pan());
        }
        for (int i = 0; i < 5; i++) {
            forestCardsPile.addCard(new Basket());
        } 
    }

    public static CardPile getForestCardsPile() {
        return forestCardsPile;
    }

    public static CardList getForest() {
        return forest;
    }

    public static ArrayList<Card> getDecayPile() {
        return decayPile;
    }

    public static void updateDecayPile() {
        if (decayPile.size() == 4) {
            decayPile.removeAll(decayPile);
            decayPile.add(forest.getElementAt(forest.size()-1));
            forest.removeCardAt(forest.size()-1);
        } else {
            decayPile.add(forest.getElementAt(forest.size()-1));
            forest.removeCardAt(forest.size()-1);
        }
    }
}
