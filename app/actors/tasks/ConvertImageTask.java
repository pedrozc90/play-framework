package actors.tasks;

import core.objects.FileMetadata;
import core.utils.FileUtils;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.RecursiveTask;

@AllArgsConstructor
public class ConvertImageTask extends RecursiveTask<FileMetadata> {

    private final byte[] bytes;
    private final String filename;
    private final String target;

    private final FileUtils fileUtils = FileUtils.getInstance();

    @Override
    protected FileMetadata compute() {
        try (final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
             final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final BufferedImage image = ImageIO.read(stream);

            final String basename = fileUtils.getBasename(filename);
            final String extension = fileUtils.getExtension(filename);

            ImageIO.write(image, target, output);

            return new FileMetadata(basename + "." + target, output.toByteArray(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
