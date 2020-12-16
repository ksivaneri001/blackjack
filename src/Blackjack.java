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

        short chipsToBuy = 0;
        do {
            System.out.println("\nYou have " + player.getChips() + " chip(s). How many chips would you like to buy? (Must be greater than 0 but no more than 32,767)");
            try {
                chipsToBuy = in.nextShort();
            }
            catch (Exception e) {
                chipsToBuy = 0;
                in.next();
            }
        } while (chipsToBuy <= 0);
        in.nextLine();
        player.setInitChips(player.getChips() + chipsToBuy);
        player.addChips(chipsToBuy);

        while (deck.size() >= 4 && player.getChips() > 0) {
            for (int i = 0; i < 2; i++) {
                player.dealCard(deck.get(0));
                deck.remove(0);
                dealer.dealCard(deck.get(0));
                deck.remove(0);
            }

//            Print statement used for testing code
            for (Card card : deck) {
                System.out.print(card.getRank() + " ");
            }
            System.out.println(deck.size());

            int wager = 0;
            do {
                System.out.println("\nYou currently have " + player.getChips() + " chip(s). How many will you wager?");
                try {
                    wager = in.nextInt();
                }
                catch (Exception e) {
                    wager = 0;
                    in.next();
                }
                if (wager > player.getChips()) {
                    System.out.println("You don't have that many chips.");
                }
            } while (wager <= 0 || wager > player.getChips());
            in.nextLine();

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
                System.out.println("\nIt's a Blackjack! You get a 3-2 payout on the " + wager + " chip(s) you wagered!");
                player.addChips((int) (1.5 * wager));
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
                    System.out.println("\nYou lose the " + wager + " chip(s) you wagered...");
                    player.addChips(-1 * wager);
                }
                else if (dealer.getBust()) {
                    System.out.println("\nYou win! You get paid back 1 chip for each of the " + wager + " chip(s) you wagered!");
                    player.addChips(wager);
                }
                else if (player.addCards() - player.getAceDeduction() > dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou win! You get paid back 1 chip for each of the " + wager + " chip(s) you wagered!");
                    player.addChips(wager);
                }
                else if (player.addCards() - player.getAceDeduction() < dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nYou lose the " + wager + " chip(s) you wagered...");
                    player.addChips(-1 * wager);
                }
                else if (player.addCards() - player.getAceDeduction() == dealer.addCards() - dealer.getAceDeduction()) {
                    System.out.println("\nIt's a tie. You break even.");
                }
            }

            player.setBust(false);
            dealer.setBust(false);
            player.aceDeductionZero();
            dealer.aceDeductionZero();
            player.clearHand();
            dealer.clearHand();
            System.out.println("\n#####################");
            System.out.println("#### -NEXT HAND- ####");
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
            else if (deck.size() == 0) {
                System.out.println("\nNo cards are left in the deck, you have to stand."); // Prevents code from throwing an error
                return;
            }

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
            else if (dealer.addCards() - dealer.getAceDeduction() > 21) {
                dealer.setBust(true);
                System.out.println("\nIt's a bust for the dealer."); // bust
            }
            else if (deck.size() == 0) {
                System.out.println("\nNo cards are left in the deck, the dealer will stand."); // Prevents code from throwing an error
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
        }
    }

    public void shuffle() {
        deck = new ArrayList<>(52);
        for (String rank : RANKS) {
            for (int i = 0; i < 4; i++) {
                deck.add(new Card(rank));
            }
        }

        Collections.shuffle(deck);
    }

    public void endGame() {
        String endMessage = (player.getChips() == 0) ? "\nYou lost all your chips. Looks like you can't play anymore." : "\nThe deck is empty! The game has ended.";
        System.out.println(endMessage);

        System.out.print("\nYou started with " + player.getInitChips() + " chip(s) and ended with " + player.getChips() + " chip(s). ");
        if (player.getChips() == 0) {
            System.out.println("Better luck next time...");
        }
        else if (player.getChips() < player.getInitChips()) {
            System.out.println("Looks like you lost some money there. Better luck next time!");
        }
        else if (player.getChips() == player.getInitChips()) {
            System.out.println("Looks like you broke even. Not bad, but you can do better.");
        }
        else if (player.getChips() >= 2 * player.getInitChips()) {
            System.out.println("Wow, you more than doubled your original amount! Incredible!");
        }
        else if (player.getChips() > player.getInitChips()) {
            System.out.println("Congratulations! You're leaving with a profit!");
        }

        player.clearHand();
        dealer.clearHand();

        String playAgain = "";
        do {
            System.out.println("\nPlay Again? (Yes or No)");
            playAgain = in.nextLine().toLowerCase();
        } while (!playAgain.equals("yes") && !playAgain.equals("no"));
        if (playAgain.equals("yes")) {
            game();
        }
    }

    public static void main(String[] args) {
        System.out.println("\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        System.out.println("\u2588\u2588    \u2588\u2588\u2588");
        System.out.println("\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        System.out.println("\u2588\u2588    \u2588\u2588\u2588");
        System.out.println("\u2588\u2588\u2588\u2588\u2588\u2588\u2588 L A C K J A C K");
        System.out.println("A Human vs. CPU rendition of the classic card game. Put your skills (and your luck) to the test!");
        new Blackjack().game();
    }
}
