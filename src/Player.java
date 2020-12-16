import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;
    private int chips;
    private int aceDeduction;

    public Player() {
        this.hand = new ArrayList<>();
        this.chips = 0;
        this.aceDeduction = 0;
    }

    public List<Card> getHand() {
        return hand;
    }


    public int getChips() {
        return chips;
    }

    public int getAceDeduction() {
        return aceDeduction;
    }

    public void dealCard(Card card) {
        hand.add(card);
    }

    public int addCards() {
        int total = 0;

        for (int i = 0; i < hand.size(); i++) {
            total += Card.getCardValue(hand.get(i));
        }

        return total;
    }

    public void checkAce(Card card) {
        if (card.getRank().equals("A")) {
            aceDeduction += 10;
        }
    }
}
