package cards;

public class Mushroom extends EdibleItem {
    protected int stickPerMushroom;

    public Mushroom(CardType type, String cardName) {
        super(type, cardName);
    }

    public int getSticksPerMushroom() {
        return stickPerMushroom;
    }
}
