package cs3500.fcmodel;

import java.util.List;

/**
 * Represents an open pile of cards in the card game FreeCell.
 * An open pile is a pile that can store up to one card at a time.
 */
public class OpenPile extends APile {

  OpenPile(PileType type, List<Card> cards) throws IllegalArgumentException {
    if (type == PileType.OPEN) {
      this.type = type;
    } else {
      throw new IllegalArgumentException("Invalid pile type; must be Open");
    }
    this.cards = cards;
  }

  /**
   * Determine if the given card can be added to this pile. In the case of the Open pile,
   * a card can only be added if the pile is empty.
   *
   * @param c the given card to be added
   * @return true if the card can be added, false if not.
   */
  @Override
  public boolean canAddCard(Card c) {
    return this.cards.size() == 0;
  }

  @Override
  public boolean canRemoveCard(int cardIdx) {
    return this.cards.size() == 1 && cardIdx == 0;
  }
}
