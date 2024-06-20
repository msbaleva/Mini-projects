package bg.sofia.uni.fmi.mjt.markdown.converter;

public class ItalicConverter extends AbstractConverter {
    public ItalicConverter() {
        super("*", "<em>", "</em>");
    }
}
