package seedu.coinflip.utils.exceptions;

public class CoinFlipFileException extends CoinFlipException {

    public CoinFlipFileException() {

    }

    public CoinFlipFileException(String message) {
        super(message);
    }

    public static final String SAVE_FILE_CORRUPTED = "Warning: Your save file was corrupted!" +
            "Your previous progress will not be loaded.";

    public static final String SAVE_FILE_NO_ACCESS = "Warning: Coinflip could not access your save file! " +
            "Your previous progress will not be loaded.";

    public static final String SAVE_FILE_CANNOT_CREATE = "Warning: Coinflip could not create a save file! " +
            "Your progress will not be saved.";

    public static final String SAVE_FILE_CANNOT_SAVE = "Warning: Coinflip could not save your save file! " +
            "Your progress will not be saved.";
}
