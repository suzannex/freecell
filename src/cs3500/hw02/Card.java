package cs3500.hw02;

import java.util.Objects;

/**
 * Represents a playing card from a standard deck of 52 cards.
 *
 * <p>Changed for HW3: made this class public so that FreeCellController can be paramaterized
 * over it.
 * - Made getNumber and getSuit public so that move method in FreeCellModelMulti can use
 * card info to check if a build is valid.
 * - Added opposite color suit comparison to the Card class from the Pile classes
 */
public class Card {

  private final int number;
  private final Suit suit;

  /**
   * Constructor for Card.
   * @param number The card number to be assigned to this card
   * @param suit The suit this card will have
   */
  public Card(int number, Suit suit) {
    if ((number < 1) || (number > 13)) {
      throw new IllegalArgumentException("Invalid card number");
    }
    this.number = number;
    this.suit = suit; // Check for null suit?? Whose problem is it?
  }

  /**
   * Return this card's numerical value (1-13, inclusive).
   *
   * @return this card's number
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * Return this card's suit.
   *
   * @return this card's suit
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Provide the value and suit of this card as a String.
   *
   * @return this card's value and suit as a string
   */
  @Override
  public String toString() {
    String value = "";
    if ((this.number <= 10) && (this.number > 1)) {
      value = Integer.toString(this.number);
    } else {
      switch (this.number) {
        case 1:
          value = "A";
          break;
        case 11:
          value = "J";
          break;
        case 12:
          value = "Q";
          break;
        case 13:
          value = "K";
          break;
        default: // this should not be reached;
          // cards outside valid value range prevented by constructor
      }
    }
    return value + this.suit.getSymbol();
  }

  /**
   * Determines if the given card is the same suit as this card.
   *
   * @param c the card to compare suits to
   * @return true if given card is same suit, false if not
   */
  protected boolean sameSuit(Card c) {
    return this.suit == c.suit;
  }


  /**
   * Two cards are equal if their suits and numbers are equal.
   *
   * @param obj the object to be compared
   * @return true if the cards are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Card) {
      Card other = (Card) obj;
      return this.suit == other.suit && this.number == other.number;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.suit, this.number);
  }

  /**
   * Determine if this card is the opposite color suit as the other card.
   *
   * @param c The other card to be compared
   * @return true if they are opposite colors, false if not
   */
  public boolean oppositeColorSuit(Card c) {
    if ((c.getSuit() == Suit.Clubs) || (c.getSuit() == Suit.Spades)) {
      return (this.suit == Suit.Hearts) || (this.suit == Suit.Diamonds);
    } else {
      return (this.suit == Suit.Clubs) || (this.suit == Suit.Spades);
    }
  }
}
