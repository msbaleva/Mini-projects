package bg.sofia.uni.fmi.mjt.markdown.converter;

public class BoldConverter extends AbstractConverter {
    public BoldConverter() {
        super("**", "<strong>", "</strong>");
    }
}
