import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;
    private int chips;

    public Player() {
        this.hand = new ArrayList<>();
        this.chips = 0;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getChips() {
        return chips;
    }

    public void dealCard(Card card) {
        hand.add(card);
    }

    public int addCards(Player player) {
        int total = 0;

        for (int i = 0; i < player.getHand().size(); i++) {
            total += Card.getCardValue(player.hand.get(i));
        }

        return total;
    }
}
