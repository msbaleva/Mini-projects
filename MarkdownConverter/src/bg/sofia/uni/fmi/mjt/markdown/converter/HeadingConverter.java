package bg.sofia.uni.fmi.mjt.markdown.converter;

public class HeadingConverter implements Converter {

    public static final String MARKDOWN_SYMBOL = "#";
    public static String openingTag = "<h%s>";
    public static String closingTag = "</h%s>";
    public HeadingConverter() {

    }

    @Override
    public boolean isApplicable(String line) {
        return line.contains(MARKDOWN_SYMBOL);
    }

    @Override
    public String apply(String line) {
        int level = 1;
        String toReplace;
        int index = line.indexOf(MARKDOWN_SYMBOL);
        int bound = line.indexOf(MARKDOWN_SYMBOL) + 6;
        for (int i = index + 1; i < bound; i++) {
            if (line.charAt(i) == MARKDOWN_SYMBOL.charAt(0)) {
                level++;
                continue;
            }

            break;
        }

        toReplace = MARKDOWN_SYMBOL.repeat(level) + " ";
        return line.replace(toReplace, String.format(openingTag, level)) + String.format(closingTag, level);
    }
}