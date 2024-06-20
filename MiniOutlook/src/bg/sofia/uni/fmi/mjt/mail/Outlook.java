package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Outlook implements MailClient {
    private static final String INVALID_STRING_MESSAGE = "%s is not a valid string.";
    private static final String ACCOUNT_EXISTS_MESSAGE = "Account %s already exists.";
    private static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account %s does not exist.";
    private static final String INVALID_PRIORITY_MESSAGE = "%d is not in range [1,10]";
    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 10;
    private static final String SENDER_METADATA = "sender:";
    private static final String METADATA_SEPARATOR = " ";
    private static final String LINE_SEPARATOR = "\n";
    private static final String SUBJECT_METADATA = "subject:";
    private static final String RECIPIENTS_METADATA = "recipients:";
    private static final String RECEIVED_METADATA = "received:";
    private static final String RECIPIENTS_METADATA_SEPARATOR = ", ";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Map<String, String> emailsByAccountName = new HashMap<>();
    private Map<String, Account> accountsByEmail = new HashMap<>();
    private Map<String, AccountFolder> foldersByAccountName = new HashMap<>();
    private Map<String, Queue<Rule>> rulesByAccountName = new HashMap<>();
    private RuleComparator ruleComparator = new RuleComparator();

    public Outlook() {

    }
    @Override
    public Account addNewAccount(String accountName, String email) {
        if (validateStringInput(accountName, email)) {
            if (emailsByAccountName.containsKey(accountName)) {
                throw new AccountAlreadyExistsException(String.format(ACCOUNT_EXISTS_MESSAGE, accountName));
            }

            Account account = new Account(email, accountName);
            emailsByAccountName.put(accountName, email);
            accountsByEmail.put(email, account);
            foldersByAccountName.put(accountName, new AccountFolder());
            rulesByAccountName.put(accountName, new PriorityQueue<>(ruleComparator));
            return account;
        }

        return null;
    }

    @Override
    public void createFolder(String accountName, String path) {
        if (validateStringInput(accountName, path)) {
            if (!emailsByAccountName.containsKey(accountName)) {
                throw new AccountNotFoundException(String.format(ACCOUNT_NOT_EXIST_MESSAGE, accountName));
            }

            foldersByAccountName.get(accountName).addFolder(path);
        }

    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        if (validateStringInput(accountName, folderPath, ruleDefinition)) {
            if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
                throw new IllegalArgumentException(String.format(INVALID_PRIORITY_MESSAGE, priority));
            }

            if (!emailsByAccountName.containsKey(accountName)) {
                throw new AccountNotFoundException(String.format(ACCOUNT_NOT_EXIST_MESSAGE, accountName));
            }

            AccountFolder folder = foldersByAccountName.get(accountName);
            if (folder.hasFolder(folderPath)) {
                Rule newRule = new Rule(folderPath, accountName, priority);
                Queue<Rule> rules = rulesByAccountName.get(accountName);
                for (Rule rule : rules) {
                    if (rule.isConflictingWith(newRule)) {
                        return;
                    }
                }

                rules.add(newRule);
                folder.update(newRule);
            }

        }

    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        if (validateStringInput(accountName, mailMetadata, mailContent)) {
            if (!emailsByAccountName.containsKey(accountName)) {
                throw new AccountNotFoundException(String.format(ACCOUNT_NOT_EXIST_MESSAGE, accountName));
            }

            Mail mail = generateMail(mailMetadata, mailContent);
            Queue<Rule> rules = rulesByAccountName.get(accountName);
            for (Rule rule : rules) {
                if (rule.isApplicable(mail)) {
                    foldersByAccountName.get(accountName).addMail(mail, rule.folderPath());
                    return;
                }
            }

            foldersByAccountName.get(accountName).addMailToInbox(mail);
        }
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        if (validateStringInput(account, folderPath)) {
            if (!emailsByAccountName.containsKey(account)) {
                throw new AccountNotFoundException(String.format(ACCOUNT_NOT_EXIST_MESSAGE, account));
            }

            AccountFolder accountFolder = foldersByAccountName.get(account);
            return accountFolder.mails(folderPath);
        }

        return null;
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        if (validateStringInput(accountName, mailMetadata, mailContent)) {
            String updatedMailMetadata = mailMetadata;
            if (!emailsByAccountName.containsKey(accountName)) {
                throw new AccountNotFoundException(String.format(ACCOUNT_NOT_EXIST_MESSAGE, accountName));
            }

            if (!mailMetadata.contains(SENDER_METADATA)) {
                String senderMetadata = String.format("\n%s %s\n", SENDER_METADATA, emailsByAccountName.get(accountName));
                updatedMailMetadata = mailMetadata + senderMetadata;
            }

            Mail mail = generateMail(updatedMailMetadata, mailContent);
            Set<String> recipientEmails = mail.recipients();
            for (String email : recipientEmails) {
                Account recipientsAccount = accountsByEmail.get(email);
                if (recipientsAccount != null) {
                   receiveMail(recipientsAccount.name(), updatedMailMetadata, mailContent);
                }
            }

            foldersByAccountName.get(accountName).addMailToSent(mail);
        }
    }

    private boolean validateStringInput(String... input) {
        for (String s : input) {
            if (s == null || s.isEmpty() || s.isBlank()) {
                throw new IllegalArgumentException(String.format(INVALID_STRING_MESSAGE, s));
            }
        }

        return true;
    }

    private Mail generateMail(String mailMetadata, String mailContent) {
        String[] metadata = mailMetadata.split(LINE_SEPARATOR);
        Account sender = null;
        String subject = "";
        Set<String> recipients = new HashSet<>();
        LocalDateTime received = null;
        int sepIndex = 0;
        for (String line : metadata) {
            sepIndex = line.indexOf(METADATA_SEPARATOR) + 1;
            if (line.startsWith(SENDER_METADATA)) {
                sender = accountsByEmail.get(line.substring(sepIndex));
            } else if (line.startsWith(SUBJECT_METADATA)) {
                subject = line.substring(sepIndex);
            } else if (line.startsWith(RECIPIENTS_METADATA)) {
                line = line.substring(sepIndex);
                recipients = new HashSet<>(List.of(line.split(RECIPIENTS_METADATA_SEPARATOR)));
            } else if (line.startsWith(RECEIVED_METADATA)) {
                received = LocalDateTime.parse(line.substring(sepIndex), formatter);
            }
        }

        return new Mail(sender, recipients, subject, mailContent, received);
    }

}
