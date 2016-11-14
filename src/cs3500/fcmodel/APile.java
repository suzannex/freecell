package cs3500.fcmodel;

import java.util.List;

/**CHANGED FOR HW 4:
 * The removeCard method always removed the last card in the pile no matter the given index.
 * Changed it so that it removes the card at the given index. I hate myself lmao, that bug
 * took years to find.
 *
 * <p>Abstract class representing a pile of cards in the game FreeCell. Contains methods to
 * determine if the pile is empty, and to print the pile contents, since those are general
 * to all pile types.
 */
public abstract class APile implements IPile {

  /**
   * The type of this pile (see {@Link PileType}).
   */
  PileType type;

  /**
   * A list of the cards that are in this pile.
   * The first card in the list is the bottom card, and the last card is the top card (first
   * that must be removed).
   */
  List<Card> cards;

  @Override
  public boolean hasCardAt(int cardIdx) {
    return (cardIdx >= 0) && (cardIdx < cards.size());
  }

  @Override
  public int size() {
    return this.cards.size();
  }

  @Override
  public Card getCard(int cardIndex) throws IllegalArgumentException {
    if (cardIndex < this.cards.size()) {
      return this.cards.get(cardIndex);
    } else {
      throw new IllegalArgumentException("Card does not exist at that index");
    }
  }

  @Override
  public void addCard(Card c) throws IllegalArgumentException {
    if (!canAddCard(c)) {
      throw new IllegalArgumentException("The card cannot be added to this pile");
    } else {
      this.cards.add(c);
    }
  }

  @Override
  public Card removeCard(int cardIdx) throws IllegalArgumentException {
    return this.cards.remove(cardIdx);
  }

  @Override
  public String printPileContents() {
    if (this.cards.size() < 1) {
      return "";
    } else {
      String cardNames = cards.get(0).toString(); // add the first card
      for (int i = 1; i < cards.size(); i += 1) { // add rest of the cards with ", " before each
        cardNames += ", " + cards.get(i).toString();
      }
      return cardNames;
    }
  }

  @Override
  public boolean isEmpty() {
    return this.cards.size() == 0;
  }

  /**
   * Determine if the two cards are opposite color suits.
   * @param c1 The first card to be compared
   * @param c2 The second card to be compared
   * @return true if they are opposite colors, false if not
   */
  protected boolean oppositeColorSuit(Card c1, Card c2) {
    if ((c1.getSuit() == Suit.Clubs) || (c1.getSuit() == Suit.Spades)) {
      return (c2.getSuit() == Suit.Hearts) || (c2.getSuit() == Suit.Diamonds);
    } else {
      return (c2.getSuit() == Suit.Clubs) || (c2.getSuit() == Suit.Spades);
    }
  }

}
