/**
 * Curse 42 is a game project. 
 * 
 * @author Soch04
 * @version v.1.2 (last updated: Febuary 2024)
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

enum Color {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),      // for warning, error, points lost
    GREEN("\u001B[32m"),    // points gained
    YELLOW("\u001B[33m"),   // For messages and emphasis
    BLUE("\u001B[34m"),     // point display
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),     // Winds of chaos events
    WHITE("\u001B[37m"),
    WHITE_BACKGROUND("\u001B[47m"),
    BLUE_BACKGROUND("\u001B[44m");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

enum PointChange {
    PLUS("+"),
    MINUS("-");

    private final String symbol;

    PointChange(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

// Main method to start the game.
class CurseFortyTwo {
    public static int points;
    public static int count;
    public static final int ONE_SECOND = 1000;
    public static final int HALF_SECOND = 500;
    public static final byte RANDOM_DELAY = -1;
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static ArrayList<String> textResponses = readWordsFromFile(""); // REPLACE WITH FILE PATH
    public static ArrayList<String> messages = readWordsFromFile("");

    public static void main(String[] args) {

        String textResponse;

        System.out.print(colorMachine(Color.RED,"Enter 42 (or else!) | (press 0 to stop) : "));
        int input = scanner.nextInt();
        if (input == 42){
            count++;
        }

        while (input == 42) {
            delayMachine(HALF_SECOND);
            textResponse = textResponses.get(random.nextInt(textResponses.size()));
            System.out.println("\n"+ colorMachine(Color.YELLOW, textResponse) + "\n");

            // Trigger with a 10% chance
            switch (random.nextInt(10)) {
                case 1:
                    chaosFrenzy();
                    displayPoints();
                    break;
                case 2:
                    whispersOfChaos();
                    displayPoints();
                    break;
                case 3:
                    curseOfTheRepeating42();
                    displayPoints();
                    break;
                case 4:
                    mimicMirror();
                    displayPoints();
                    break;
                case 5:
                    countdownConsequence(5, scanner); // 5-second countdown
                    displayPoints(); 
                    break;
            }

            System.out.print("Enter 42 (or else!) | (press 0 to stop) : ");
            input = scanner.nextInt();
            count++;

        }

        if (input != 42 && input != 0) {
            throw new IllegalArgumentException(colorMachine(Color.RED, "Only 42 is allowed! You shall not pass!"));
        }
 
        if (count > 0) {
            System.out.println("Ah, you've entered the answer to the ultimate question of life, the universe, and everything for a number of " + count + " times!");
        } else {
            System.out.println("You have entered nothing...YOU FOOL!");
        }
    }

    public static void displayPoints(){
        System.out.println(Color.BLUE_BACKGROUND.getCode() + colorMachine(Color.BLACK, "\nTOTAL POINTS: " + points + "  "));
    }

    /**
     * Colors and displays the addition or subtraction of points automatically. 
     * @param thePoints the points added or subtracted to be displayed.
     * @param change indicates PLUS or MINUS of points.
     * @return the colored point based on PLUS (green) or MINUS (red).
     */
    public static String printPoints(int thePoints, PointChange change){

        String color = change == PointChange.PLUS ? Color.GREEN.getCode() : Color.RED.getCode();
        return color + change.getSymbol() + " " + points + "points" +Color.RESET.getCode();
    }

    /**
     * Updates color based on arguments.
     * 
     * @param color color enum with associciated color code. ColorMachine automatically applies .getCode() and resets the color at the end of a message.
     * @param message message to be displayed with color. 
     */
    public static String colorMachine (Color color, String message){
        return color.getCode() + message + Color.RESET.getCode();
    }

    /**
     * Initiates a dramatic countdown consequence in the game.
     * 
     * @param seconds number of seconds to count down from
     * @param scanner user input check
     */
    public static void countdownConsequence(int seconds, Scanner scanner) {
        System.out.println(colorMachine (Color.RED, "Quick!")+ "Enter 42 after the countdown ends...or else!");

        for (int i = seconds; i > 0; i--) {
            System.out.println(Color.RED.getCode() + i + "...");
            delayMachine(ONE_SECOND);
        }
        System.out.println(Color.RESET.getCode());
        // Check if the user entered 42 in time
        if (scanner.nextInt()==42){
            points ++;
            System.out.println("Whew! You saved the day...for now. " + printPoints(points, PointChange.PLUS));
        }
        else{
            System.out.println("Too late! The universe has imploded... Just kidding!" + colorMachine(Color.RED, "But seriously, enter 42 next time."));
        }
    }
    
    /**
     * CURSE OF THE REPEATING 42 GAME:
     * 
     * Triggering the curse results in being trapped in a loop of 42s (has a random delay for suspense).
     * During the loop, players may encounter a chance for a 32 to appear. 
     * The game concludes after a set number of iterations.
     */
    public static void curseOfTheRepeating42() { 
        System.out.println(colorMachine(Color.RED,"WARNING: ") + "You have entered 42 too many times!");
        delayMachine(ONE_SECOND);
        System.out.println("The Curse of the Repeating 42 has been triggered!");
        System.out.println("You will be trapped in an infinite loop of 42s!"); 

        // Add random delays and messages during the loop
        for (int i = 0; i < 25; i++) {
            System.out.println(42);
            delayMachine(RANDOM_DELAY); 

            // Chance for a 32 to appear
            if (Math.random() < 0.1) { // 10% chance
                lucky32();
            }
        }
    }

    /**
     * WHISPERS OF CHAOS GAME:
     * 
     * Presented with a choice to embrace or escape, players take a gamble that influences their points.
     * Escape attempts may lead to potential point losses, while points are gained by embracing the chaos.
     * Additional risks or rewards based on a gamble further shape the outcome.
     */
    public static void whispersOfChaos() {
        System.out.println(colorMachine(Color.CYAN, "Whispers of Chaos: ") + "The winds of fate carry a mysterious message...");
        String message = messages.get(random.nextInt(messages.size()));

        delayMachine(ONE_SECOND);
        System.out.println(colorMachine(Color.BLUE, message));
        delayMachine(HALF_SECOND);

        System.out.print("Do you want to" + colorMachine(Color.CYAN, " EMBRACE ") + "the chaos or" + colorMachine(Color.CYAN , " ESCAPE ") + "? " );
        int choice = scanner.nextInt();

        // Introduce a gamble-like mechanic
        int gamble = random.nextInt(5); // 20% chance
        Integer reward = 0; // Wrapper class to work with colorMachine().
        Integer risk = 0;

        if (gamble == 0) { 
            reward = 3;
            risk = 1;
        } else if (gamble == 1) { 
            reward = 5;
            risk = 2;
        } else {
            reward = 1;
            risk = 0;
        }

        String messageLost = "You lost " + colorMachine(Color.YELLOW, risk.toString()) + " point(s)!\n";
        String messageGain = "You gained " + colorMachine(Color.YELLOW, reward.toString()) + " point(s)!\n";

        if (choice == 0) { // Escape
            if (random.nextBoolean()) { // 50% chance
                System.out.println("\nYou try to escape, but the winds of chaos follow you...");
                points -= risk;
                System.out.println(messageLost);
            } else {
                System.out.println("You escaped the winds...for now");
                points += reward;
                System.out.println(messageGain);
            }
        } else if (choice == 42) { // Embrace
            System.out.println("\nYou embrace the chaos, and the winds whisper secrets in your ear...");
            message = messages.get(random.nextInt(messages.size()));

            delayMachine(ONE_SECOND);
            System.out.println(colorMachine(Color.BLUE, message));
            delayMachine(HALF_SECOND);

            points += reward;
            System.out.println(messageGain);

            // Additional risk or reward based on the gamble
            if (gamble == 0 || gamble == 1) {
                if (random.nextBoolean()) {
                    points += 1;
                    System.out.println(colorMachine(Color.YELLOW, "+1 ") + " point " + colorMachine(Color.GREEN, "BONUS"));
                } else {
                    points -= 1;
                    System.out.println(colorMachine(Color.YELLOW, "-1 ") + " point " + colorMachine(Color.RED, "PENALTY"));
                }
            }

        } else { // incorrect input
                points -= 1;
                System.out.println(colorMachine(Color.RED, " Incorrect input Penalty:") + "You lost a point!" );
        }
    }

    /**
     * CHAOS FRENZY GAME:
     * 
     * Player must decide whether to embrace the chaos (by entering 42) or escape (by entering 0) in a series of turns.
     * The potential for points is accompanied by the potential for point losses. The game ends after a limited number
     * of attempts or when the player chooses to escape.
     */
    public static void chaosFrenzy() {
        Integer frenzyReward;
        Integer frenzyRisk;
        int choice = 1;
        int counter = 0;

        System.out.println("\n" + colorMachine(Color.YELLOW,"***") + colorMachine(Color.RED, "Chaos Frenzy") + "has begun!" + colorMachine(Color.YELLOW,"***"));
        System.out.println("The " + colorMachine(Color.CYAN, "WINDS OF CHAOS")+ " swirl around you promising great " + colorMachine(Color.GREEN, "rewards") + " but also great " + colorMachine(Color.RED, "risks") + "...");

        if (points < 5){
            frenzyReward = random.nextInt(3) + 1;
            frenzyRisk = random.nextInt(3) + 1;
        }
        else{
            frenzyReward = random.nextInt(points / 3) + 1; // Scale reward based on points
            frenzyRisk = random.nextInt(points / 5) + 1; // Scale risk based on points
        }

        delayMachine(HALF_SECOND);
        System.out.println("The potential reward is " + printPoints(frenzyReward, PointChange.PLUS) + " but the potential risk is " + printPoints(frenzyRisk, PointChange.MINUS));

        while (choice != 0 && counter != 3) {
            System.out.print("Do you dare to embrace the chaos?" + colorMachine(Color.RED, "(Enter 42 to embrace, 0 to escape): "));
            choice = scanner.nextInt();

            // Can run a minimum of 3 times.
            if (choice == 42) {
                counter++;
                // Embrace the chaos
                if (random.nextBoolean()) { // 50% chance of reward
                    points += frenzyReward;
                    System.out.println("You ride the waves of chaos and gain " + frenzyReward + " points!");
                } else { // 50% chance of risk
                    points -= frenzyRisk;
                    System.out.println("The chaos overwhelms you, and you lose " + frenzyRisk + " points!");
                }
            } else if (choice == 0) {
                // Escape the chaos
                System.out.println("You narrowly escape the chaos, but gain no reward.");
            } else {
                System.out.println(colorMachine(Color.RED , "Invalid input.") + "Choose 42 to embrace or 0 to escape.");
            }
        }

        System.out.println(colorMachine(Color.RED, "Chaos Frenzy") + " has ended.\n");
    }

    /**
     * MIMIC MIRROR GAME:
     * 
     * The player must enter their sequence opposite to the mimic's display. Success earns points, but
     * limited attempts allowed. The mimic disappears upon failure.
     */
    public static void mimicMirror() {
        final int MAXATTEMPTS = 3; // dictates number of attempts allowed
        System.out.println("\n" + colorMachine(Color.YELLOW,"***") + "A " + colorMachine(Color.RED , "MIMIC") + " appears! Do the opposite of what it does!" + colorMachine(Color.YELLOW,"***"));
        delayMachine(HALF_SECOND);

        int sequenceLength = random.nextInt(5) + 5; // Generate a sequence of 5-9 numbers
        int[] sequence = new int[sequenceLength];
        for (int i = 0; i < sequenceLength; i++) {
            sequence[i] = random.nextInt(2); // 0 or 1
        }
    
        System.out.print(colorMachine(Color.RED, "MIMIC: "));
        for (int i = 0; i < sequenceLength; i++) {
            System.out.print((sequence[i] == 0) ? 0 : 42); // Print 0 or 42
            System.out.print(" ");
        }
        System.out.println();

        delayMachine(HALF_SECOND);

        int attempts = MAXATTEMPTS; // You can change this to adjust allowed attempts
        boolean sequenceMatched = false;
    
        while (attempts > 0 && !sequenceMatched) {
            System.out.print(colorMachine(Color.YELLOW, "YOU:   "));

            // Resolves issue where beginning of the program reads a newline and considers it a response.
            if (attempts == MAXATTEMPTS){
                scanner.nextLine(); // Consume the newline character
            }
            
            // Read the entire line of input at once
            String inputLine = scanner.nextLine();
            String[] inputNumbers = inputLine.split(" ");
    
            // Check if the input matches the sequence length
            if (inputNumbers.length != sequenceLength) {
                System.out.println("Invalid input. Please enter the same number of values as the mimic.");
                attempts--;
                continue;
            }
    
            sequenceMatched = true;
            for (int i = 0; i < sequenceLength; i++) {
                int input = Integer.parseInt(inputNumbers[i]);
                if (input != 0 && input != 42) {
                    System.out.println("Invalid input. Please enter only 42 or 0.");
                    sequenceMatched = false;
                    break;
                }
                if (input == sequence[i]) {
                    sequenceMatched = false;
                    break;
                }
            }
    
            if (sequenceMatched) {
                points += 4;
                System.out.println("You've outsmarted the mimic!" + printPoints(points,PointChange.PLUS));
            } else {
                attempts--;
                System.out.println("The mimic laughs at your feeble imitation. Try again." + printPoints(points, PointChange.MINUS));
                if (attempts > 0) {
                    System.out.println(colorMachine(Color.RED, "Attempts remaining: ") + attempts);
                }
            }
        }
    
        if (!sequenceMatched) {
            System.out.println("The mimic has had its fun and disappears...");
        }
    }

    // Triggers the appearance of a 32 in the game.
    public static void lucky32() {
        System.out.println("Ah, a glimpse of hope!" + colorMachine(Color.YELLOW, "32") + " appears!");
        delayMachine(HALF_SECOND);
        System.out.println(colorMachine(Color.GREEN, "Press 0 to claim your point!"));

        int userInput = scanner.nextInt();
        if (userInput == 0) {
            points++;
            System.out.println("You scored a point!");
        }
    }
    
    /**
     * Generates delays at specified values. Random delay if argument is -1.
     * 
     * @param milliseconds delay duration
     */
    public static void delayMachine(long milliseconds) {
        if (milliseconds < 0) {
            milliseconds = (long)(Math.random() * 500);
        }
    
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
    }

    /**
     * Reads words from a file and returns them in an ArrayList.
     * 
     * @param filename name of the file to read words from
     * @return ArrayList containing the words read from the file
     */
        static ArrayList<String> readWordsFromFile(String filename) {
        ArrayList<String> words = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
        return words;
    }
}

