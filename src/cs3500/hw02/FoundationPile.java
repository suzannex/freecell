package cs3500.hw02;

import java.util.List;

/**
 * Represents a foundation pile of cards in the game FreeCell.
 * A foundation pile may either be empty, or contain cards of a specific suit, in ascending
 * unbroken order starting from Ace.
 */
public class FoundationPile extends APile {

  FoundationPile(PileType type, List<Card> cards) throws IllegalArgumentException {
    if (type == PileType.FOUNDATION) {
      this.type = type;
    } else {
      throw new IllegalArgumentException("Invalid pile type; must be Foundation");
    }
    this.cards = cards;
  }

  /**
   * Determine if the given card can be added to this foundation pile.
   * The card can be added if it has the same suit and number one higher than the top card
   * (last card in cards list) of this pile.
   *
   * @param c the given card
   * @return true if the card can be added, false otherwise
   */
  @Override
  public boolean canAddCard(Card c) {
    if (this.cards.size() == 0) {
      return (c.getNumber() == 1);
    } else {
      Card topCard = this.cards.get(cards.size() - 1);
      return c.sameSuit(topCard) && c.getNumber() == (topCard.getNumber() + 1);
    }
  }

  @Override
  public boolean canRemoveCard(int cardIdx) {
    return !this.isEmpty() && ((this.cards.size() - 1) == cardIdx);
  }
}
