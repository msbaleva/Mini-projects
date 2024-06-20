package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

import static bg.sofia.uni.fmi.mjt.mail.AccountFolder.INBOX;
import static bg.sofia.uni.fmi.mjt.mail.AccountFolder.SENT;
import static bg.sofia.uni.fmi.mjt.mail.Outlook.formatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutlookTest {
    private static final String ACCOUNT_NAME_1 = "saudadeng";
    private static final String ACCOUNT_NAME_2 = "msbaleva";
    private static final String ACCOUNT_NAME_3 = "probe";
    private static final String ACCOUNT_NAME_4 = "NON";
    private static final String EMAIL_1 = "saudedeng7@gmail.com";
    private static final String EMAIL_2 = "msbal@yahoo.com";
    private static final String EMAIL_3 = "probe@gmail.com";
    private static final String EMAIL_4 = "non@gmail.com";
    private static final String PATH_1 = "/inbox/important";
    private static final String PATH_2 = "/important";
    private static final String PATH_3 = "/inbox/work/important/";
    private static final String MAIL_METADATA =
            //"sender: %s\n" +
            "subject: %s\n" +
            "recipients: %s\n" +
            "received: %s";
    private static final String SUBJECT = "NESHTO SI!";
    private static final String MAIL_CONTENT =
            "ALABALA LALALALALALALALALALALALALALLA\n" + "ALABALALALALALALALALALALALALALALALLA\n";
    private static final String TIMESTAMP = "2022-12-08 14:14";
    private static final int OUT_OF_BOUNDS_PRIORITY = -3;
    private static final String RULE_DEF =
            "subject-includes: neshto\n" +
            "subject-or-body-includes: alabala\n" +
            "from: saudedeng7@gmail.com";
    private static final int PRIORITY = 5;

    private static Account account1 = new Account(EMAIL_1, ACCOUNT_NAME_1);
    private static Account account2 = new Account(EMAIL_2, ACCOUNT_NAME_2);
    private static Account account3 = new Account(EMAIL_3, ACCOUNT_NAME_3);
    private static final MailClient outlook = new Outlook();

    @BeforeAll
    public static void setup() {
        outlook.addNewAccount(ACCOUNT_NAME_1, EMAIL_1);
        outlook.addNewAccount(ACCOUNT_NAME_2, EMAIL_2);
        outlook.createFolder(ACCOUNT_NAME_2, PATH_1);
    }

    @Test
    public void testAddNewAccount() {
        Account actual = outlook.addNewAccount(ACCOUNT_NAME_3, EMAIL_3);
        assertEquals(account3, actual, "Add new account not working properly.");
    }

    @Test
    public void testAddNewAccountThrowsIfAccountAlreadyExists() {
        assertThrows(AccountAlreadyExistsException.class, () -> outlook.addNewAccount(ACCOUNT_NAME_1, EMAIL_1));
    }


    @Test
    public void testCreateFolderThrowsIfAccountDoesNotExist() {
        assertThrows(AccountNotFoundException.class, () -> outlook.createFolder(ACCOUNT_NAME_4, PATH_1));
    }

    @Test
    public void testCreateFolderThrowsIfFolderAlreadyExists() {
        outlook.createFolder(ACCOUNT_NAME_1, PATH_1);
        assertThrows(FolderAlreadyExistsException.class, () -> outlook.createFolder(ACCOUNT_NAME_2, PATH_1));
    }

    @Test
    public void testCreateFolderThrowsIfPathDoesNotStartWithRoot() {
        assertThrows(InvalidPathException.class, () -> outlook.createFolder(ACCOUNT_NAME_1, PATH_2));
    }

    @Test
    public void testCreateFolderThrowsIfParentPathDoesNotExist() {
        assertThrows(InvalidPathException.class, () -> outlook.createFolder(ACCOUNT_NAME_1, PATH_3));
    }

    @Test
    public void testSendMail() {
        String mailMetadata = String.format(MAIL_METADATA, SUBJECT, EMAIL_2, TIMESTAMP);
        outlook.sendMail(ACCOUNT_NAME_1, mailMetadata, MAIL_CONTENT);
        Mail actual1 = outlook.getMailsFromFolder(ACCOUNT_NAME_2, INBOX).iterator().next();
        Mail actual2 = outlook.getMailsFromFolder(ACCOUNT_NAME_1, SENT).iterator().next();
        Mail expected = new Mail(account1, new HashSet<>(Collections.singleton(EMAIL_2)), SUBJECT, MAIL_CONTENT, LocalDateTime.parse(TIMESTAMP, formatter));
        assertEquals(expected, actual1, "Send mail not working properly.");
        assertEquals(expected, actual2, "Send or receive mail not working properly.");
    }
    
    @Test
    public void testAddRuleThrowsIfPriorityOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> outlook.addRule(ACCOUNT_NAME_1, PATH_3, RULE_DEF, OUT_OF_BOUNDS_PRIORITY));
    }

    @Test
    public void testAddRule() {
        //outlook.addRule(ACCOUNT_NAME_2, PATH_1, RULE_DEF, PRIORITY);
    }
}
