package seedu.coinflip;

import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import seedu.coinflip.utils.exceptions.CoinFlipException;
import seedu.coinflip.utils.exceptions.CoinFlipFileException;
import seedu.coinflip.utils.printer.Printer;

public class Coinflip {
    private static String filePath = "./data/coinflip.csv";
    private int balance = 500;
    private int betAmount = 20;

    /**
     * Constructs Coinflip object
     */
    Coinflip() {
    }

    /**
     * Returns user's coin balance
     *
     * @return user's balance in coins
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Returns the amount of coins a user bets on each coinflip
     *
     * @return user's betting amount in coins
     */
    public int getBetAmount() {
        return betAmount;
    }

    private void setupFile() throws CoinFlipFileException {
        File userData = new File(filePath);
        try {
            if (!userData.exists()) {
                Files.createDirectories(Paths.get("./data"));
                Files.createFile(Paths.get(filePath));
                Printer.printNewSaveFileNote();
            }
        } catch (IOException E) {
            throw new CoinFlipFileException(CoinFlipFileException.SAVE_FILE_CANNOT_CREATE);
        }
    }

    private void loadFromFile() throws CoinFlipFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String data;
            reader.readLine();
            while (((data = reader.readLine()) != null)) {
                try {
                    balance = Integer.parseInt(data.trim());
                } catch (NumberFormatException e) {
                    throw new CoinFlipFileException(CoinFlipFileException.SAVE_FILE_CORRUPTED);
                }
            }
        } catch (IOException e) {
            throw new CoinFlipFileException(CoinFlipFileException.SAVE_FILE_NO_ACCESS);
        }
    }

    private void saveToFile() throws CoinFlipFileException {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("Balance\n");
            writer.write(balance + "\n");
            writer.close();
        } catch (IOException e) {
            throw new CoinFlipFileException(CoinFlipFileException.SAVE_FILE_CANNOT_SAVE);
        }
    }

    private void check(String[] words) throws CoinFlipException {
        if (words.length != 2) {
            throw new CoinFlipException(CoinFlipException.CHECK_INVALID_FORMAT);
        }

        if (!words[1].equals("balance") && !words[1].equals("bet")) {
            throw new CoinFlipException(CoinFlipException.CHECK_INVALID_FORMAT);
        }

        if (words[1].equals("balance")) {
            Printer.printBalance(balance);
        } else {
            Printer.printBetAmount(betAmount);
        }
    }

    private void change(String[] words) throws CoinFlipException {
        try {
            betAmount = Integer.parseInt(words[1]);

            if (betAmount < 0) {
                throw new CoinFlipException(CoinFlipException.BET_AMOUNT_INVALID_FORMAT);
            }

            if (betAmount > balance) {
                Printer.printNotEnoughCoins();
                return;
            }

            Printer.printBetAmount(betAmount);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new CoinFlipException(CoinFlipException.BET_AMOUNT_INVALID_FORMAT);
        }
    }

    private void bet(String[] words) throws CoinFlipException {
        if (words.length != 2) {
            throw new CoinFlipException(CoinFlipException.FLIP_INVALID_FORMAT);
        }

        if (!words[1].equals("heads") && !words[1].equals("tails")) {
            throw new CoinFlipException(CoinFlipException.FLIP_INVALID_FORMAT);
        }

        if (betAmount > balance) {
            Printer.printNotEnoughCoins();
            return;
        }

        Random random = new Random();
        String coinFlip = random.nextBoolean() ? "Heads" : "Tails";
        Boolean outcome = coinFlip.equalsIgnoreCase(words[1]);

        if (outcome) {
            balance += getBetAmount();
        } else {
            balance -= getBetAmount();
        }

        Printer.printBetAmount(betAmount);
        Printer.printFlipOutcome(coinFlip, outcome, betAmount);
        Printer.printBalance(balance);
    }

    /**
     * Runs main Coinflip program, which waits for next line of user input
     * before outputting an appropriate response
     *
     * @param args Arguments included with command to start Coinflip
     */
    public void run(String[] args) {

        Scanner in = new Scanner(System.in);
        Printer.printWelcome();

        try {
            setupFile();
            loadFromFile();
        } catch (CoinFlipFileException e) {
            Printer.printException(e);
        }

        boolean isExit = false;

        while (!isExit) {
            String input = in.nextLine();
            String[] words = input.split("\\s+");

            try {
                switch (words[0]) {
                case "check":
                    check(words);
                    break;
                case "change":
                    change(words);
                    break;
                case "flip":
                    bet(words);
                    break;
                case "help":
                    Printer.printHelp();
                    break;
                case "exit":
                    Printer.printBye();
                    saveToFile();
                    isExit = true;
                    break;
                default:
                    Printer.printInvalidCommand();
                    break;
                }
            } catch (CoinFlipException e) {
                Printer.printException(e);
            }
        }
    }

    /**
     * Main entry-point for the java.seedu.coinflip.Coinflip application.
     *
     * @param args Arguments included with command to start Coinflip
     */
    public static void main(String[] args) {
        Coinflip program = new Coinflip();
        program.run(args);
    }
}
