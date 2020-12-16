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

                if (player.addCards() - player.getAceDeduction() <= 21) {
                    System.out.print("\nDealer's hand: [");
                    for (int i = 0; i < dealer.getHand().size() - 1; i++) {
                        System.out.print(dealer.getHand().get(i).getRank() + ", ");
                    }
                    System.out.println(dealer.getHand().get(dealer.getHand().size() - 1).getRank() + "]");

                    takeTurn(true);
                }

                player.aceDeductionZero();
                dealer.aceDeductionZero();
                if (player.addCards() > 21) {
                    for (int i = 0; i < player.getHand().size(); i++) {
                        player.checkAce(player.getHand().get(i));
                        if (player.addCards() - player.getAceDeduction() <= 21) {
                            break;
                        }
                    }
                }
                if (dealer.addCards() > 21) {
                    for (int i = 0; i < dealer.getHand().size(); i++) {
                        dealer.checkAce(dealer.getHand().get(i));
                        if (dealer.addCards() - dealer.getAceDeduction() <= 21) {
                            break;
                        }
                    }
                }

                if (player.addCards() - player.getAceDeduction() > 21) {
                    System.out.println("\nYou lose...");
                }
                else if (dealer.addCards() - dealer.getAceDeduction() > 21) {
                    System.out.println("\nYou win!");
                }
                else if (player.addCards() - player.getAceDeduction() > dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou win!");
                }
                else if (player.addCards() - player.getAceDeduction() <= dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou lose...");
                }
            }

            System.out.println("\n#####################");
            System.out.println("#### -NEXT TURN- ####");
            System.out.println("#####################");
            player.clearHand();
            dealer.clearHand();
        }
    }

    public void takeTurn(boolean cpu) {
        if (!cpu) {
            if (player.addCards() > 21) {
                for (int i = 0; i < player.getHand().size(); i++) {
                    player.checkAce(player.getHand().get(i));
                    if (player.addCards() - player.getAceDeduction() < 21) {
                        break;
                    }
                }
            }
            System.out.println("Your current card total: " + (player.addCards() - player.getAceDeduction()));
            player.aceDeductionZero();

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

                if (player.addCards() == 21) {
                    System.out.println("\nCards equal 21, you must stand!");
                    return; // Stop hitting because the total equals 21
                }
                else if (player.addCards() > 21) {
                    for (int i = 0; i < player.getHand().size(); i++) {
                        player.checkAce(player.getHand().get(i));
                        if (player.addCards() - player.getAceDeduction() == 21) {
                            System.out.println("\nCards equal 21, you must stand!");
                            player.aceDeductionZero();
                            return; // Stop hitting because the total equals 21
                        }
                        else if (player.addCards() - player.getAceDeduction() < 21) {
                            player.aceDeductionZero();
                            takeTurn(false);
                        }
                    }
                    System.out.println("\nIt's a bust. How pathetic.");
                    return; // Bust
                }
                else {
                    takeTurn(false);
                }
            }
            else {
                return;
            }
        }
        else {
            if (dealer.addCards() > 21) {
                for (int i = 0; i < dealer.getHand().size(); i++) {
                    dealer.checkAce(dealer.getHand().get(i));
                    if (dealer.addCards() - dealer.getAceDeduction() < 21) {
                        break;
                    }
                }
            }
            System.out.println("Dealer's current card total: " + (dealer.addCards() - dealer.getAceDeduction()));
            dealer.aceDeductionZero();

            if (dealer.addCards() - dealer.getAceDeduction() >= 17 && dealer.addCards() - dealer.getAceDeduction() <= 21) {
                System.out.println("\nThe dealer will stand.");
                return;
            }
            else if (dealer.addCards() - dealer.getAceDeduction() <= 16) {
                System.out.println("\nThe dealer will hit.");
                dealer.dealCard(deck.get(0));
                deck.remove(0);
                takeTurn(true);
            }
            else if (dealer.addCards() - dealer.getAceDeduction() > 21) {
                for (int i = 0; i < dealer.getHand().size(); i++) {
                    dealer.checkAce(dealer.getHand().get(i));
                    if (dealer.addCards() - dealer.getAceDeduction() == 21) {
                        System.out.println("\nThe dealer stands.");
                        dealer.aceDeductionZero();
                        return; // Stop hitting because the total equals 21
                    }
                    else if (dealer.addCards() - dealer.getAceDeduction() < 21) {
                        dealer.aceDeductionZero();
                        takeTurn(true);
                    }
                }
                System.out.println("\nIt's a bust for the dealer.");
                dealer.aceDeductionZero();
                return; // Bust
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
