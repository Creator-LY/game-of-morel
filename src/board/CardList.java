package board;

import java.util.ArrayList;

import cards.Card;

public class CardList {
    private ArrayList<Card> cList;
    
    public CardList() {
        cList = new ArrayList<>();
    }

    public void add(Card card) {
        cList.add(0, card);
    }

    public int size() {
        return cList.size();
    }

    public Card getElementAt(int position) {
        return cList.get(position);
    }

    public Card removeCardAt(int position) {
        return cList.remove(position);
    }
}
