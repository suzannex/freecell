package cs3500.fcmodel;

import java.util.List;
import cs3500.fcmodel.strategy.*;

/**
 * Represents a pile of cards in the game Freecell.
 */
public final class Pile implements IPile {

  private List<Card> cards;
  private PlacementStrategy placementStrategy;
  private RemovalStrategy removalStrategy;


  @Override
  public boolean hasCardAt(int cardIdx) {
    return false;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public Card getCard(int cardIdx) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void addCard(Card c) throws IllegalArgumentException {

  }

  @Override
  public Card removeCard(int cardIdx) throws IllegalArgumentException {
    return null;
  }

  @Override
  public boolean canAddCard(Card c) {
    return false;
  }

  @Override
  public boolean canRemoveCard(int cardIdx) {
    return false;
  }

  @Override
  public String printPileContents() {
    return null;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
