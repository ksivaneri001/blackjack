import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Blackjack {
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player dealer;
    private List<Card> deck;
    private final Scanner in;

    public Blackjack() {
        this.player = new Player();
        this.dealer = new Player();
        this.in = new Scanner(System.in);
    }

    public void game() {
        shuffle();
    }

    public void shuffle() {
        if (deck == null) {
            deck = new ArrayList<>(52);
            for (String rank : RANKS) {
                deck.add(new Card(rank));
            }
        }

        Collections.shuffle(deck);
    }

    public static void main(String[] args) {
        new Blackjack().game();
    }
}
