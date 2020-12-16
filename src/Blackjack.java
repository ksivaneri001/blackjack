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

        while (true) {
            int payoutMultiplier = 0;

            for (int i = 0; i < 2; i++) {
                player.dealCard(deck.get(0));
                deck.remove(0);
                dealer.dealCard(deck.get(0));
                deck.remove(0);
            }

            System.out.print("\nYour hand: [");
            for (int i = 0; i < player.getHand().size() - 1; i++) {
                System.out.print(player.getHand().get(i).getRank() + ", ");
            }
            System.out.println(player.getHand().get(player.getHand().size() - 1).getRank() + "]");

            System.out.print("\nDealer's hand: [?, ");
            for (int i = 1; i < dealer.getHand().size() - 1; i++) {
                System.out.print(dealer.getHand().get(i).getRank() + ", ");
            }
            System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");


            if (player.addCards() == 21) {
                System.out.println("\nBlackjack!");
            }
            else {
                takeTurn(false);
            }

            break;
        }
    }

    public void takeTurn(boolean cpu) {
        if (!cpu) {
            System.out.println("\nHit or Stand?");
            String hitOrStand = in.nextLine().toLowerCase();

            if (hitOrStand.equals("hit")) {
                player.dealCard(deck.get(0));
                deck.remove(0);

                if (player.addCards() == 21) {
                    System.out.println("\nCards equal 21, you win your payout!");
                    return;
                }
                else if (player.addCards() > 21) {
                    for (int i = 0; i < player.getHand().size(); i++) {
                        player.checkAce(player.getHand().get(i));
                        if (player.addCards() - player.getAceDeduction() == 21) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void shuffle() {
        if (deck == null) {
            deck = new ArrayList<>(52);
            for (String rank : RANKS) {
                for (int i = 0; i < 4; i++) {
                    deck.add(new Card(rank));
                }
            }
        }

        Collections.shuffle(deck);
    }

    public static void main(String[] args) {
        new Blackjack().game();
    }
}
