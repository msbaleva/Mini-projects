package bg.sofia.uni.fmi.mjt.mail;

import java.util.Comparator;

public class RuleComparator implements Comparator<Rule> {

    @Override
    public int compare(Rule r1, Rule r2) {
        return Integer.compare(r1.priority(), r2.priority());
    }
}
