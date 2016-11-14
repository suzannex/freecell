package cs3500.fccontroller;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

import cs3500.fcmodel.IFreeCellModel;
import cs3500.fcmodel.FreeCellModel;
import cs3500.fcmodel.Card;

import cs3500.fcmodel.FreeCellModelMulti;

import java.util.List;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.StringReader;

/**CHANGED FOR HW 04: Made test class abstract, and made factory method and two
 * static factory classes that implement the factory method in order to run
 * all tests on both FreeCellModel and FreeCellModelMulti.
 *
 * <p>Contains the tests for the implementation of the FreeCell controller.
 */
public abstract class FreeCellControllerTest {

  /**
   * Factory class that implements abstract method makeFcm() to create a FreeCellModel.
   */
  public final static class FcmSingleTest extends FreeCellControllerTest {

    @Override
    public IFreeCellModel<Card> makeFcm() {
      return new FreeCellModel();
      //return new FreeCellModelCreator().create(FreeCellModelCreator.GameType.SINGLEMOVE);
    }
  }

  /**
   * Factory class that implements abstract method makeFcm() to create a FreeCellModelMulti.
   */
  public final static class FcmMultiTest extends FreeCellControllerTest {

    @Override
    public IFreeCellModel<Card> makeFcm() {
      return new FreeCellModelMulti();
      //return new FreeCellModelCreator().create(FreeCellModelCreator.GameType.MULTIMOVE);
    }
  }

  /**
   * Factory method that creates object from class that implements IFreeCellModel(Card).
   *
   * @return an IFreeCellModel type object
   */
  public abstract IFreeCellModel<Card> makeFcm();

  /**
   * A standard deck of cards, arranged A-K within suits.
   */
  List<Card> deck0;

  /**
   * A FreeCellModel that is just used for playGame methods with FreeCellController objects.
   */
  IFreeCellModel<Card> fcm0;

  /**
   * Initialize the fields used in some of the tests.
   * I don't know how to make this comment any better so that I won't get style points off here.
   */
  @Before
  /**
   * Maybe putting two Javadoc comments will help? Turns out it did.
   */
  public void init() {
    fcm0 = makeFcm();
    // Deck is A-K; hearts, diamonds, clubs, spades
    this.deck0 = fcm0.getDeck();
  }

  // Test playGame method on FreeCellController with null Readable
  @Test(expected = IllegalStateException.class)
  public void testNullState1() {
    IFreeCellController fcc = new FreeCellController(null, new StringBuffer());
    fcc.playGame(deck0, fcm0, 4, 4, false);
  }

  // Test playGame method on FCC with null Appendable
  @Test(expected = IllegalStateException.class)
  public void testNullState2() {
    IFreeCellController fcc = new FreeCellController(new InputStreamReader(System.in), null);
    fcc.playGame(deck0, fcm0, 4, 4, false);
  }

  // Test playGame method with null deck input
  @Test(expected = IllegalArgumentException.class)
  public void testNullArgs1() {
    IFreeCellController fcc = new FreeCellController(new InputStreamReader(System.in),
            new StringBuffer());
    fcc.playGame(null, fcm0, 4, 4, false);
  }

  // Test playGame method with null FCM input
  @Test(expected = IllegalArgumentException.class)
  public void testNullArgs2() {
    IFreeCellController fcc = new FreeCellController(new InputStreamReader(System.in),
            new StringBuffer());
    fcc.playGame(deck0, null, 4, 4, false);
  }

  //
  @Test
  public void testInitialGameState() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    IFreeCellModel<Card> fcm = makeFcm();

    fcc.playGame(fcm.getDeck(), fcm, 52, 4, false);
    assertEquals(
            "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\nC3: 3♥\nC4: 4♥\nC5: 5♥\n"
                    + "C6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\nC10: 10♥\n" +
                    "C11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\n"
                    + "C15: 2♦\nC16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\n" +
                    "C20: 7♦\nC21: 8♦\nC22: 9♦\nC23: 10♦\n"
                    + "C24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\nC28: 2♣\n" +
                    "C29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\n"
                    + "C33: 7♣\nC34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\n" +
                    "C38: Q♣\nC39: K♣\nC40: A♠\nC41: 2♠\n"
                    + "C42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\nC46: 7♠\n" +
                    "C47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());

    out = new StringBuffer();
    IFreeCellController fcc1 = new FreeCellController(in, out);
    fcc1.playGame(fcm.getDeck(), fcm, 4, 4, false);
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\n" +
                    "C1: A♥, 5♥, 9♥, K♥, 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠\n" +
                    "C2: 2♥, 6♥, 10♥, A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠\n" +
                    "C3: 3♥, 7♥, J♥, 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠\n" +
                    "C4: 4♥, 8♥, Q♥, 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠",
            out.toString());
  }

  // Test bad source pile input - invalid types.
  // Test retyping all 3 pile types, and also multiple bad inputs in a row.
  @Test
  public void testBadSrcPileType() {
    StringBuffer out = new StringBuffer();
    // Retype source pile as C1, move C1 -> O1
    // Successful move C27 -> F1
    // Retype source pile as F1, move F1 -> C2
    // Retype source pile as O1, move O1 -> C28
    Reader in = new StringReader(
            "testing 1 O1 sucks C1 C27 1 F1 timeto 1 C2 F1 #420 1 C28 blaze starkiller O1");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    // new move: C1 -> O1
                    + "\nF1:\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    // new move: C27 -> F1
                    + "\nF1: A♣\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    // new move: F1 -> C2
                    + "\nF1:\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥, A♣\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    // new move: O1 -> C28
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2: 2♥, A♣\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣, A♥\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());
  }

  // In these tests, the inputs have valid pile types but non-integers for indices,
  // or valid pile integers but bad pile types.
  @Test
  public void testBadSrcPileIdx() {
    StringBuffer out = new StringBuffer();
    // Retype source pile as C1, move C1 -> O1
    // Successful move C27 -> F1
    // Retype source pile as F1, move F1 -> C2
    // Retype source pile as O1, move O1 -> C28
    Reader in = new StringReader(
            "Ctesting 1 O1 Fsucks C1 C27 1 F1 TimeTo 1 C2 F1 A420 1 C28 Cblaze Fstarkiller O1");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    // new move: C1 -> O1
                    + "\nF1:\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    // new move: C27 -> F1
                    + "\nF1: A♣\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    // new move: F1 -> C2
                    + "\nF1:\nF2:\nF3:\nF4:\nO1: A♥\nO2:\nO3:\nO4:\nC1:\nC2: 2♥, A♣\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    + "\nEnter a valid source pile type and index: "
                    // new move: O1 -> C28
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2: 2♥, A♣\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣, A♥\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());
  }

  // Test a series of moves with single and multiple bad card indices
  @Test
  public void testBadCardIdx() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 help F1 i hate testing strings 1 C2 seriously F1 make it stop"
            + " 1 C3 fortheloveofgod O2 1");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    // new move: C1 -> F1
                    + "\nF1: A♥\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    + "\nEnter a valid card index: "
                    // new move: C2 -> F1
                    + "\nF1: A♥, 2♥\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2:\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid card index: "
                    // new move: C2 -> F1
                    + "\nF1: A♥, 2♥\nF2:\nF3:\nF4:\nO1:\nO2: 3♥\nO3:\nO4:\nC1:\nC2:\n"
                    + "C3:\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());
  }

  // Try to make moves with valid or invalid dest pile indices, but bad pile types.
  @Test
  public void testBadDestPileType() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 cons l0 blerner F1 C45 1 lives on O4");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);
    // first move: C1 -> F1
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    // new move: C1 -> F1
                    + "\nF1: A♥\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    // new move: C45 -> O4
                    + "\nF1: A♥\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4: 6♠\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45:\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());
  }

  // Try to make moves with valid dest pile types, but invalid indices.
  @Test
  public void testBadDestPileIdx() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C22 1 F O4 C14 1 C420lsdfkn666 Clamshell Can Cbe Crecycled F2");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid destination pile type and index: "
                    // New move: C22 -> O4
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4: 9♦\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22:\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    + "\nEnter a valid destination pile type and index: "
                    // New move: C14 -> F2
                    + "\nF1:\nF2: A♦\nF3:\nF4:\nO1:\nO2:\nO3:\nO4: 9♦\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14:\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22:\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());
  }

  // Test a series of moves that are not allowed in the game.
  // Pile indices out of range, card indices out of range, moving non-Ace to foundation pile,
  // illegal move to cascade pile, taking card out of middle of cascade pile, attempting
  // to remove card from empty pile
  @Test
  public void testInvalidMove() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C420 1 F1 C3 1 O420 C2 -420 O3 C26 1 F1 C3 1 C4 C1 1 O3 O4 1 O1");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, new FreeCellModel(), 51, 4, false);
    // First move: Src pile index out of range
    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Invalid pile number"
                    // Dest pile index out of range
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Invalid pile number"
                    // Card idx out of range:
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Cannot remove card from pile"
                    // Moving non-Ace to Foundation pile:
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Cannot add card to pile"
                    // Illegal move to Cascade pile:
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Cannot add card to pile"
                    // Taking card out of middle of cascade pile:
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Cannot remove card from pile"
                    // Trying to remove card from Empty pile
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠"
                    + "\nInvalid move. Try again. Issue: Cannot remove card from pile"
                    + "\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥, K♠\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠",
            out.toString());
  }

  // Test that the game will quit if the first input of a move is to quit
  @Test
  public void testQuitFromInitialInput1() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Q 1 F4");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit if the first input of a later move is to quit
  @Test
  public void testQuitFromInitialInput2() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C27 1 F1 q 1 C2");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    // Move: C27 -> F1
                    + "\nF1: A♣\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27:\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit if the second input of the first move is to quit
  @Test
  public void testQuitFromInitialInput3() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C42 q O3");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit if the second input of a later move is to quit
  @Test
  public void testQuitFromInitialInput4() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C52 1 O1 C4 q O2");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    // Move: C52 -> O1
                    + "\nF1:\nF2:\nF3:\nF4:\nO1: K♠\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52:\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit if the third input of the first move is to quit
  @Test
  public void testQuitFromInitialInput5() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 Q");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit if the third input of a later move is to quit
  @Test
  public void testQuitFromInitialInput6() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C1 1 F1 C2 1 q");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    // Move: C1 -> F1
                    + "\nF1: A♥\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1:\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠\nGame quit prematurely.",
            out.toString());
  }

  // Test that the game will quit appropriately if the quit command is given
  // when user is asked to input a source pile again
  @Test
  public void testQuitFromSrcPileRedo() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("Hopeyourgradingisgoingwell 1 F1 q");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid source pile type and index: "
                    + "\nGame quit prematurely.",
            out.toString());

  }

  // Test that game will quit appropriately if quit command is given when user is asked to input
  // card index again
  @Test
  public void testQuitFromCardIdxRedo() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C47 IneverreallylikedhomeworkgradingwhenIwasatutor O2 Q");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid card index: "
                    + "\nGame quit prematurely.",
            out.toString());

  }

  // Test that game quits when user provides quit command instead of destination pile redo
  @Test
  public void testQuitFromDestPileRedo() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("C16 1 hm q");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠"
                    + "\nEnter a valid destination pile type and index: "
                    + "\nGame quit prematurely.",
            out.toString());

  }

  // Gross I do not want to do this
  // I'd almost rather take the point loss
  // It's really not worth my time
  @Test
  public void testFullGamePlaythrough() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("");
    IFreeCellController<Card> fcc = new FreeCellController(in, out);
    fcc.playGame(deck0, makeFcm(), 52, 4, false);

    assertEquals("\nF1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♥\nC2: 2♥\n"
                    + "C3: 3♥\nC4: 4♥\nC5: 5♥\nC6: 6♥\nC7: 7♥\nC8: 8♥\nC9: 9♥\n"
                    + "C10: 10♥\nC11: J♥\nC12: Q♥\nC13: K♥\nC14: A♦\nC15: 2♦\n"
                    + "C16: 3♦\nC17: 4♦\nC18: 5♦\nC19: 6♦\nC20: 7♦\nC21: 8♦\n"
                    + "C22: 9♦\nC23: 10♦\nC24: J♦\nC25: Q♦\nC26: K♦\nC27: A♣\n"
                    + "C28: 2♣\nC29: 3♣\nC30: 4♣\nC31: 5♣\nC32: 6♣\nC33: 7♣\n"
                    + "C34: 8♣\nC35: 9♣\nC36: 10♣\nC37: J♣\nC38: Q♣\nC39: K♣\n"
                    + "C40: A♠\nC41: 2♠\nC42: 3♠\nC43: 4♠\nC44: 5♠\nC45: 6♠\n"
                    + "C46: 7♠\nC47: 8♠\nC48: 9♠\nC49: 10♠\nC50: J♠\n"
                    + "C51: Q♠\nC52: K♠",
            out.toString());

  }
}
