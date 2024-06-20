package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;

import java.util.*;

public class AccountFolder {
    private static final String PARENT_NOT_EXIST_MESSAGE = "Parent folder for %s does not exist.";
    private static final String FOLDER_EXISTS_MESSAGE = "Folder %s already exists.";
    private static final String FOLDER_NOT_EXIST_MESSAGE = "Folder %s does not exist.";
    private static final String INVALID_ROOT_MESSAGE = "Path %s must start with folders '/inbox' or '/sent'.";
    public static final String INBOX = "/inbox";
    public static final String SENT = "/sent";
    private Map<String, Set<Mail>> folders = new TreeMap<>();
    private static final String PATH_SEPARATOR = "/";

    public AccountFolder() {
        folders.put(INBOX, new HashSet<>());
        folders.put(SENT, new HashSet<>());
    }

    public void addMail(Mail mail, String folderPath) {
        folders.get(folderPath).add(mail);
    }

    public void addFolder(String path) {
        if (!path.startsWith(INBOX) && !path.startsWith(SENT)) {
            throw new InvalidPathException(String.format(INVALID_ROOT_MESSAGE, path));
        }

        if (folders.containsKey(path)) {
            throw new FolderAlreadyExistsException(String.format(FOLDER_EXISTS_MESSAGE, path));
        }

        if (!folders.containsKey(path.substring(0, path.lastIndexOf(PATH_SEPARATOR)))) {
            throw new InvalidPathException(String.format(PARENT_NOT_EXIST_MESSAGE, path));
        }

        folders.put(path, new HashSet<>());
    }

    public Collection<Mail> mails(String path) {
        Set<Mail> mails = folders.get(path);
        if (mails == null) {
            throw new FolderNotFoundException(String.format(FOLDER_NOT_EXIST_MESSAGE, path));
        }

        return mails;
    }

    public boolean hasFolder(String path) {
        if (!folders.containsKey(path)) {
            throw new FolderNotFoundException(String.format(FOLDER_NOT_EXIST_MESSAGE, path));
        }

        return true;
    }

    public void update(Rule newRule) {
        Set<Mail> mails = folders.get(INBOX);
        for (Mail mail : mails) {
            if (newRule.isApplicable(mail)) {
                addMail(mail, newRule.folderPath());
                mails.remove(mail);
            }
        }
    }

    public void addMailToInbox(Mail mail) {
        addMail(mail, INBOX);
    }

    public void addMailToSent(Mail mail) {
        addMail(mail, SENT);
    }
}
