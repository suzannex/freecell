package cs3500.fcmodel.strategy;

import java.util.List;

import cs3500.fcmodel.Card;
import cs3500.fcmodel.IPile;

/**
 * Represents the logic of moving cards to a pile in a game of Freecell.
 * Breaks it down into basic list movement -- doesn't deal with pile classes at all.
 */
public interface PlacementStrategy {

  /**
   * Determine if the given list of cards can be added to the given destination list.
   *
   * @param toBeAdded The cards to be added to the destination
   * @param destination The destination for the other cards
   */
  public void canAdd(List<Card> toBeAdded, List<Card> destination);

  //??
  public void add(List<Card> toBeAdded, List<Card> destination);

}
