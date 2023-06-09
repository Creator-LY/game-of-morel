package board;

import java.util.ArrayList;

import cards.Card;

public class Hand implements Displayable {
    private ArrayList<Card> handList = new ArrayList<Card>();
    
    public void add(Card card) {
        handList.add(card);
    }

    public int size() {
        return handList.size();
    }

    public Card getElementAt(int position) {
        return handList.get(position);
    }

    public Card removeElement(int position) {
        return handList.remove(position);
    }
}
