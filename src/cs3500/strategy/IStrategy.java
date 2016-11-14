package cs3500.strategy;

import cs3500.hw02.Card;
import cs3500.hw02.IPile;

/**
 * Represents the logic of moving cards to and from a pile in a game of Freecell.
 */
public interface IStrategy {

  /**
   * Determine if the given card can be added to the given pile.
   *
   * @param pile the pile to add the card to
   * @param c the given card
   */
  public void canAddCard(IPile pile, Card c);


  /**
   * Determine if the given card can be removed from the given pile.
   *
   * @param pile the pile to remove the card from
   * @param index the index of the card to remove from the pile
   */
  public void canRemoveCard(IPile pile, int index);


}
