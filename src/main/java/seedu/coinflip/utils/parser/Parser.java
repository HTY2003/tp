package seedu.coinflip.utils.parser;

import seedu.coinflip.utils.achievement.AchievementList;
import seedu.coinflip.utils.exceptions.CoinflipException;
import seedu.coinflip.utils.command.Command;
import seedu.coinflip.utils.command.ChangeCommand;
import seedu.coinflip.utils.command.CheckCommand;
import seedu.coinflip.utils.command.ExitCommand;
import seedu.coinflip.utils.command.ResetCommand;
import seedu.coinflip.utils.command.FlipCommand;
import seedu.coinflip.utils.command.HelpCommand;
import seedu.coinflip.utils.logger.CoinflipLogger;
import seedu.coinflip.utils.storage.Storage;
import seedu.coinflip.utils.userdata.UserData;

import java.util.Scanner;

/**
 * Helper class which provides high-level abstraction for receiving and parsing
 * user inputs into relevant command objects for the program to execute.
 */
public class Parser {
    private final Storage storage;
    private UserData userData;
    private AchievementList achievementList;
    private Scanner scanner;
    private String input;

    //@@author timothyloh0523

    /**
     * Constructs Parser object.
     *
     * @param userData        User data to be passed to Command object (if needed)
     * @param achievementList Achievement list to be passed to Command object (if needed)
     * @param storage         Storage handler to be passed to Command object (if needed)
     */

    public Parser(UserData userData, AchievementList achievementList, Storage storage) {
        this.userData = userData;
        this.achievementList = achievementList;
        this.storage = storage;
        this.scanner = new Scanner(System.in);
        CoinflipLogger.info("Parser initialized");
    }

    //@@author HTY2003

    /**
     * Updates class to receive next line of input.
     * If no input is given, the program will stall on this function.
     */
    public void receiveUserInput() {
        this.input = scanner.nextLine();
        CoinflipLogger.info("Received user input: " + this.input);
    }

    // @@author HTY2003

    /**
     * Parses user input, then constructs and returns relevant Command
     * object if the first word is a valid command.
     *
     * @return Relevant Command object for user input
     * @throws CoinflipException if input is an invalid command
     */
    public Command parseUserInput() throws CoinflipException {
        String[] words = this.input.trim().split(" ");
        CoinflipLogger.fine("Split input into " + words.length + "words");

        if (words.length < 1) {
            CoinflipLogger.warning("Empty command");
            throw new CoinflipException(CoinflipException.INVALID_COMMAND);
        }

        switch (words[0]) {
        case "check":
            CoinflipLogger.fine("Created CheckCommand object");
            return new CheckCommand(words, userData);
        case "change":
            CoinflipLogger.fine("Created ChangeCommand object");
            return new ChangeCommand(words, userData, storage);
        case "flip":
            CoinflipLogger.fine("Created FlipCommand object");
            return new FlipCommand(words, userData, achievementList, storage);
        case "help":
            CoinflipLogger.fine("Created HelpCommand object");
            return new HelpCommand(words);
        case "exit":
            CoinflipLogger.fine("Created ExitCommand object");
            return new ExitCommand(words);
        case "reset":
            CoinflipLogger.fine("Created ResetCommand object");
            return new ResetCommand(words, userData, storage);
        default:
            CoinflipLogger.warning("Unknown command: " + words[0]);
            throw new CoinflipException(CoinflipException.INVALID_COMMAND);
        }
    }
}
