import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

public class Main {
 
	public static void main(String []args) {
       	HangMan game = new HangMan();
		while (true) {
			game.run();
		}
	}
}

class HangMan {

	List<String> words = new ArrayList<String>();
	List<Character> guessedChars;
	String wordToGuess;
	char[] wordToDisplay;
	
    int numWrongGuesses;
	int numCorrectGuesses;
	
	Scanner input = new Scanner(System.in);
	String bodyParts[] = {"  |", "  O", " /", "|", "\\", "  |", " /", " \\"};
	
	/*  _____
	      |
	      O
	     /|\
	      |
	     / \	*/
	
	public HangMan() {
		System.out.println("Starting Game!");     
		createGame();
	}
    
	void createGame() {
		numWrongGuesses = 0;
		numCorrectGuesses = 0;
		guessedChars = new ArrayList<Character>();
		if (words.size() <= 0) {
			getWords();
		}
		pickWord();
	}

	void getWords() {     
		try {
			Scanner scanner = new Scanner(new File("nouns.txt")).useDelimiter("\\n");
			while (scanner.hasNext()) {
				words.add(scanner.next().trim());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} 
	}    

	void pickWord() {
		Random random = new Random();
		int randomIndex = random.nextInt(words.size());

		wordToGuess = words.get(randomIndex);
		wordToDisplay = new char[wordToGuess.length()];
		
		for (int i = 0; i < wordToDisplay.length; i++) {
			wordToDisplay[i] = '_';
		}
	}	
	
	void run() {
		printBoard(numWrongGuesses);
		char guessedChar = getInput();
		if (!guessedChars.contains(guessedChar)) {
			checkGuess(guessedChar, 0, 0);
		}
		checkEndConditions();
	}
	
	void printBoard(int numWrongGuesses) {
		// print rafter
		System.out.println("_____");
		
		// print noose and body parts
		for (int i = 0; i <= numWrongGuesses; i++) {
			if ((i >= 2) && (i < 4) || (i == 6)) {
				System.out.print(bodyParts[i]);
			} else {
				System.out.println(bodyParts[i]);
			} 
		}
		System.out.println();
		System.out.println();
		
		// print each char in wordToGuess
		for (int i = 0; i < wordToDisplay.length; i++) {
			System.out.print(wordToDisplay[i]);
			System.out.print(" ");
		}
		System.out.println();
		System.out.println();

		// print guessedChars
		if (guessedChars.size() > 0) {
			System.out.print("Your guesses: ");
			for (int i = 0; i < guessedChars.size(); i++) {
				System.out.print(guessedChars.get(i) + " ");
			}
			System.out.println();
		}
	}
		
	char getInput() {
		System.out.println("Guess a letter!");
		String guessedLetter = input.next();
		char guessedChar = guessedLetter.charAt(0);
		return guessedChar;
	}
	
	int checkGuess(char guessedChar, int fromIndex, int numFound) {
		int guessedIndex = wordToGuess.indexOf(guessedChar, fromIndex);	
		if (guessedIndex == -1) {
			if (numFound == 0) {
				guessedChars.add(guessedChar);
				numWrongGuesses++;
			}
			return guessedIndex;
		} else {
			numFound++;
			if (numFound == 1) {
				guessedChars.add(guessedChar);
			}
			numCorrectGuesses++;
			wordToDisplay[guessedIndex] = guessedChar;
			return checkGuess(guessedChar, guessedIndex + 1, numFound);
		}
	}
		
	void checkEndConditions() {
		boolean gameOver = false;
		if (numWrongGuesses >= bodyParts.length - 1) {
			printBoard(numWrongGuesses);
			gameOver = true;
			System.out.println("Game over!");
			System.out.println("The word was: " + wordToGuess);
		}
		if (numCorrectGuesses >= wordToDisplay.length) {
			printBoard(numWrongGuesses);
			gameOver = true;		
			System.out.println("You win!");
		}
		if (gameOver) {
			System.out.println("Play again? Y/N");
			if (input.hasNext()) {
				String choice = input.next();
				if (choice.toUpperCase().equals("Y")) {
					createGame();
				} else {
					System.exit(0);
				}
			}
		}
	}
}

