package board;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.CardType;
import cards.EdibleItem;
import cards.Mushroom;
import cards.Pan;
import cards.Stick;

public class Player {
    private Hand h;
    private Display d;
    private int score;
    private int handlimit;
    private int sticks;

    public Player() {
        h = new Hand();
        d = new Display();
        score = 0;
        handlimit = 8;
        sticks = 0;

        d.add(new Pan());
    }

    public int getScore() {
        return score;
    }

    public int getHandLimit() {
        return handlimit;
    }

    public int getStickNumber() {
        return sticks;
    }

    public void addSticks(int amount) {
        sticks += amount;

        for (int i = 0; i < amount; i++) {
            addCardtoDisplay(new Stick());
        }
    }

    public void removeSticks(int amount) {
        sticks -= amount;

        for (int i = d.size()-1; i >= 0; i--) {
            if (d.getElementAt(i).getType() == CardType.STICK) {
                d.removeElement(i);
                amount -= 1;

                if (amount == 0) {
                    break;
                }
            }
        }
    }

    public Hand getHand() {
        return h;
    }

    public Display getDisplay() {
        return d;
    }

    public void addCardtoHand(Card card) {
        if (card.getType() == CardType.BASKET) {
            addCardtoDisplay(card);
            handlimit += 2;
        } else {
            h.add(card);
        }
    }

    public void addCardtoDisplay(Card card) {
        d.add(card);
    }


    public boolean takeCardFromTheForest(int num) {
        int convertNum = Board.getForest().size()-num;

        if (num > 0 && num < 9) {
            if (h.size() == handlimit && Board.getForest().getElementAt(convertNum).getType() != CardType.BASKET) {
                return false;
            }
            else if (num == 1 || num == 2) {
                addCardtoHand(Board.getForest().removeCardAt(convertNum));
                return true;
            } else {
                if (getStickNumber() >= num-2) {
                    addCardtoHand(Board.getForest().removeCardAt(convertNum));
                    removeSticks(num-2);
                    return true;
                } else { return false; }
            }
        }
        else { return false; }
    }

    public boolean takeFromDecay() {
        int count = 0;

        for (Card card : Board.getDecayPile()) {
            if (card.getType() == CardType.BASKET) {
                count -= 2;
            } else {
                count += 1;
            }
        }

        if ((getHandLimit() - h.size()) >= count) {
            for (Card card : Board.getDecayPile()) {
                addCardtoHand(card);
            }
            Board.getDecayPile().removeAll(Board.getDecayPile());
            return true;
        } else { return false; }
    }

    public boolean cookMushrooms(ArrayList<Card> cards) {
        boolean hasPan = false;
        int PanLocation = -1;
        int useButter = 0;
        int useCider = 0;
        int mushroomAmount = 0;
        String chosenMushroom = "";
        int FlavourForType = 0;

        for (Card card : cards) {
            if (card.getType() == CardType.PAN) {
                hasPan = true;
            }
            else if (card.getType() == CardType.BASKET) {
                return false;
            }
        }
        
        if (!hasPan) {
            for (int i = 0; i < d.size(); i++) {
                if (d.getElementAt(i).getType() == CardType.PAN) {
                    hasPan = true;
                    PanLocation = i;
                    break;
                }
            }
        }

        if (hasPan) {
            for (Card card : cards) {
                if (card.getType() == CardType.DAYMUSHROOM || card.getType() == CardType.NIGHTMUSHROOM) {
                    if (chosenMushroom == "") {
                        FlavourForType = ((EdibleItem)card).getFlavourPoints();
                        chosenMushroom = card.getName();
                    } else {
                        if (!chosenMushroom.equals(card.getName())) {
                            return false;
                        }
                    }
                    if (card.getType() == CardType.DAYMUSHROOM) {
                        mushroomAmount += 1;
                    } else {
                        mushroomAmount += 2;
                    }  
                } else if (card.getType() == CardType.BUTTER) {
                    useButter += 1;
                } else if (card.getType() == CardType.CIDER) {
                    useCider += 1;
                }
            }
            if ((mushroomAmount > 2) && (mushroomAmount >= (useButter * 4) + (useCider * 5))) {
                score += mushroomAmount * FlavourForType + useButter * 3 + useCider * 5;

                for (Card card : cards) {
                    for (int i = h.size()-1; i >= 0 ; i--) {
                        if (card == h.getElementAt(i)) {
                            h.removeElement(i);
                            break;
                        }
                    }
                }
                if (PanLocation != -1) {
                    d.removeElement(PanLocation);
                }

                return true;
            }
        }
        
        return false;
    }

    public boolean sellMushrooms(String cardName, int amount) {
        ArrayList<String> mushrooms = new ArrayList<String>() {
            {
                add("birchbolete");
                add("chanterelle");
                add("henofwoods");
                add("honeyfungus");
                add("lawyerswig");
                add("morel");
                add("porcini");
                add("shiitake");
                add("treeear");
            }
        };
        String formatName = cardName.replaceAll("['\\s+]", "").toLowerCase();
        int count = 0;
        List<Integer> dayCards = new ArrayList<>();
        List<Integer> nightCards = new ArrayList<>();

        if (amount > 1 && mushrooms.contains(formatName)) {
            for (int i = 0; i < h.size(); i++) {
                if (h.getElementAt(i).getName().equals(formatName)) {
                    if (h.getElementAt(i).getType() == CardType.NIGHTMUSHROOM) {
                        count += 2;
                        nightCards.add(i);
                    } else {
                        count += 1;
                        dayCards.add(i);
                    }
                }
            }
            if (count >= amount) {
                int stickForType = 0;

                if (dayCards.size() != 0) {
                    stickForType = ((Mushroom)h.getElementAt(dayCards.get(0))).getSticksPerMushroom();
                } else {
                    stickForType = ((Mushroom)h.getElementAt(nightCards.get(0))).getSticksPerMushroom();
                }
                
                int addedStick = amount * stickForType;
                addSticks(addedStick);
                
                if (dayCards.size() <= amount) {
                    int n = 0;
                    for (int index : dayCards) {
                        h.removeElement(index-n);
                        n++;
                    }

                    amount -= dayCards.size(); 
                    
                    if (amount != 0) {
                        if (dayCards.size() == 0) {
                            int i = 0;
                            while (amount > 0) {
                                h.removeElement(nightCards.get(i)-n);
                                amount -= 2;
                                i++;
                                n++;
                            }
                        } else {
                            for (int i = h.size()-1; i >= 0; i--) {
                                if ((h.getElementAt(i).getName().equals(formatName)) && (h.getElementAt(i).getType() == CardType.NIGHTMUSHROOM)) {
                                    h.removeElement(i);
                                    amount -=2;
                                    
                                    if (amount <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                        
                    }
                } else {
                    int i = 0;
                    while (amount > 0) {
                        h.removeElement(dayCards.get(i)-i);
                        amount -= 1;
                        i++;
                    }
                }
                
                return true;
            }
        }

        return false;
    }

    public boolean putPanDown() {
        for (int i = 0; i < h.size(); i++) {
            if (h.getElementAt(i).getType() == CardType.PAN) {
                addCardtoDisplay(h.removeElement(i));
                return true;
            }
        }
        return false;
    }
}
