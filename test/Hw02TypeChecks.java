import java.util.List;

import cs3500.fcmodel.FreeCellModel;
import cs3500.fcmodel.IFreeCellModel;
import cs3500.fcmodel.PileType;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw02TypeChecks {

  public static void main(String[] args) {
    helper(new FreeCellModel());
  }

  private static <T> void helper(IFreeCellModel<T> model) {
    List<T> deck = model.getDeck();
    model.startGame(deck, 8, 4, false);
    model.move(PileType.CASCADE, 0, 4, PileType.CASCADE, 2);
  }
}
