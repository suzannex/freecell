package cs3500.fccontroller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import cs3500.fcmodel.IFreeCellModel;
import cs3500.fcmodel.PileType;

//class Game {
//
//  public static void main(String[] args) {
//    // System.in is an InputStream
//    // InputStreamReader is of type Readable and takes an InputStream in the constructor
//    // Apparently wrapping an InputStreamReader in a BufferedReader is more efficient,
//    // so I guess I will do that. (according to Javadocs)
//    FreeCellController fcc = new FreeCellController(new BufferedReader(
//            new InputStreamReader(System.in)), System.out);
//    IFreeCellModel fcm = new FreeCellModel();
//
//    try {
//      fcc.playGame(fcm.getDeck(), fcm, 4, 4, true);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//  }
//}


/**
 * A class that is the controller for a game of FreeCell.
 *
 * <p>CHANGED FOR HW 4:
 * - playGame: Abstracted the pile type and index parsing into a separate helper method,
 * initPileAndIndex, that parses the input and stores in 2 one-element arrays.
 * - initPileAndIndex: Changed to use arrays for storage of parsed values instead of
 * StringBuilders or other lossy data types.
 */
public class FreeCellController implements IFreeCellController {

  /**
   * The source of String input for moves in the game.
   */
  Readable input;

  /**
   * The location to where the game output is written.
   */
  Appendable output;

  /**
   * Construct a FreeCell controller, which handles input received and communicates
   * it as a call to the move method in the FreeCell model.
   *
   * @param rd The source of input for moves in the game
   * @param ap The destination to which game output will be written.
   */
  public FreeCellController(Readable rd, Appendable ap) {
    this.input = rd;
    this.output = ap;
  }

  @Override
  public void playGame(List deck, IFreeCellModel model, int numCascades, int numOpens,
                       boolean shuffle) throws RuntimeException {

    if (Objects.isNull(input) || Objects.isNull(output)) {
      throw new IllegalStateException("Input and output must be initialized");
    }
    if (Objects.isNull(deck) || Objects.isNull(model)) {
      throw new IllegalArgumentException("Deck and model must be initialized");
    }

    // Try starting the game with the given inputs for the model.
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) { //If inputs are invalid, notify and return.
      appendToOutput("Could not start game.");
      return;
    }

    // Use a scanner to scan the input feed.
    Scanner scan = new Scanner(input);

    //start the while loop that will scan for input for rest of game
    while (!model.isGameOver()) {

      // Print the current game state before the move.
      appendToOutput("\n" + model.getGameState());

      // store the first 3 inputs to make a move (later inputs might be processed before first 3
      // but we still need to know what the original 3 were)
      // Also check if any of them are a quit command.
      if (!scan.hasNext()) {
        return;
      }
      String in1 = scan.next();
      if (stringIsQuit(in1)) {
        appendToOutput("\nGame quit prematurely.");
        return;
      }
      if (!scan.hasNext()) {
        return;
      }
      String in2 = scan.next();
      if (stringIsQuit(in2)) {
        appendToOutput("\nGame quit prematurely.");
        return;
      }
      if (!scan.hasNext()) {
        return;
      }
      String in3 = scan.next();
      if (stringIsQuit(in3)) {
        appendToOutput("\nGame quit prematurely.");
        return;
      }

      // 1. Initialize source pile type and index:
      // *** BEGIN CHANGES – HW 4
      // A 1-item array to write the src pile type to once it's parsed
      PileType[] srcPileTypeLoc = new PileType[1];
      // A 1-item array to write the src pile index to once it's parsed
      int[] srcPileIdxLoc = new int[1];
      // Initialize the source pile type and index, and store the result of the initialization
      // attempt in a boolean var.
      boolean srcInitSucceeds = initTypeAndIndex(srcPileTypeLoc, srcPileIdxLoc, in1, scan,
              "Enter a valid source pile type and index: ");
      // a. If the result was false, an input was a quit command. Quit the game.
      if (!srcInitSucceeds) {
        // Specific quit message will already have been handled in initTypeAndIndex method.
        return;
      }
      // b. Otherwise, the initialization succeeded. Retrieve the vals from the arrays
      // and initialize source pile variables.
      PileType srcPileType = srcPileTypeLoc[0];
      int srcPileIdx = srcPileIdxLoc[0];
      // *** END CHANGES – HW 4


      // 2. Initialize card index:
      // Must initialize card idx here to invalid value so it can be referenced later,
      // it will be reassigned in the loop or the loop will not exit (or the game will be quit).
      int cardIdx = -1;
      boolean cardIdxInitd = false;
      // Repeat until card index is initialized.
      while (!cardIdxInitd) {
        // First, check if the input is a quit command.
        if (stringIsQuit(in2)) {
          appendToOutput("\nGame quit prematurely.");
          return;
        }
        // Then, try to parse the integer.
        try {
          cardIdx = Integer.parseInt(in2);
          // If no exception is thrown, the statement below will run, and the loop will exit
          cardIdxInitd = true;
          // Otherwise, try setting the card index again:
        } catch (NumberFormatException e) {
          appendToOutput("\nEnter a valid card index: ");
          if (!scan.hasNext()) {
            return;
          }
          in2 = scan.next();
        }
      }

      // 3. Initialize destination pile type and index:
      // *** BEGIN CHANGES – HW 4
      // A 1-item array to write the dest pile type to once it's parsed
      PileType[] destPileTypeLoc = new PileType[1];
      // A 1-item array to write the dest pile index to once it's parsed
      int[] destPileIdxLoc = new int[1];
      // Initialize the destination pile type and index, and store the result of the
      // initialization attempt in a boolean var.
      boolean destInitSucceeds = initTypeAndIndex(destPileTypeLoc, destPileIdxLoc, in3, scan,
              "Enter a valid destination pile type and index: ");
      // a. If the result was false, an input was a quit command. Quit the game.
      if (!destInitSucceeds) {
        // Specific quit message will already have been handled in initTypeAndIndex method.
        return;
      }
      // b. Otherwise, the initialization succeeded. Retrieve the vals from the arrays
      // and initialize destination pile variables.
      PileType destPileType = destPileTypeLoc[0];
      int destPileIdx = destPileIdxLoc[0];
      // *** END CHANGES – HW 4


      // With valid input secured, the move can finally be made with the valid input
      // Either the move will work or it won't, and the loop will start again.
      try {
        // Account for the 1-based indexing of user input vs. 0-based indexing of move method
        model.move(srcPileType, srcPileIdx - 1, cardIdx - 1, destPileType, destPileIdx - 1);
      } catch (IllegalArgumentException e) {
        appendToOutput("\nInvalid move. Try again. Issue: " + e.getLocalizedMessage());
      }

      // Don't go through the loop again if all the input is empty.
      if (!scan.hasNext()) {
        break;
      }

    }

    appendToOutput("\n" + model.getGameState());

    // If the gameplay loop exited, check if the game is over.
    if (model.isGameOver()) {
      appendToOutput("\nGame over.");
    }
    return;

  }

  /**
   * Determines if the string is a quit command.
   *
   * @param str the string to be parsed
   * @return true if str is "q" or "Q", false otherwise
   */
  private boolean stringIsQuit(String str) {
    return str.equals("q") || str.equals("Q");
  }

  /**
   * Determines if the given char is one of 'C', 'F', or 'O', i.e. the valid pile type codes.
   *
   * @param c the character to be compared
   * @return true if the char is a valid pile type code, false otherwise
   */
  private boolean validPileTypeChar(Character c) {
    return (c == 'C') || (c == 'F') || (c == 'O');
  }

  /**
   * Append the given string to this controller's output stream.
   * This method exists to deal with the potential IOException from writing to
   * Readables that cannot be handled in the playGame method.
   *
   * @param str The string to be appended to the output
   */
  private void appendToOutput(String str) {
    try {
      this.output.append(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Store the pile type and index to be initialized (in playGame) in two one-length arrays,
   * and return false if one of the inputs was a quit command, true if they were
   * properly initialized.
   *
   * <p>CHANGED FOR HW 4:
   * This method now takes arrays as input to store the pile type and index in, because
   * arrays are simpler and result in less data loss than StringBuilders for PileType pile type
   * and int pile index.
   *
   * @param pileType The array to write pile type to
   * @param pileIdx  The array to write pile index to
   * @param inStr    the initial input string
   * @param scan     The scanner to get input from
   * @return true if pile index and source were successfully stored, false if input was quit cmd.
   */
  boolean initTypeAndIndex(PileType[] pileType, int[] pileIdx, String inStr, Scanner scan,
                           String errorMsg) {

    String in1 = inStr;
    while (true) {
      // Check if the input was a quit command:
      if (stringIsQuit(in1)) {
        appendToOutput("\nGame quit prematurely.");
        return false;
      }
      // if the input is not empty and the first char is C, O, or F:
      if ((!in1.equals("")) && validPileTypeChar(in1.charAt(0))) {
        // INVARIANT: At this point, in1.charAt(0) is one of 'C', 'O', 'F'
        // If the source pile index is a valid integer, assign pile type and index, and exit.
        try {
          pileIdx[0] = Integer.parseInt(in1.substring(1));
          // If no exception is thrown, the code below will run, and the loop will exit
          // The whole input is known to be valid, so initialize the pile type:
          switch (in1.charAt(0)) {
            case 'C':
              pileType[0] = PileType.CASCADE;
              break;
            case 'F':
              pileType[0] = PileType.FOUNDATION;
              break;
            case 'O':
              pileType[0] = PileType.OPEN;
              break;
            default: // this will never be reached
          }
          return true;
          // If the integer was not valid, ignore input
        } catch (NumberFormatException e) {
          //don't need to do anything, new input will be gathered after end of this if statement
        }
      }
      // Stay in loop if input was empty OR first char was not valid pile code
      appendToOutput("\n" + errorMsg);
      // Reassign in1 because we're expecting an input of the same type (pile+idx)
      if (!scan.hasNext()) {
        return false;
      }
      in1 = scan.next();
      // The loop will run again with the new input.
    }
  }

}
