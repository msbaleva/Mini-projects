package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {

    private static final String RULE_ALREADY_DEFINED_MESSAGE = "Rule condition already exists.";
    private static final int CONDITION_COUNT = 4;
    private String folderPath;
    private int priority;
    private Set<String> subjectIncludes = new HashSet<>();
    private Set<String> subjectOrBodyIncludes = new HashSet<>();
    private Set<String> recipientsIncludes = new HashSet<>();
    private String sender;

    public Rule(String folderPath, String ruleDefinition, int priority) {
        this.folderPath = folderPath;
        this.priority = priority;
        String[] conditions = ruleDefinition.split(System.lineSeparator());
        if (conditions.length > CONDITION_COUNT) {
            throw new RuleAlreadyDefinedException(String.format(RULE_ALREADY_DEFINED_MESSAGE));
        }

        for (String condition : conditions) {
            if (condition.startsWith("subject-includes:")) {
                this.subjectIncludes = new HashSet<>(List.of(condition.split(" ", 1)[1].strip().split(",")));
            } else if (condition.startsWith("subject-or-body-includes:")) {
                this.subjectOrBodyIncludes = new HashSet<>(List.of(condition.split(" ", 1)[1].strip().split(",")));
            } else if (condition.startsWith("recipients-includes:")) {
                this.recipientsIncludes = new HashSet<>(List.of(condition.split(" ", 1)[1].strip().split(",")));
            } else if (condition.startsWith("sender:")) {
                this.sender = condition.split(" ", 1)[1];
            }
        }
    }

    public boolean isApplicable(Mail mail) {
        boolean senderIsApplicable = sender == null || sender.equals(mail.sender().name());
        boolean recipientIsApplicable = recipientsIncludes.isEmpty();
        for (String recipient : recipientsIncludes) {
            if (mail.recipients().contains(recipient)) {
                recipientIsApplicable = true;
                break;
            }
        }

        boolean subjectIsApplicable = subjectIncludes.isEmpty();
        Set<String> subjectKeywords = new HashSet<>(List.of(mail.subject().toLowerCase().replaceAll("\\p{Punct}", "").toLowerCase()));
        subjectIsApplicable = subjectKeywords.containsAll(subjectIncludes);
        boolean subjectOrBodyAreApplicable = subjectOrBodyIncludes.isEmpty();
        Set<String> subjectOrBodyKeywords = new HashSet<>(List.of(mail.body().toLowerCase().replaceAll("\\p{Punct}", "").toLowerCase()));
        subjectOrBodyAreApplicable = subjectKeywords.containsAll(subjectOrBodyIncludes) || subjectOrBodyKeywords.containsAll(subjectOrBodyIncludes);

        return senderIsApplicable && recipientIsApplicable && subjectIsApplicable && subjectOrBodyAreApplicable;
    }

    public String folderPath() {
        return folderPath;
    }

    public int priority() {
        return priority;
    }

    public boolean isConflictingWith(Rule other) {
        return sender.equals(other.sender) && priority == other.priority &&
                subjectIncludes.containsAll(other.subjectIncludes) && subjectOrBodyIncludes.containsAll(other.subjectOrBodyIncludes)
                && recipientsIncludes.containsAll(other.recipientsIncludes) && !folderPath.equals(other.folderPath);
        
    }
}
