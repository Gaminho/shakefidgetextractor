package extract;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public final class ImgDownloader {

    private static final String AUTHORIZED_REFERER = "https://fr.4m7.de/sammelalbum/dungeons.php?dungeon=15&schatten=1";

    private static InputStream downloadImg(final String imgUrl) {
        final HttpClient client = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(imgUrl);
        httpGet.addHeader("referer", AUTHORIZED_REFERER);
        try {
            final HttpResponse response = client.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            System.err.println("Unable to download image: " + e.getMessage());
            return null;
        }
    }

    private static void saveImg(final InputStream imgStream,
                               final String target) {
        if (Objects.nonNull(imgStream)) {
            try {
                Files.copy(imgStream, Paths.get(target.concat(".jpg")));
                imgStream.close();
            } catch (IOException e) {
                System.err.println("Unable to save image: " + e.getMessage());
            }
        } else {
            System.err.println("Img stream was null");
        }
    }

    public static void downloadImg(final String imgUrl,
                                   final String target) {
        final InputStream imgStream =  downloadImg(imgUrl);
        saveImg(imgStream, target);
    }

    private ImgDownloader() {
    }
}
