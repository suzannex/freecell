package cs3500.fcmodel;

import java.util.ArrayList;
import java.util.Objects;

import cs3500.fcmodel.FreeCellModel;
import cs3500.fcmodel.IPile;
import cs3500.fcmodel.PileType;
import cs3500.fcmodel.Card;

/**
 * The class that represents a FreeCell model, where the ability to move multiple cards
 * has been implemented.
 */
public class FreeCellModelMulti extends FreeCellModel {

  /**
   * Constructs a FreeCellModel with default values:
   * the game has not started, and all the piles are initialized but empty.
   */
  public FreeCellModelMulti() {
    super();
    gameStarted = false;
    foundationPiles = new ArrayList<IPile>();
    cascadePiles = new ArrayList<IPile>();
    openPiles = new ArrayList<IPile>();
  }

  /**
   * This overrides the superclass move method to implement multi-card moves.
   *
   * @param sourceType       the type of the source pile (see {@link PileType})
   * @param sourcePileNumber the pile number of the given type, starting at 0
   * @param cardIndex        the index of the card to be moved from the source pile, starting at 0
   * @param destType         the type of the destination pile (see {@link PileType})
   * @param destPileNumber   the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if the move is not possible
   */
  @Override
  public void move(PileType sourceType, int sourcePileNumber, int cardIndex, PileType destType,
                   int destPileNumber) throws IllegalArgumentException {

    if (!gameStarted) {
      throw new IllegalStateException("Cannot make move, game has not started");
    }

    if ((sourcePileNumber < 0) || (destPileNumber < 0)) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    if (Objects.isNull(sourceType) || Objects.isNull(destType)) {
      throw new IllegalArgumentException("Null pile type");
    }

    IPile sourcePile = this.getPile(sourceType, sourcePileNumber);
    IPile destPile = this.getPile(destType, destPileNumber);
    String errorMsg = "";

    // If source pile has a card at the given index, can try to move any card+build
    if (sourcePile.hasCardAt(cardIndex)) {

      // Determine the size of the build
      int buildSize = sourcePile.size() - cardIndex;
      // If:
      // 1. the build is valid, [break here if src/dest piles are same] and
      // 2. the destination pile will accept the bottom card in the build, and
      // 3. the number of cards in the build is less than or equal
      // to the maximum possible build size,
      // Then: move all the cards one by one, starting with the bottom card, to the new pile.
      // Don't increment the card index because the cards at that index will be removed
      // and the cards to be removed will fall into place.
      //1.
      if (isValidBuild(sourcePile, cardIndex)) {

        // If the card/build is to be moved from and to the same pile, ignore it.
        if ((sourceType == destType) && (sourcePileNumber == destPileNumber)) {
          // In this case, nothing should be moved.
          return;
        }
        //2.
        if (destPile.canAddCard(sourcePile.getCard(cardIndex))) {
          //3.
          if (buildSize <= this.maxBuildMoveSize()) {

            while (cardIndex < sourcePile.size()) {
              //remove from source, add to dest
              destPile.addCard(sourcePile.removeCard(cardIndex));
            }
            // Once the build has been moved, return b/c the job is done.
            return;
          } else {
            errorMsg = "Build size exceeds maximum possible move size";
          }
        } else {
          errorMsg = "Cannot add card to destination pile";
        }
      } else {
        errorMsg = "Cannot move invalid build";
      }
    } else {
      errorMsg = "Source pile has no card at that index";
    }
    // If the build was not successfully moved for any reason,
    // throw an exception.
    throw new IllegalArgumentException(errorMsg);
  }

  /**
   * Based on the number of empty open and cascade piles in the game,
   * determines the maximum build size that can be moved from one pile to another.
   * Does not account for builds being moved to a currently empty cascade pile.
   *
   * @return the max number of cards that can be moved in a build given current game state
   */
  protected int maxBuildMoveSize() {
    int freeOpens = 0;
    for (IPile open : this.openPiles) {
      if (open.isEmpty()) {
        freeOpens += 1;
      }
    }
    int freeCascades = 0;
    for (IPile cascade : this.cascadePiles) {
      if (cascade.isEmpty()) {
        freeCascades += 1;
      }
    }
    return (freeOpens + 1) * (int) Math.pow(2, freeCascades);
  }

  /**
   * Determine if the cards in a pile starting at index startIdx form a valid build
   * (alternating red/black suits and numbers in unbroken descending order).
   *
   * @param pile     The pile to check
   * @param startIdx The card index to start at
   * @return true if the build is valid, false otherwise
   */
  protected boolean isValidBuild(IPile pile, int startIdx) {
    // The build is invalid if there is no card at the given index.
    if (!pile.hasCardAt(startIdx)) {
      return false;
    }
    // Assume that the build is valid to start out with.
    boolean isValid = true;
    Card prevCard = pile.getCard(startIdx);
    int curIdx = startIdx + 1;

    // For each card after the bottom card in the build (if there is one),
    // check if the number decreases by 1 and the suit color is opposite.
    while ((curIdx < pile.size()) && isValid) {
      Card curCard = pile.getCard(curIdx);
      isValid &= prevCard.oppositeColorSuit(curCard);
      isValid &= (prevCard.getNumber() == curCard.getNumber() + 1);

      // Increment the index of next card to be checked and store current card in
      // previous card slot for the next check.
      curIdx += 1;
      prevCard = curCard;
    }

    return isValid;
  }


}
