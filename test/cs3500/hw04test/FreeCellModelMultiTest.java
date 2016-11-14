package cs3500.hw04test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import cs3500.hw02.PileType;
import cs3500.hw02.Suit;
import cs3500.hw04.FreeCellModelMulti;
import cs3500.hw02.Card;
import cs3500.hw02.IFreeCellModel;

/**
 * Contains tests for IFreeCellModel methods implemented by the FreeCellModelMulti class.
 * Only contains tests for some aspects of the move method,
 * since all other methods and all legal and illegal *single* card moves
 * were tested in the FreeCellModelTest class in the hw02test package.
 */
public class FreeCellModelMultiTest {

  ArrayList<Card> reverseOrderDeck;

  /**
   * Initialize the reverse order deck used in many tests.
   */
  @Before
  public void init() {
    reverseOrderDeck = new ArrayList<Card>(52);
    // Give reverseOrderDeck 52 cards with the kings first and with numbers
    // grouped together (instead of suits together) for easy dealing
    for (int i = 13; i >= 1; i -= 1) {
      for (Suit s : Suit.values()) {
        reverseOrderDeck.add(new Card(i, s));
      }
    }
  }

  @Test
  public void testMultiCardMoves() {
    IFreeCellModel<Card> ifcm = new FreeCellModelMulti();
    ifcm.startGame(ifcm.getDeck(), 52, 4, false);
    // 4 hearts to 5 clubs
    ifcm.move(PileType.CASCADE, 3, 0, PileType.CASCADE, 30);
    // 3 clubs to 4 hearts (30)
    ifcm.move(PileType.CASCADE, 28, 0, PileType.CASCADE, 30);
    // 2 hearts to 3 clubs (30)
    ifcm.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 30);
    // A clubs to 2 hearts (30)
    ifcm.move(PileType.CASCADE, 26, 0, PileType.CASCADE, 30);
    // A pile has been formed.
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: A♥\n" + "C2:\n" + "C3: 3♥\n" + "C4:\n" +
                    "C5: 5♥\n" + "C6: 6♥\n" + "C7: 7♥\n" + "C8: 8♥\n" +
                    "C9: 9♥\n" + "C10: 10♥\n" + "C11: J♥\n" + "C12: Q♥\n" +
                    "C13: K♥\n" + "C14: A♦\n" + "C15: 2♦\n" + "C16: 3♦\n" +
                    "C17: 4♦\n" + "C18: 5♦\n" + "C19: 6♦\n" + "C20: 7♦\n" +
                    "C21: 8♦\n" + "C22: 9♦\n" + "C23: 10♦\n" + "C24: J♦\n" +
                    "C25: Q♦\n" + "C26: K♦\n" + "C27:\n" + "C28: 2♣\n" +
                    "C29:\n" + "C30: 4♣\n" + "C31: 5♣, 4♥, 3♣, 2♥, A♣\n" + "C32: 6♣\n" +
                    "C33: 7♣\n" + "C34: 8♣\n" + "C35: 9♣\n" + "C36: 10♣\n" +
                    "C37: J♣\n" + "C38: Q♣\n" + "C39: K♣\n" + "C40: A♠\n" +
                    "C41: 2♠\n" + "C42: 3♠\n" + "C43: 4♠\n" + "C44: 5♠\n" +
                    "C45: 6♠\n" + "C46: 7♠\n" + "C47: 8♠\n" + "C48: 9♠\n" +
                    "C49: 10♠\n" + "C50: J♠\n" + "C51: Q♠\n" + "C52: K♠",
            ifcm.getGameState());
    // Move a build at C31 to an empty cascade pile C2
    ifcm.move(PileType.CASCADE, 30, 0, PileType.CASCADE, 1);
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" +
                    "O1:\n" + "O2:\n" + "O3:\n" + "O4:\n" +
                    "C1: A♥\n" + "C2: 5♣, 4♥, 3♣, 2♥, A♣\n" + "C3: 3♥\n" + "C4:\n" +
                    "C5: 5♥\n" + "C6: 6♥\n" + "C7: 7♥\n" + "C8: 8♥\n" +
                    "C9: 9♥\n" + "C10: 10♥\n" + "C11: J♥\n" + "C12: Q♥\n" +
                    "C13: K♥\n" + "C14: A♦\n" + "C15: 2♦\n" + "C16: 3♦\n" +
                    "C17: 4♦\n" + "C18: 5♦\n" + "C19: 6♦\n" + "C20: 7♦\n" +
                    "C21: 8♦\n" + "C22: 9♦\n" + "C23: 10♦\n" + "C24: J♦\n" +
                    "C25: Q♦\n" + "C26: K♦\n" + "C27:\n" + "C28: 2♣\n" +
                    "C29:\n" + "C30: 4♣\n" + "C31:\n" + "C32: 6♣\n" +
                    "C33: 7♣\n" + "C34: 8♣\n" + "C35: 9♣\n" + "C36: 10♣\n" +
                    "C37: J♣\n" + "C38: Q♣\n" + "C39: K♣\n" + "C40: A♠\n" +
                    "C41: 2♠\n" + "C42: 3♠\n" + "C43: 4♠\n" + "C44: 5♠\n" +
                    "C45: 6♠\n" + "C46: 7♠\n" + "C47: 8♠\n" + "C48: 9♠\n" +
                    "C49: 10♠\n" + "C50: J♠\n" + "C51: Q♠\n" + "C52: K♠",
            ifcm.getGameState());
  }

  // Test moving an invalid build when there are not enough open spots to handle it
  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidTooFewOpenSpots() {
    IFreeCellModel<Card> ifcm = new FreeCellModelMulti();
    ifcm.startGame(ifcm.getDeck(), 4, 4, false);
    ifcm.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 1);
  }

  // Test moving an invalid build when there would be enough open spots to handle it
  @Test (expected = IllegalArgumentException.class)
  public void testMoveInvalidBuild() {
    IFreeCellModel<Card> ifcm = new FreeCellModelMulti();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    for (int i = 12; i >= 0; i -= 1) {
      ifcm.move(PileType.CASCADE, 0, i, PileType.FOUNDATION, 0);
    }
    for (int i = 12; i >= 0; i -= 1) {
      ifcm.move(PileType.CASCADE, 1, i, PileType.FOUNDATION, 1);
    }
    ifcm.move(PileType.CASCADE, 2, 5, PileType.CASCADE, 0);
  }

  // Try to move a valid build when there would not be enough open spots to handle it
  @Test (expected = IllegalArgumentException.class)
  public void testMoveValidTooFewOpenSpots() {
    IFreeCellModel<Card> ifcm = new FreeCellModelMulti();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    // Move all the diamonds to the first foundation pile
    for (int i = 12; i >= 0; i -= 1) {
      ifcm.move(PileType.CASCADE, 1, i, PileType.FOUNDATION, 0);
    }
    // ace hearts to found #2
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 1);
    // 2 hearts to found #2
    ifcm.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 1);
    // ace clubs to found #3
    ifcm.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 2);
    // 2 hearts to 3 clubs
    ifcm.move(PileType.FOUNDATION, 1, 0, PileType.CASCADE, 2);
    // ace clubs to 2 hearts
    ifcm.move(PileType.FOUNDATION, 2, 0, PileType.CASCADE, 2);
    // ace spades to open #1
    ifcm.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
    // 2 spades to open #2
    ifcm.move(PileType.CASCADE, 3, 11, PileType.OPEN, 1);
    // 3 spades to open #3
    ifcm.move(PileType.CASCADE, 3, 10, PileType.OPEN, 2);
    // 4 spades to open #4
    ifcm.move(PileType.CASCADE, 3, 9, PileType.OPEN, 3);
    // try and fail to move build to open cascade pile
    ifcm.move(PileType.CASCADE, 2, 10, PileType.CASCADE, 1);
  }

  // Try to move a valid build to an invalid destination
  @Test (expected = IllegalArgumentException.class)
  public void testMoveValidToBadDestination() {
    IFreeCellModel<Card> ifcm = new FreeCellModelMulti();
    ifcm.startGame(reverseOrderDeck, 4, 4, false);
    // Move all the diamonds to the first foundation pile
    for (int i = 12; i >= 0; i -= 1) {
      ifcm.move(PileType.CASCADE, 1, i, PileType.FOUNDATION, 0);
    }
    // ace hearts to found #2
    ifcm.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 1);
    // 2 hearts to found #2
    ifcm.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 1);
    // ace clubs to found #3
    ifcm.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 2);
    // 2 hearts to 3 clubs
    ifcm.move(PileType.FOUNDATION, 1, 0, PileType.CASCADE, 2);
    // ace clubs to 2 hearts
    ifcm.move(PileType.FOUNDATION, 2, 0, PileType.CASCADE, 2);
    // ace spades to open #1
    ifcm.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
    // 2 spades to open #2
    ifcm.move(PileType.CASCADE, 3, 11, PileType.OPEN, 1);
    // 3 spades to open #3
    ifcm.move(PileType.CASCADE, 3, 10, PileType.OPEN, 2);
    // 4 spades to open #4
    ifcm.move(PileType.CASCADE, 3, 9, PileType.OPEN, 3);
    // try and fail to move build to bad cascade pile destination
    ifcm.move(PileType.CASCADE, 2, 10, PileType.CASCADE, 3);
  }

}