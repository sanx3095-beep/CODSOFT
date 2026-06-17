import java.util.Random;
import java.util.Scanner;

public class NumberGame {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    
    // Game statistics
    private static int totalGames = 0;
    private static int gamesWon = 0;
    private static int totalScore = 0;
    private static int coins = 100;
    
    // Game constants
    private static final int STARTING_COINS = 100;
    private static final int HINT_COST = 10;
    private static final int WIN_COINS = 30;
    private static final int MAX_ATTEMPTS = 10;
    
    // Achievement thresholds
    private static final int MASTER_MIND_ATTEMPTS = 9;
    private static final int RISING_CHAMPION_SCORE = 200;
    private static final int NUMBER_KING_SCORE = 500;
    private static final int LEGEND_SCORE = 1000;

    public static void main(String[] args) {
        printHeader();
        String playerName = getPlayerName();
        playGameLoop(playerName);
        displayFinalReport(playerName);
    }

    private static void printHeader() {
        System.out.println("\u001B[1m" + // Bold
            "\n=================================\n" +
            "    \u26A1 NUMBER GUESSING GAME \u26A1\n" +
            "=================================\u001B[0m");
    }

    private static void printSection(String title) {
        System.out.println("\u001B[1m\n--- " + title + " ---\u001B[0m");
    }

    private static String getPlayerName() {
        System.out.print("\nEnter Player Name: ");
        String name = SCANNER.nextLine().trim();
        return name.isEmpty() ? "Unknown Player" : name;
    }

    private static void playGameLoop(String playerName) {
        boolean playAgain = true;

        while (playAgain) {
            totalGames++;
            
            int range = selectDifficulty();
            boolean won = startGame(playerName, range);
            
            if (won) {
                gamesWon++;
            }

            playAgain = askPlayAgain();
        }
    }

    private static int selectDifficulty() {
        printSection("CHOOSE DIFFICULTY");
        System.out.println("1. \u26AA Easy     (1-50)");
        System.out.println("2. \u26AA Medium   (1-100)");
        System.out.println("3. \u26AA Hard     (1-200)");
        System.out.println("4. \u26AA Extreme  (1-500)");

        int choice = getValidInt(1, 4);

        return switch (choice) {
            case 1 -> 50;
            case 2 -> 100;
            case 3 -> 200;
            case 4 -> 500;
            default -> 50;
        };
    }

    private static boolean startGame(String playerName, int range) {
        int secretNumber = RANDOM.nextInt(range) + 1;
        int attempts = MAX_ATTEMPTS;

        printGameStatus(playerName, range);
        System.out.println("\u2728 Guess the number between 1 and " + range);

        while (attempts > 0) {
            printRoundStatus(attempts);
            int option = getGameOption();

            if (option == 2) {
                handleBuyHint();
                continue;
            }

            int guess = getValidGuess(range);
            
            if (guess == secretNumber) {
                handleWin(attempts, secretNumber);
                return true;
            }

            handleWrongGuess(guess, secretNumber);
            attempts--;
        }

        handleGameOver(secretNumber);
        return false;
    }

    private static void printGameStatus(String playerName, int range) {
        System.out.println("\n\u001B[1m" +
            "--------------------------------\n" +
            "Player  : " + playerName + "\n" +
            "Coins   : " + coins + "\u26AA\n" +
            "Range   : 1-" + range + "\n" +
            "--------------------------------\u001B[0m");
    }

    private static void printRoundStatus(int attempts) {
        System.out.println("\n\u26A1 Attempts Left: " + attempts);
        System.out.println("1. \u2606 Guess Number");
        System.out.println("2. \u2606 Buy Hint (" + HINT_COST + " Coins)");
    }

    private static int getGameOption() {
        System.out.println();
        return getValidInt(1, 2);
    }

    private static int getValidInt(int min, int max) {
        while (true) {
            try {
                int value = SCANNER.nextInt();
                if (value >= min && value <= max) {
                    SCANNER.nextLine(); // consume remainder
                    return value;
                }
                System.out.println("\u274C Enter a number between " + min + " and " + max);
            } catch (Exception e) {
                System.out.println("\u274C Invalid input. Enter a number.");
                SCANNER.nextLine();
            }
        }
    }

    private static int getValidGuess(int range) {
        while (true) {
            System.out.print("Enter Guess (1-" + range + "): ");
            try {
                int guess = SCANNER.nextInt();
                SCANNER.nextLine();
                if (guess >= 1 && guess <= range) {
                    return guess;
                }
                System.out.println("\u274C Number must be between 1 and " + range);
            } catch (Exception e) {
                System.out.println("\u274C Invalid input. Enter a number.");
                SCANNER.nextLine();
            }
        }
    }

    private static void handleBuyHint() {
        if (coins < HINT_COST) {
            System.out.println("\u274C Not enough coins! You need " + HINT_COST + " coins.");
            return;
        }

        coins -= HINT_COST;
        System.out.println("\u2728 Buying hint...");
        provideHint();
    }

    private static void provideHint() {
        int secretNumber = RANDOM.nextInt(200) + 1; // This should be passed from startGame
        int hintType = RANDOM.nextInt(4);

        switch (hintType) {
            case 0 -> System.out.println("\u2728 Hint: Number is " + 
                (secretNumber % 2 == 0 ? "\u26AA EVEN" : "\u26AA ODD"));
            case 1 -> {
                int lower = Math.max(1, secretNumber - 10);
                int upper = secretNumber + 10;
                System.out.println("\u2728 Hint: Number lies between " + lower + " and " + upper);
            }
            case 2 -> System.out.println("\u2728 Hint: Number is " + 
                (isPrime(secretNumber) ? "\u26AA PRIME" : "\u26AA NOT PRIME"));
            case 3 -> {
                int remainder = secretNumber % 5;
                System.out.println("\u2728 Hint: Number % 5 = " + remainder);
            }
        }
    }

    private static void handleWin(int attemptsLeft, int secretNumber) {
        int roundScore = attemptsLeft * 20;
        totalScore += roundScore;
        coins += WIN_COINS;

        System.out.println("\n\u001B[1m\u001B[32m🎉 CORRECT GUESS! \u001B[32m🎉\u001B[0m");
        System.out.println("Score Earned : " + roundScore + "\u26AA");
        System.out.println("Coins Earned : " + WIN_COINS + "\u26AA");
        System.out.println("Secret Number: " + secretNumber);

        unlockAchievements(attemptsLeft);
    }

    private static void handleWrongGuess(int guess, int secretNumber) {
        int diff = Math.abs(secretNumber - guess);
        String direction = guess > secretNumber ? "Too High! \u2197\uFE0F" : "Too Low! \u2198\uFE0F";
        
        System.out.println("\u274C " + direction);

        if (diff <= 5)
            System.out.println("\u25F0\uFE0F Very Close! (Distance: " + diff + ")");
        else if (diff <= 15)
            System.out.println("\u25CC Close! (Distance: " + diff + ")");
        else
            System.out.println("\u2744 Far Away! (Distance: " + diff + ")");
    }

    private static void handleGameOver(int secretNumber) {
        System.out.println("\n\u001B[1m\u001B[31m💀 Game Over! \u001B[31m💀\u001B[0m");
        System.out.println("Correct Number was: " + secretNumber);
        System.out.println("Coins Lost: " + (MAX_ATTEMPTS * 5));
        coins -= MAX_ATTEMPTS * 5;
    }

    private static void unlockAchievements(int attemptsLeft) {
        printSection("ACHIEVEMENTS UNLOCKED");

        if (attemptsLeft >= MASTER_MIND_ATTEMPTS)
            System.out.println("\uF0E4\ufe0f \u26AA Master Mind!");

        if (totalScore >= RISING_CHAMPION_SCORE)
            System.out.println("\uF0E4\ufe0f \u26AA Rising Champion!");

        if (totalScore >= NUMBER_KING_SCORE)
            System.out.println("\uF0E4\ufe0f \u26AA Number King!");

        if (totalScore >= LEGEND_SCORE)
            System.out.println("\uF0E4\ufe0f \u26AA LEGEND! \u26AA");

        System.out.println();
    }

    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;

        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0)
                return false;
        }
        return true;
    }

    private static boolean askPlayAgain() {
        System.out.println("\nPlay Again? (Y/N)");
        String choice = SCANNER.next().trim();
        SCANNER.nextLine();
        return choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES");
    }

    private static void displayFinalReport(String playerName) {
        printHeader();
        printSection("FINAL REPORT");

        double winRate = totalGames > 0 ? (gamesWon / totalGames) * 100 : 0;

        System.out.println("Player Name    : " + playerName);
        System.out.println("Games Played   : " + totalGames);
        System.out.println("Games Won      : " + gamesWon + "\u26AA");
        System.out.println("Total Score    : " + totalScore + "\u26AA");
        System.out.println("Coins          : " + coins + "\u26AA");
        System.out.printf("Win Rate       : %.2f%%\n", winRate);
        
        System.out.println("\n\u001B[1mThank you for playing! \u26A1\u001B[0m");
        System.out.println("=================================\n");
    }
}