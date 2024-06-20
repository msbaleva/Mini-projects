package bg.sofia.uni.fmi.mjt.markdown.converter;

public interface Converter {

    boolean isApplicable(String line);

    String apply(String line);
}
