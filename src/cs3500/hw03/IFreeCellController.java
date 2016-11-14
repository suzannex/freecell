package cs3500.hw03;

import cs3500.hw02.IFreeCellModel;

import java.util.List;

/**
 * The interface for the controller in a game of FreeCell.
 */
public interface IFreeCellController<K> {

  /**
   * Start a game of FreeCell with the given inputs. Run the game through input given to the
   * controller via a Readable, and write output to an Appendable. Keep the game running
   * until it is won, there is no more input, or until the game is quit by the user.
   *
   * <p>Print the game state once before every move, and once more before the game is quit (by user
   * or because input has ran out).
   *
   * @param deck        The deck to be used in the game
   * @param model       The FreeCell model that will be used to play the game
   * @param numCascades The number of cascade piles in the game
   * @param numOpens    Number of open piles in the game
   * @param shuffle     Whether or not the deck should be shuffled before game start
   */
  void playGame(List<K> deck, IFreeCellModel<K> model, int numCascades, int numOpens,
                boolean shuffle) throws RuntimeException;

}
