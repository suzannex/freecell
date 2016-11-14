package cs3500.hw02;

import java.util.List;

/**
 * Represents a cascade pile of cards in the game FreeCell.
 * A cascade pile is a pile of cards that may contain a random selection of randomly
 * ordered cards, but a card can only be added to it if the card has the opposite color suit
 * and a number one less than the last card on the cascade pile.
 */
public class CascadePile extends APile {

  CascadePile(PileType type, List<Card> cards) throws IllegalArgumentException {
    if (type == PileType.CASCADE) {
      this.type = type;
    } else {
      throw new IllegalArgumentException("Invalid pile type; must be Cascade");
    }
    this.cards = cards;
  }

  /**
   * Determine if the given card can be added to this foundation pile.
   * The card can be added if it has the opposite color suit and a value one less than the top
   * card (last card in list) of this pile.
   *
   * @param c the given card
   * @return true if the card can be added, false if not.
   */
  @Override
  public boolean canAddCard(Card c) {
    if (this.cards.size() == 0) {
      return true;
    } else {
      Card topCard = this.cards.get(cards.size() - 1);
      return this.oppositeColorSuit(c, topCard) && c.getNumber() == (topCard.getNumber() - 1);
    }
  }

  @Override
  public boolean canRemoveCard(int cardIdx) {
    return !this.isEmpty() && ((this.cards.size() - 1) == cardIdx);
  }
}
