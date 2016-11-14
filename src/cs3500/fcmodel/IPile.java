package cs3500.fcmodel;

/**
 * This is the interface for classes representing piles of cards in the game FreeCell.
 */
public interface IPile {

  /**
   * Determines if this pile has a card at the given index.
   *
   * @param cardIdx The index to be checked
   * @return true if this pile has a card at that index, false otherwise
   */
  boolean hasCardAt(int cardIdx);

  /**
   * Determine the size of (number of cards in) this pile.
   *
   * @return the size of the pile
   */
  int size();

  /**
   * Return the card at the given index in this pile, if that index exists in the pile.
   *
   * @param cardIdx the index of the card to get
   * @return the card at the given index in this pile
   * @throws IllegalArgumentException if index is outside the range of this pile's card list
   */
  Card getCard(int cardIdx) throws IllegalArgumentException;

  /**
   * Add the card c to the end of this pile, if this is a legal move.
   *
   * @param c the card to be added
   * @throws IllegalArgumentException if the card cannot be added to the pile
   */
  void addCard(Card c) throws IllegalArgumentException;

  /**
   * Remove the card at the given index from the pile, if the pile is not empty.
   * Only the last card may be removed from the pile.
   *
   * @param cardIdx the index of the card to be removed
   * @return the card that was removed
   * @throws IllegalArgumentException if the pile is empty or the index does not specify the last
   *                                  card in the pile.
   */
  Card removeCard(int cardIdx) throws IllegalArgumentException;

  /**
   * Determine if the given card c could be legally added to this pile,
   * based on this pile's contents/last card.
   *
   * @param c the given card
   * @return true if the card can be added to this pile, false if not
   */
  boolean canAddCard(Card c);

  /**
   * Determine if the card at the given index can be removed from the pile.
   * Card can only be removed if the index specifies the last card in the pile and the pile
   * is not empty.
   *
   * @param cardIdx the index of the card to be removed
   * @return true if the card can be removed, false if it cannot be
   */
  boolean canRemoveCard(int cardIdx);

  /**
   * Produce a comma and space-separated string of all the cards in the pile,
   * from bottom of pile (first card in list) to top (last card in list).
   *
   * @return a list of all the cards in the pile
   */
  String printPileContents();

  /**
   * Tells whether or not the pile is empty (has no cards).
   *
   * @return true if pile is empty, false if not
   */
  boolean isEmpty();

}
