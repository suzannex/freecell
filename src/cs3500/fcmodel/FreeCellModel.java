package cs3500.fcmodel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * This is the class that represents a Freecell model, where single cards can be moved at a time.
 *
 * <p>CHANGED for HW 4:
 * - move method now checks pile type inputs for null, since there is a more direct possibility
 * of that happening from a call of the method from FreeCellController and it should have been
 * done in the first place anyway.
 * - Changed the shuffle method to use Collections.shuffle() for less code repetition.
 */
public class FreeCellModel implements IFreeCellModel<Card> {


  /**
   * The list of foundation piles in this game.
   */
  protected List<IPile> foundationPiles;
  /**
   * The list of cascade piles in this game.
   */
  protected List<IPile> cascadePiles;
  /**
   * The list of open piles in this game.
   */
  protected List<IPile> openPiles;
  /**
   * Whether or not the game has started.
   */
  protected boolean gameStarted;


  /**
   * Constructs a FreeCellModel with default values:
   * the game has not started, and all the piles are initialized but empty.
   */
  public FreeCellModel() {
    gameStarted = false;
    foundationPiles = new ArrayList<IPile>();
    cascadePiles = new ArrayList<IPile>();
    openPiles = new ArrayList<IPile>();
  }


  /**
   * CHANGED for HW 4: Now checking pile type input for null.
   * Also changed the check for if a card could be removed from the source pile.
   * Instead of relying on a method canRemoveCard(int cardIdx) in the pile classes that took a
   * card index and determined if the card at that index could be removed,
   * now the check just determines 2 things:
   * 1. if the pile has a card at that index and
   * 2. if there is no card at the next highest index
   * (because in that case, cardIndex would be idx of last card)
   * This is because canRemoveCard was putting too much of the FreeCellModel implementation
   * responsibility into the pile classes. Only the model should determine if a card at a
   * certain point in the pile can be removed.
   */
  @Override
  public void move(PileType sourceType, int sourcePileNumber, int cardIndex, PileType destType,
                   int destPileNumber) throws IllegalArgumentException {

    if (!gameStarted) {
      throw new IllegalStateException("Cannot make move, game has not started");
    }

    if (sourcePileNumber < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    if (Objects.isNull(sourceType) || Objects.isNull(destType)) {
      throw new IllegalArgumentException("Null pile type");
    }

    IPile sourcePile = this.getPile(sourceType, sourcePileNumber);
    IPile destPile = this.getPile(destType, destPileNumber);

    // If the card index refers to the last card in the source pile, try the move.
    if (sourcePile.hasCardAt(cardIndex) && !sourcePile.hasCardAt(cardIndex + 1)) {

      // If the card is to be moved from and to the same pile,
      // allow this if the card is the last card in the pile.
      // Otherwise, it implies that user is trying to move card from inside pile,
      // which is still illegal.
      if ((sourceType == destType) && (sourcePileNumber == destPileNumber)) {
        // In this case, nothing should be moved.
        return;
      }
      // Try to do the move normally if the source and destination piles are not the same.
      Card temp = sourcePile.getCard(cardIndex);
      if (destPile.canAddCard(temp)) {
        destPile.addCard(temp);
        sourcePile.removeCard(cardIndex);
      } else {
        throw new IllegalArgumentException("Cannot add card to pile");
      }
    } else {
      throw new IllegalArgumentException("Cannot remove card from pile");
    }
  }


  @Override
  public List<Card> getDeck() {

    List<Card> newDeck = new ArrayList<Card>();
    // Create the 52 different cards:
    for (Suit s : Suit.values()) {
      for (int i = 1; i <= 13; i += 1) {
        newDeck.add(new Card(i, s));
      }
    }
    return newDeck;
  }

  /**
   * Randomizes the order of the cards in the given deck.
   *
   * <p>CHANGED FOR HW 4: to use Collections.shuffle() for simplicity.
   * This method may now seem redundant, but I kept it in because shuffling is a standard deck
   * operation that should persist no matter what the data type used to represent a deck is,
   * so here there is a deck-specific shuffling method.
   *
   * @param deck the deck to be shuffled
   */
  protected void shuffle(List<Card> deck) {
    Collections.shuffle(deck);
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    if (Objects.isNull(deck)) {
      throw new IllegalArgumentException("Null deck");
    }
    if (!isValidDeck(deck)) {
      throw new IllegalArgumentException("Invalid deck: " + deck);
    } else if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Invalid number of cascade piles");
    } else if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Invalid number of open piles");
    }

    if (shuffle) {
      Collections.shuffle(deck);
    }

    // Clear the lists of piles
    this.foundationPiles = new ArrayList<IPile>();
    this.cascadePiles = new ArrayList<IPile>();
    this.openPiles = new ArrayList<IPile>();

    // Initialize given number of cascade piles, with (1/numCascadePiles) of the 52 cards in deck
    for (int i = 0; i < numCascadePiles; i += 1) {

      List<Card> thisPilesCards = new ArrayList<Card>();

      for (int j = i; j < 52; j += numCascadePiles) { //get the portion of the cards for this pile
        thisPilesCards.add(deck.get(j));
      }

      IPile cascade = new CascadePile(PileType.CASCADE, thisPilesCards);
      cascadePiles.add(cascade);
    }
    // Initialize given number of open piles
    for (int i = 0; i < numOpenPiles; i += 1) {
      IPile open = new OpenPile(PileType.OPEN, new ArrayList<Card>());
      openPiles.add(open);
    }
    // Initialize 4 foundation piles
    for (int i = 0; i < 4; i += 1) {
      IPile foundation = new FoundationPile(PileType.FOUNDATION, new ArrayList<Card>());
      foundationPiles.add(foundation);
    }

    // All arguments are valid, and the initializations succeeded, so the game can start.
    this.gameStarted = true;
  }


  /**
   * Finds the pile of the given number and type from among this game's piles.
   *
   * @param type    the pile type
   * @param pileNum the pile number
   * @return the pile of the given type and number
   * @throws IllegalArgumentException if pile type is invalid or pile of the given number does not
   *                                  exist
   */
  protected IPile getPile(PileType type, int pileNum) throws IllegalArgumentException {
    switch (type) {
      case FOUNDATION:
        if ((pileNum >= 0) && (this.foundationPiles.size() > pileNum)) {
          return this.foundationPiles.get(pileNum);
        }
        break;
      case OPEN:
        if ((pileNum >= 0) && (this.openPiles.size() > pileNum)) {
          return this.openPiles.get(pileNum);
        }
        break;
      case CASCADE:
        if ((pileNum >= 0) && (this.cascadePiles.size() > pileNum)) {
          return this.cascadePiles.get(pileNum);
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid pile type");
    }
    throw new IllegalArgumentException("Invalid pile number");
  }

  @Override
  public boolean isGameOver() {
    // If the game has not yet started, it shouldn't be "over"
    if (!gameStarted) {
      return false;
    }
    // If any non-foundation pile still has cards, the game is not over.
    for (IPile pile : this.cascadePiles) {
      if (!pile.isEmpty()) {
        return false;
      }
    }
    for (IPile pile : this.openPiles) {
      if (!pile.isEmpty()) {
        return false;
      }
    }
    // If all the non-foundation piles are empty, the foundation piles must all be full,
    // so the game is over.
    return true;
  }

  @Override
  public String getGameState() {
    String output = "";
    if (!gameStarted) {
      return output;
    } else {

      // print each foundation pile with \n after it
      for (int i = 0; i < this.foundationPiles.size(); i += 1) {
        output += "F" + (i + 1) + ":";

        // If the pile is not empty, also print a space and then its contents
        if (!this.foundationPiles.get(i).isEmpty()) {
          output += " " + this.foundationPiles.get(i).printPileContents();
        }
        output += "\n";
      }

      // print each open pile with \n after it
      for (int i = 0; i < this.openPiles.size(); i += 1) {
        output += "O" + (i + 1) + ":";

        // If the pile is not empty, also print a space and then its contents
        if (!this.openPiles.get(i).isEmpty()) {
          output += " " + this.openPiles.get(i).printPileContents();
        }
        output += "\n";
      }

      // print the first cascade pile w/no new line
      output += "C1:";
      if (!this.cascadePiles.get(0).isEmpty()) {
        output += " " + this.cascadePiles.get(0).printPileContents();
      }

      // print rest of cascade piles with \n before each
      for (int i = 1; i < this.cascadePiles.size(); i += 1) {
        output += "\nC" + (i + 1) + ":";

        // If the pile is not empty, also print a space and then its contents
        if (!this.cascadePiles.get(i).isEmpty()) {
          output += " " + this.cascadePiles.get(i).printPileContents();
        }
      }

      return output;
    }
  }

  /**
   * Determines if the given deck is valid; i.e. has 52 cards, with no duplicates.
   * There can only be 13 cards in a given suit, and only 4 suits, so if there are 52 cards,
   * there are guaranteed to be no duplicates.
   *
   * @return whether or not the given deck is valid
   */
  protected boolean isValidDeck(List<Card> deck) {
    if (deck.size() != 52) { // Deck must be 52 cards
      return false;
    }

    Set<Card> cards = new HashSet<Card>();
    cards.addAll(deck);
    return (cards.size() == 52);

  }

}
