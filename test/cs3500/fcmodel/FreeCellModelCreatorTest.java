package cs3500.fcmodel;

import org.junit.Test;

import cs3500.fcmodel.FreeCellModel;
import cs3500.fcmodel.IFreeCellModel;
import cs3500.fcmodel.Card;
import cs3500.fcmodel.FreeCellModelCreator;
import cs3500.fcmodel.FreeCellModelMulti;

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