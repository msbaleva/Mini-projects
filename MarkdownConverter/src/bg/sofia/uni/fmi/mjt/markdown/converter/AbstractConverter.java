package bg.sofia.uni.fmi.mjt.markdown.converter;

import java.util.regex.Pattern;

public abstract class AbstractConverter implements Converter {

    private String markdownSymbol;
    private String openingTag;
    private String closingTag;
    public static final char ESCAPE_CHAR = '\\';
    public static final char SPECIAL_CHAR = '*';

    public AbstractConverter(String markdownSymbol, String openingTag, String closingTag) {
        this.markdownSymbol = markdownSymbol;
        this.openingTag = openingTag;
        this.closingTag = closingTag;
    }
    @Override
    public boolean isApplicable(String line) {
        //String pattern = markdownSymbol + "%s" + markdownSymbol;
        //return Pattern.compile(pattern).matcher(line).find();
        return line.contains(markdownSymbol);
    }

    private String escapeSpecialCharacters(String str) {
        StringBuilder result = new StringBuilder();
        boolean hasSpecial = false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == SPECIAL_CHAR) {
                hasSpecial = true;
                result.append(ESCAPE_CHAR).append(SPECIAL_CHAR);
            }
        }

        return hasSpecial ? result.toString() : str;
    }

    @Override
    public String apply(String line) {
        String escaped = escapeSpecialCharacters(markdownSymbol);
        return line.replaceFirst(escaped, openingTag).replaceFirst(escaped, closingTag);
    }
}
