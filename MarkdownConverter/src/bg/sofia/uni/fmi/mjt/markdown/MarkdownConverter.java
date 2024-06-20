package bg.sofia.uni.fmi.mjt.markdown;

import bg.sofia.uni.fmi.mjt.markdown.converter.*;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MarkdownConverter implements MarkdownConverterAPI {

    public static final String OPENING = "<html>\n<body>";
    public static final String CLOSING = "</body>\n</html>";
    public static final String SOURCE_EXT = "*.md";
    public static final String TARGET_EXT = ".html";
    public List<Converter> converters;
    public Converter headingConverter;
    public MarkdownConverter() {
        converters = List.of(new BoldConverter(), new ItalicConverter(), new CodeConverter());
        headingConverter = new HeadingConverter();
    }

    @Override
    public void convertMarkdown(Reader source, Writer output) {
        BufferedReader reader = new BufferedReader(source);
        PrintWriter writer = new PrintWriter(output);
        writer.println(OPENING);
        String line;
        try {
            while((line = reader.readLine()) != null) {
                if (line.strip().equals("")) {
                    writer.println(line);
                    continue;
                }

                if (headingConverter.isApplicable(line)) {
                    writer.println(headingConverter.apply(line));
                    continue;
                }


                for (Converter converter : converters) {
                    if (converter.isApplicable(line)) {
                        line = converter.apply(line);
                    }
                }

                writer.println(line);
            }

            writer.println(CLOSING);

        } catch (IOException ioExc) {
            throw new RuntimeException("Error occurred while processing markdown file", ioExc);
        }

    }

    @Override
    public void convertMarkdown(Path from, Path to) {
        try (var reader = new FileReader(from.toString());
        var writer = new FileWriter(to.toString())) {
            convertMarkdown(reader, writer);
        } catch (FileNotFoundException fnfExc) {
            throw new RuntimeException("Error occurred while opening file.", fnfExc);
        } catch (IOException ioExc) {
            throw new RuntimeException("Error occurred while processing markdown file", ioExc);
        }

    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(sourceDir, SOURCE_EXT)) {
            for (Path source : dirStream) {
                String fileName = source.getFileName().toString().split("\\.")[0];
                Path target = Path.of(String.valueOf(targetDir), fileName + TARGET_EXT);
                //Path target = targetDir.resolve(fileName + TARGET_EXT);
                convertMarkdown(source, target);
            }
        } catch (IOException ioExc) {
            throw new RuntimeException("Error occurred while processing markdown file", ioExc);
        }
    }
}
