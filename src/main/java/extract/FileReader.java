package extract;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

public final class FileReader {

    public static String readFileAsString(final String filePath) {
        final File file = getFileFromResource(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found");
        } else if (!file.canRead()) {
            throw new IllegalStateException("Can not read file");
        } else {
            try {
                final String fileContent = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
                return cleanString(fileContent);
            } catch (IOException e) {
                throw new IllegalArgumentException("Can not read file as String");
            }
        }
    }

    private static File getFileFromResource(final String filePath) {
        final URL url = FileReader.class.getClassLoader().getResource(filePath);
        if (Objects.isNull(url)) {
            throw new IllegalArgumentException("File not found");
        } else {
            try {
                return new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URI");
            }
        }
    }

    private static String cleanString(final String fileContent) {
        return StringUtils.isNotBlank(fileContent) ?
                fileContent.replaceAll("\\s{2}", "") : fileContent;
    }

    private FileReader() {
    }
}
