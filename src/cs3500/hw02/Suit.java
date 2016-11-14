package cs3500.hw02;

/**
 * Created by SLB on 9/22/16.
 */
public enum Suit {
  Hearts("♥"), Diamonds("♦"), Clubs("♣"), Spades("♠");

  private final String symbol;

  Suit(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Gives this suit's symbol as a string.
   *
   * @return this suit's symbol
   */
  public String getSymbol() {
    return this.symbol;
  }

}
