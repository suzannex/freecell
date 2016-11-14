package cs3500.fcmodel;

import java.util.Objects;

/**
 * A factory class for FreeCellModel and FreeCellModelMulti - for FreeCell models
 * that implement IFreeCellModel, and single-card and multi-card moves (respectively).
 */
public class FreeCellModelCreator {

  /**
   * Represents one of two possible game types,
   * a single-move game and a multi-move game of FreeCell.
   */
  public enum GameType {
    /**
     * The type that represents a single-move game.
     */
    SINGLEMOVE,

    /**
     * The type that represents a multi-move game.
     */
    MULTIMOVE;
  }

  /**
   * Create an instance of either a FreeCellModel or a FreeCellModelMulti.
   * @param type The type of game, either single-move or multi-move FreeCell
   * @return A FreeCellModel or a FreeCellModelMulti
   */
  public static IFreeCellModel<Card> create(GameType type) {
    if (Objects.isNull(type)) {
      throw new IllegalArgumentException("No null arguments");
    }
    switch (type) {
      case SINGLEMOVE:
        return new FreeCellModel();
      case MULTIMOVE:
        return new FreeCellModelMulti();
      default:
        throw new IllegalArgumentException("No null arguments");
    }
  }

}
