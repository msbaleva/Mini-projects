package bg.sofia.uni.fmi.mjt.markdown;

import bg.sofia.uni.fmi.mjt.markdown.converter.Converter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarkdownConverterTest {

    public static final String OPENING = "<html>\n<body>\n";
    public static final String CLOSING = "</body>\n</html>\n";
    public static final String LINE1 = "Converted Markdown content here\n";
    public static final String EXP_LINE1 = "Converted Markdown content here\n";
    public static final String LINE2 = "#### Heading level 4\n";
    public static final String EXP_LINE2 = "<h4>Heading level 4</h4>\n";
    public static final String LINE3 = "I just love **bold text**.\n";
    public static final String EXP_LINE3 = "I just love <strong>bold text</strong>.\n";
    public static final String LINE4 = "Love**is**bold\n";
    public static final String EXP_LINE4 = "Love<strong>is</strong>bold\n";
    public static final String LINE5 = "Italicized text is the *cat's meow*.\n";
    public static final String EXP_LINE5 = "Italicized text is the <em>cat's meow</em>.\n";
    public static final String LINE6 = "A*cat*meow\n";
    public static final String EXP_LINE6 = "A<em>cat</em>meow\n";
    public static final String LINE7 = "Always `.close()` your streams\n";
    public static final String EXP_LINE7 = "Always <code>.close()</code> your streams\n";
    public static final String LINE8 = "`.close()` *your* **eyes**\n";
    public static final String EXP_LINE8 = "<code>.close()</code> <em>your</em> <strong>eyes</strong>\n";

    public static final String INPUT = LINE1 + LINE2 + LINE3 + LINE4 + LINE5 +LINE6 + LINE7 + LINE8;
    public static final String EXP_OUTPUT = OPENING + EXP_LINE1 + EXP_LINE2 + EXP_LINE3 +
            EXP_LINE4 + EXP_LINE5 + EXP_LINE6 + EXP_LINE7 + EXP_LINE8 + CLOSING;

    public static final String INPUT_FILE_NAME = "D:\\JavaProjects2023\\HtmlFiles\\markdown.md";
    public static final String OUTPUT_FILE_NAME = "D:\\JavaProjects2023\\HtmlFiles\\converted.html";
    public static final String SOURCE_DIR_NAME = "D:\\JavaProjects2023\\HtmlFiles\\mdDir";
    public static final String TARGET_DIR_NAME = "D:\\JavaProjects2023\\HtmlFiles\\htmlDir";
    public static final int CNT_FILES = 10;
    private static final String SRC_EXT = ".md";
    private static final String TAR_EXT = ".html";

    public static MarkdownConverter converter = new MarkdownConverter();

    @Test
    void testConvertMarkdown() {
        try (var reader = new BufferedReader(new StringReader(INPUT)); var writer = new StringWriter()) {
            converter.convertMarkdown(reader, writer);
            String actual = removeCRLF(writer.toString());
            assertEquals(EXP_OUTPUT, actual, "Convert markdown to html.");

        } catch (IOException ioExc) {
            throw new RuntimeException("Problem occurred while reading/writing.", ioExc);
        }
    }

    @Test
    void testConvertMarkdownPath() {
        Path inputFilePath = Paths.get(INPUT_FILE_NAME);
        Path outputFilePath = Paths.get(OUTPUT_FILE_NAME);
        try {
            File iFile = Files.createFile(inputFilePath).toFile();
            File oFile = Files.createFile(outputFilePath).toFile();
            Files.write(inputFilePath, INPUT.getBytes());
            converter.convertMarkdown(inputFilePath, outputFilePath);
            String actual = removeCRLF(Files.readString(outputFilePath));
            assertEquals(EXP_OUTPUT, actual, "Convert markdown to html by path.");
            iFile.delete();
            oFile.delete();
        } catch (IOException ioExc) {
            throw new RuntimeException("Problem occurred while creating files", ioExc);
        }
    }

    @Test
    void testConvertDirectory() {
        Path sourcePath = Paths.get(SOURCE_DIR_NAME);
        Path targetPath = Paths.get(TARGET_DIR_NAME);
        File[] iFiles = new File[CNT_FILES];
        File[] oFiles = new File[CNT_FILES];
        try {
            for (int i = 0; i < CNT_FILES; i++) {
                Path iPath = Path.of(SOURCE_DIR_NAME + "\\" + i + SRC_EXT);
                Path oPath = Path.of(TARGET_DIR_NAME + "\\" + i + TAR_EXT);
                iFiles[i] = Files.createFile(iPath).toFile();
                Files.write(iPath, INPUT.getBytes());
                oFiles[i] = Files.createFile(oPath).toFile();
            }

            converter.convertAllMarkdownFiles(sourcePath, targetPath);
            for (int i = 0; i < CNT_FILES; i++) {
                Path path = Path.of(TARGET_DIR_NAME + "\\" + i + TAR_EXT);
                String actual = removeCRLF(Files.readString(path));
                assertEquals(EXP_OUTPUT, actual, "Convert markdown directory to html.");
                iFiles[i].delete();
                oFiles[i].delete();
            }


        } catch (IOException ioExc) {
            throw new RuntimeException("Problem occurred creating files.", ioExc);
        }

    }

    private String removeCRLF(String str) {
        return str.replace("\r", "");
    }

}
