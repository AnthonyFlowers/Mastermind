package com.anthony.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Game implements Serializable {
	/**
	 * Enumerator representing code pegs of different colors
	 *
	 */
	public enum CodePeg {
		// Red, Green, Blue, Magenta, Cyan, Yellow
		R, G, B, M, C, Y;
	}

	/**
	 * Enumerator representing key pegs of different colors
	 *
	 */
	public enum KeyPeg {
		Bl, Wh;
	}

	private static final long serialVersionUID = 2019_12_05L;

	private final int TOTAL_TURNS = 10;
	private final int CODE_LENGTH = 4;
	private Random rand;
	private int turn; // turn counter
	private CodePeg[] code; // secret code
	private List<CodePeg[]> guesses; // track guesses
	private List<KeyPeg[]> responses; // track responses to guesses

	private boolean isGameOver;

	private boolean gameIsWon;

	/**
	 * Game class representing the game Mastermind
	 */
	public Game() {
		rand = new Random();
		initializeGame();
	}

	// Method to create the list of guesses
	private List<CodePeg[]> createGuessList() {
		List<CodePeg[]> guesses = new ArrayList<CodePeg[]>();
		for (int x = 0; x < TOTAL_TURNS; x++) {
			guesses.add(new CodePeg[CODE_LENGTH]);
		}
		return guesses;
	}

	// Method to create the list of responses
	private List<KeyPeg[]> createResponseList() {
		List<KeyPeg[]> responses = new ArrayList<KeyPeg[]>();
		for (int x = 0; x < TOTAL_TURNS; x++) {
			responses.add(new KeyPeg[CODE_LENGTH]);
		}
		return responses;
	}

	// Method to generate a random code
	private CodePeg[] generateCode() {
		CodePeg[] codePegs = CodePeg.values();
		CodePeg[] code = new CodePeg[CODE_LENGTH];
		for (int x = 0; x < CODE_LENGTH; x++) {
			code[x] = codePegs[rand.nextInt(codePegs.length - 1)];
		}
		return code;
	}

	// Method to generate a response to the current guess
	private KeyPeg[] generateResponse() {
		KeyPeg[] key = new KeyPeg[4];
		CodePeg[] codeCopy = code.clone();
		// Check if any pegs match
		for (int x = 0; x < CODE_LENGTH; x++) {
			if (getCurrentGuess()[x] != null && getCurrentGuess()[x].equals(codeCopy[x])) {
				key[x] = KeyPeg.Bl;
				codeCopy[x] = null;
			}
		}
		// Check if any unmatched guessed pegs are in the code
		for (int y = 0; y < CODE_LENGTH; y++) {
			if (key[y] == null && isPegInCode(codeCopy, getCurrentGuess()[y])) {
				key[y] = KeyPeg.Wh;
			}
		}
		return key;
	}

	// Method to get the current guess
	private CodePeg[] getCurrentGuess() {
		return guesses.get(turn);
	}

	/**
	 * Method to return the current guesses
	 * 
	 * @return
	 */
	public List<CodePeg[]> getGuesses() {
		return this.guesses;
	}

	/**
	 * Method to get the guess row by its turn
	 * 
	 * @param pos - the turn of the guess to get
	 * @return an array of CodePegs representing a guess
	 */
	public CodePeg[] getGuessRow(int pos) {
		return guesses.get(pos);
	}

	/**
	 * Method to get the list of possible pegs
	 * 
	 * @return
	 */
	public CodePeg[] getPegs() {
		return CodePeg.values();
	}

	/**
	 * Method to get a specific response row by its turn
	 * 
	 * @param pos - the turn of the desired response row
	 * @return
	 */
	public KeyPeg[] getResponseRow(int pos) {
		return responses.get(pos);
	}

	/**
	 * Method to get the current turn
	 * 
	 * @return an integer representing the current turn number
	 */
	public int getTurn() {
		return turn;
	}
	
	/**
	 * Method to get the remaining turns
	 * @return
	 */
	public int getRemainingTurns() {
		return TOTAL_TURNS - turn;
	}

	// Method to initialize a game
	private void initializeGame() {
		// With seed set the initial code is CCGB
		turn = 0;
		code = generateCode();
		guesses = createGuessList();
		responses = createResponseList();
		isGameOver = false;
		gameIsWon = false;
	}

	/**
	 * Method to check if the game is over
	 * 
	 * @return a boolean true if the game is over false otherwise
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Method to check if the game has been won
	 * 
	 * @return a boolean true if the game has been won galse otherwise
	 */
	public boolean isGameWon() {
		return gameIsWon;
	}

	// method to check if the passed peg is in the passed code
	private boolean isPegInCode(CodePeg[] code, CodePeg peg) {
		for (int x = 0; x < CODE_LENGTH; x++) {
			if (code[x] != null && peg != null && code[x].equals(peg)) {
				code[x] = null; // Set checked peg to null to ignore duplicates
				return true;
			}
		}
		return false;
	}

	/**
	 * A method that handles guessing and checking if the game has been won or is
	 * over
	 */
	public void makeGuess() {
		guesses.set(turn, getCurrentGuess().clone());
		responses.set(turn, generateResponse());
		if (Arrays.equals(this.code, getCurrentGuess())) {
			isGameOver = true;
			gameIsWon = true;
		}

		turn++; // Increment turn count
		if (turn >= TOTAL_TURNS) {
			isGameOver = true;
		}
	}

	/**
	 * Method to reset the game
	 */
	public void reset() {
		initializeGame();
	}

	/**
	 * Method to set a peg for the current guess
	 * 
	 * @param pos - the position of the peg to set
	 * @param peg - the color to set this peg to
	 */
	public void setCodePeg(int pos, CodePeg peg) {
		getCurrentGuess()[pos] = peg;
	}

}
