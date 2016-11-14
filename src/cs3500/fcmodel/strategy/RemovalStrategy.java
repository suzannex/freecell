package cs3500.fcmodel.strategy;

import java.util.List;

import cs3500.fcmodel.Card;

/**
 * Represents the logic of removing cards from piles in a game of Freecell.
 */
public interface RemovalStrategy {

  public void canBeRemoved(int cardIdx);

  public List<Card> remove(int cardIdx);

}
