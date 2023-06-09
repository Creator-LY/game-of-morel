package board;

import cards.Card;

public interface Displayable {
    public void add(Card card);
    public int size();
    public Card getElementAt(int position);
    public Card removeElement(int position);
}
