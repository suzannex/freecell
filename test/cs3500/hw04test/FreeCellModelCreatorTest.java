package cs3500.hw04test;

import org.junit.Test;

import cs3500.hw02.FreeCellModel;
import cs3500.hw02.IFreeCellModel;
import cs3500.hw02.Card;
import cs3500.hw04.FreeCellModelCreator;
import cs3500.hw04.FreeCellModelMulti;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the FreeCellModelCreator class.
 */
public class FreeCellModelCreatorTest {

  @Test
  public void testProperCreation() {
    FreeCellModelCreator fcmc = new FreeCellModelCreator();
    IFreeCellModel<Card> fcm = fcmc.create(FreeCellModelCreator.GameType.SINGLEMOVE);
    assertTrue(fcm instanceof FreeCellModel);
    IFreeCellModel<Card> fcmm = fcmc.create(FreeCellModelCreator.GameType.MULTIMOVE);
    assertTrue(fcmm instanceof FreeCellModelMulti);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullArgs() {
    FreeCellModelCreator fcmc = new FreeCellModelCreator();
    fcmc.create(null);
  }
}