package cs3500.hw02test;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import cs3500.hw02.Card;
import cs3500.hw02.FreeCellModel;
import cs3500.hw02.IFreeCellModel;
import cs3500.hw02.PileType;
import cs3500.hw02.Suit;
import cs3500.hw04.FreeCellModelMulti;
//import cs3500.hw04.FreeCellModelCreator;

/**
 * CHANGED FOR HW 04. Rewrote all the tests so that they only accessed public
 * FCM interface methods.
 * All tests are new except the second-to-last one that tests the gameOver() method. Also, moved
 * this test class out of the cs3500.hw02 package because I shouldn't have put it there in the
 * first place, and since it no longer accesses protected/package methods of classes in the hw02
 * package, it doesn't need to be there.
 *
 * <p>Contains tests for the IFreeCellModel interface, specifically the FreeCellModel
 * implementation. Any tests for behavior that can be generalized to the FreeCellModelMulti class
 * (that allows multiple-card moves) are run on both classes. Tests that check for behavior like
 * not allowing a card to be removed from the middle of a pile are only run on the single-card-move
 * FreeCell class.
 */
public abstract class FreeCellModelTest {

  /**
   * Factory class that implements abstract method makeFcm() to create a FreeCellModel.
   */
  public final static class FcmSingleTest extends FreeCellModelTest {

    @Override
    public IFreeCellModel<Card> makeFcm() {
      return new FreeCellModel();
      //return new FreeCellModelCreator().create(FreeCellModelCreator.GameType.SINGLEMOVE);
    }
  }

  /**
   * Factory class that implements abstract method makeFcm() to create a FreeCellModelMulti.
   */
  public final static class FcmMultiTest extends FreeCellModelTest {

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

  ArrayList<Card> reverseOrderDeck; // DO NOT MODIFY!!
  ArrayList<Card> shuffledDeck;
  IFreeCellModel<Card> genFcm;
  IFreeCellModel<Card> startedFcm;

  /**
   * Initializes the fields of this tester class.
   */
  @Before
  public void initData() {
    genFcm = makeFcm();
    startedFcm = makeFcm();

    reverseOrderDeck = new ArrayList<Card>(52);
    // Give reverseOrderDeck 52 cards with the kings first and with numbers
    // grouped together (instead of suits together) for easy dealing
    for (int i = 13; i >= 1; i -= 1) {
      for (Suit s : Suit.values()) {
        reverseOrderDeck.add(new Card(i, s));
      }
    }
    startedFcm.startGame(reverseOrderDeck, 4, 2, false);

    shuffledDeck = (ArrayList) genFcm.getDeck();
    Collections.shuffle(shuffledDeck);
  }

  @Test
  public void testGetDeck() {
    List<Card> deck0 = makeFcm().getDeck();
    assertTrue(deck0.size() == 52);
  }

  // Test starting the game with a null deck
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    makeFcm().startGame(null, 4, 4, false);
  }

  // Test starting the game with an invalid deck
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck() {
    List<Card> deck51 = genFcm.getDeck();
    deck51.remove(0);
    makeFcm().startGame(deck51, 4, 4, true);
  }

  // Test starting game with too few cascade piles
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewCascades() {
    makeFcm().startGame(shuffledDeck, 0, 4, true);
  }

  // Test starting game with too few cascade piles
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooFewOpens() {
    makeFcm().startGame(shuffledDeck, 4, 0, true);
  }

  // Test that starting the game with a shuffle actually shuffles the deck
  // --test that not all the cards are in the same spot (game state should be different)
  // Also test that starting a new game clears the piles from before
  @Test
  public void testShuffleWorks() {
    List<Card> deck0 = makeFcm().getDeck();
    List<Card> deck1 = makeFcm().getDeck();
    IFreeCellModel<Card> ifcm0 = makeFcm();
    IFreeCellModel<Card> ifcm1 = makeFcm();

    // The game states are equal before the games are started
    assertEquals(ifcm0.getGameState(), ifcm1.getGameState());

    ifcm0.startGame(deck0, 4, 4, false);
    ifcm1.startGame(deck0, 4, 4, false);
    // The game states are equal when started with the same un-shuffled deck
    assertEquals(ifcm0.getGameState(), ifcm1.getGameState());

    ifcm1.startGame(deck0, 4, 4, false);
    // The game states are still equal after one game is restarted (so we know the inequality
    // in game states below is not due to improper pile clearing behavior
    assertEquals(ifcm0.getGameState(), ifcm1.getGameState());

    ifcm1.startGame(deck1, 4, 4, true);
    // The game states are not equal when the deck in one game is shuffled
    assertNotEquals(ifcm0.getGameState(), ifcm1.getGameState());
  }

  // Test move with invalid source pile number
  @Test(expected = IllegalArgumentException.class)
  public void testMoveExceptionsInvalidSrcPileNum() {
    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(PileType.CASCADE, -1, 12, PileType.FOUNDATION, 3);
  }

  // Test move with invalid dest pile number
  @Test(expected = IllegalArgumentException.class)
  public void testMoveExceptionsInvalidDestPileNum() {
    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, -1);
  }

  // Test move with null src pile type
  @Test(expected = IllegalArgumentException.class)
  public void testMoveExceptionsNullSrcType() {
    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(null, 3, 12, PileType.FOUNDATION, 3);
  }

  // Test move with null dest pile type
  @Test(expected = IllegalArgumentException.class)
  public void testMoveExceptionsNullDestType() {
    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(PileType.CASCADE, 2, 12, null, 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveExceptionsGameNotStarted() {
    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 2);
  }

  // Test valid single moves with both models. ** CHANGED FOR HW 4**
  // Test that all possible valid single move combinations are valid.
  @Test
  public void testValidMoveCardSingle() {

    String initialGameState = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" +
            "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠";

    IFreeCellModel<Card> ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);

    // Move one card from cascade to foundation
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    assertEquals("F1: A♥\n" + "F2:\n" + "F3:\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move another card from cascade to foundation
    ifcm.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    assertEquals("F1: A♥, 2♥\n" + "F2:\n" + "F3:\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move a third card from cascade to foundation
    ifcm.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 1);
    assertEquals("F1: A♥, 2♥\n" + "F2: A♣\n" + "F3:\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move card from foundation to open pile
    ifcm.move(PileType.FOUNDATION, 0, 1, PileType.OPEN, 0);
    assertEquals("F1: A♥\n" + "F2: A♣\n" + "F3:\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move card from foundation back to cascade
    ifcm.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 2);
    assertEquals("F1:\n" + "F2: A♣\n" + "F3:\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move card from cascade to open pile
    ifcm.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    assertEquals("F1:\n" + "F2: A♣\n" + "F3:\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2: A♦\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move card from open to foundation pile
    ifcm.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 2);
    assertEquals("F1:\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",
            ifcm.getGameState());
    // Move card from cascade to cascade pile
    ifcm.move(PileType.CASCADE, 3, 12, PileType.CASCADE, 1);
    assertEquals("F1:\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♠\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠",
            ifcm.getGameState());
    // Cascade to open again
    ifcm.move(PileType.CASCADE, 2, 12, PileType.OPEN, 1);
    assertEquals("F1:\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2: A♥\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♠\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠",
            ifcm.getGameState());
    // Open to foundation again
    ifcm.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
    assertEquals("F1: A♥\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1: 2♥\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♠\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠",
            ifcm.getGameState());
    // Open to non-empty foundation
    ifcm.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
    assertEquals("F1: A♥, 2♥\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♠\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠",
            ifcm.getGameState());
    // "Move" card to same pile it came from
    ifcm.move(PileType.CASCADE, 1, 12, PileType.CASCADE, 1);
    assertEquals("F1: A♥, 2♥\n" + "F2: A♣\n" + "F3: A♦\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥\n" +
                    "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♠\n" +
                    "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
                    "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠",
            ifcm.getGameState());
  }

  // Test an attempted move with card index out of bounds
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionCardIdxOutOfBounds() {
    IFreeCellModel ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(PileType.CASCADE, 0, 13, PileType.FOUNDATION, 0);
  }

  // Only tested on FreeCellModel since FreeCellModelMulti move method should not
  // necessarily throw exception for this behavior.
  // Test attempted move with card that isn't last card in pile
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionNotLastCard() {
    FreeCellModel fcm = new FreeCellModel();
    fcm.startGame(reverseOrderDeck, 4, 4, false);
    fcm.move(PileType.CASCADE, 0, 4, PileType.FOUNDATION, 0);
  }

  // Test attempted move where non-Ace card can't be added to empty foundation pile
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionNonAceToFoundation() {
    IFreeCellModel ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    // Legal move, Ace to foundation
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    // Illegal move, 2 to empty foundation
    ifcm.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 1);
  }

  // Test attempted move where card can't be added to non-empty foundation pile
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionWrongCardToFoundation() {
    IFreeCellModel ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    // Legal move, Ace to foundation
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    // Illegal move, different Ace to same foundation
    ifcm.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 0);
  }

  // Test attempted move where card can't be added to full open pile
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionCardToFullOpen() {
    IFreeCellModel ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    // Cascade to open
    ifcm.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // Cascade to same open pile
    ifcm.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
  }

  // Test attempted move where card can't be moved to cascade pile
  @Test(expected = IllegalArgumentException.class)
  public void testSingleMoveExceptionInvalidMoveBetweenCascades() {
    IFreeCellModel ifcm = makeFcm();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    ifcm.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 1);
  }

  // Test isGameOver
  @Test
  public void testIsGameOver() {
    IFreeCellModel fcm1 = makeFcm();
    fcm1.startGame(reverseOrderDeck, 4, 4, false);

    // The game has just started, so it is not over.
    assertFalse(fcm1.isGameOver());

    // Now there are 4 cascade piles with the aces on top, then the 2s, then the 3s,
    // and so on. Each cascade pile should happen to only have cards of one suit.
    // Let's move all the cards to foundation piles.
    int cardIdx = 51;
    for (int i = 0; i < 52; i += 1) {
      // Before every step (every move) in the game, the game is not yet over.
      assertFalse(fcm1.isGameOver());
      fcm1.move(PileType.CASCADE, (i % 4), cardIdx / 4, PileType.FOUNDATION, (i % 4));
      cardIdx -= 1;
    }
    // After all the cards have been moved, the game is finally over.
    assertTrue(fcm1.isGameOver());

    // However, if a card is moved from Foundation pile back to Open pile, game is no longer over.
    fcm1.move(PileType.FOUNDATION, 0, 12, PileType.OPEN, 0);

    assertFalse(fcm1.isGameOver());
  }

  // Test getGameState before game starts (it has already been tested extensively
  // otherwise through the move tests)
  @Test
  public void testGetGameStateBeforeStart() {
    IFreeCellModel<Card> ifcm = makeFcm();
    assertEquals("", ifcm.getGameState());
  }
}
