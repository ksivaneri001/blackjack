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

            if (deck.size() < 4) {
                break;
            }
            for (int i = 0; i < 2; i++) {
                player.dealCard(deck.get(0));
                deck.remove(0);
                dealer.dealCard(deck.get(0));
                deck.remove(0);
            }


            for (Card card : deck) {
                System.out.print(card.getRank() + " ");
            }
            System.out.println(deck.size());

            System.out.print("\nYour hand: [");
            for (int i = 0; i < player.getHand().size() - 1; i++) {
                System.out.print(player.getHand().get(i).getRank() + ", ");
            }
            System.out.println(player.getHand().get(player.getHand().size() - 1).getRank() + "]");

            System.out.print("Dealer's hand: [?, ");
            for (int i = 1; i < dealer.getHand().size() - 1; i++) {
                System.out.print(dealer.getHand().get(i).getRank() + ", ");
            }
            System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

            if (player.addCards() == 21) {
                System.out.println("\nBlackjack!");
            }
            else {
                takeTurn(false);

                if (!player.getBust()) {
                    System.out.print("\nDealer's hand: [");
                    for (int i = 0; i < dealer.getHand().size() - 1; i++) {
                        System.out.print(dealer.getHand().get(i).getRank() + ", ");
                    }
                    System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

                    takeTurn(true);
                }

                if (!player.getBust() && !dealer.getBust()) {
                    System.out.println("\nYour total is: " + (player.addCards() - player.getAceDeduction()));
                    System.out.println("compared to");
                    System.out.println("The dealer's total of: " + (dealer.addCards() - dealer.getAceDeduction()));
                }

                if (player.getBust()) {
                    System.out.println("\nYou lose...");
                }
                else if (dealer.getBust()) {
                    System.out.println("\nYou win!");
                }
                else if (player.addCards() - player.getAceDeduction() > dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou win!");
                }
                else if (player.addCards() - player.getAceDeduction() <= dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou lose...");
                }
            }

            player.setBust(false);
            dealer.setBust(false);
            player.aceDeductionZero();
            dealer.aceDeductionZero();
            player.clearHand();
            dealer.clearHand();
            System.out.println("\n#####################");
            System.out.println("#### -NEXT TURN- ####");
            System.out.println("#####################");
        }

        endGame();
    }

    public void takeTurn(boolean cpu) {
        if (!cpu) {
            player.aceDeductionZero();
            if (player.addCards() > 21) {
                for (int i = 0; i < player.getHand().size(); i++) {
                    player.checkAce(player.getHand().get(i));
                    if (player.addCards() - player.getAceDeduction() < 21) {
                        break;
                    }
                }
            }
            System.out.println("Your current card total: " + (player.addCards() - player.getAceDeduction()));

            if (player.addCards() - player.getAceDeduction() == 21) {
                System.out.println("\nCards equal 21, you must stand!"); // Stop hitting because the total equals 21
                return;
            }
            else if (player.addCards() - player.getAceDeduction() > 21) {
                player.setBust(true);
                System.out.println("\nIt's a bust. How pathetic."); // Bust
                return;
            }
//            else if (deck.size() == 0) {
//                System.out.println("\nCards equal 21, you must stand!"); // Prevents code from throwing an error
//                return;
//            }

            String hitOrStand = "";
            do {
                System.out.println("\nHit or Stand?");
                hitOrStand = in.nextLine().toLowerCase();
            } while (!hitOrStand.equals("hit") && !hitOrStand.equals("stand"));

            if (hitOrStand.equals("hit")) {
                player.dealCard(deck.get(0));
                deck.remove(0);

                System.out.print("\nYour hand: [");
                for (int i = 0; i < player.getHand().size() - 1; i++) {
                    System.out.print(player.getHand().get(i).getRank() + ", ");
                }
                System.out.println(player.getHand().get(player.getHand().size() - 1).getRank() + "]");

                takeTurn(false);
            }
        }
        else {
            dealer.aceDeductionZero();
            if (dealer.addCards() > 21) {
                for (int i = 0; i < dealer.getHand().size(); i++) {
                    dealer.checkAce(dealer.getHand().get(i));
                    if (dealer.addCards() - dealer.getAceDeduction() < 21) {
                        break;
                    }
                }
            }
            System.out.println("Dealer's current card total: " + (dealer.addCards() - dealer.getAceDeduction()));

            if (dealer.addCards() - dealer.getAceDeduction() >= 17 && dealer.addCards() - dealer.getAceDeduction() <= 21) {
                System.out.println("\nThe dealer will stand.");
            }
            else if (dealer.addCards() - dealer.getAceDeduction() <= 16) {
                System.out.println("\nThe dealer will hit.");
                dealer.dealCard(deck.get(0));
                deck.remove(0);

                System.out.print("\nDealer's hand: [");
                for (int i = 0; i < dealer.getHand().size() - 1; i++) {
                    System.out.print(dealer.getHand().get(i).getRank() + ", ");
                }
                System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

                takeTurn(true);
            }
            else if (dealer.addCards() - dealer.getAceDeduction() > 21) {
                dealer.setBust(true);
                System.out.println("\nIt's a bust for the dealer."); // bust
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

    public void endGame() {
        player.clearHand();
        dealer.clearHand();
        // reminder to give player back any chips they wagered on the turn the game ended
    }

    public static void main(String[] args) {
        new Blackjack().game();
    }
}
