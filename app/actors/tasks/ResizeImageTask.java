package actors.tasks;

import core.objects.FileMetadata;
import core.utils.FileUtils;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.RecursiveTask;

@AllArgsConstructor
public class ResizeImageTask extends RecursiveTask<FileMetadata> {

    private final byte[] bytes;
    private final String filename;
    private final int width;
    private final int height;

    @Override
    protected FileMetadata compute() {
        try (final ByteArrayInputStream input = new ByteArrayInputStream(bytes);
             final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final BufferedImage image = ImageIO.read(input);

            int type = (image.getType() != 0) ? image.getType() : BufferedImage.TYPE_INT_ARGB;
            final BufferedImage resized = new BufferedImage(width, height, type);

            final Graphics2D graphics = resized.createGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();

            final String extension = FileUtils.getExtension(filename);
            final String basename = FileUtils.getBasename(filename);
            final String contentType = FileUtils.getContentType(filename);

            final String newFilename = String.format("%s_%dx%d.%s", basename, width, height, extension);

            ImageIO.write(resized, extension, output);

            return new FileMetadata(newFilename, output.toByteArray(), contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
