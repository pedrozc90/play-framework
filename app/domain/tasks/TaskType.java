package domain.tasks;

import lombok.Getter;

@Getter
public enum TaskType {
    RESIZE_TEST("resize", 100, 100),
    RESIZE_1080p("resize", 1920, 1080),
    RESIZE_720p("resize", 1280, 720),
    CONVERT_JPG("convert", "jpg", "iamge/jpg"),
    CONVERT_PNG("convert", "png", "image/png"),
    CONVERT_JPEG("convert", "jpeg", "image/jpeg"),
    CONVERT_GIF("convert", "gif", "image/gif");

    private final String mode;
    private final int width;
    private final int heigth;
    private final String extension;
    private final String contentType;

    TaskType(final String mode, final int width, final int height, final String extension, final String contentType) {
        this.mode = mode;
        this.width = width;
        this.heigth = height;
        this.extension = extension;
        this.contentType = contentType;
    }

    TaskType(final String mode, final int width, final int height) {
        this(mode, width, height, null, null);
    }

    TaskType(final String mode, final String extension, final String contentType) {
        this(mode, -1, -1, extension, contentType);
    }

    public boolean isResize() {
        return mode.equals("resize");
    }

    public boolean isConversion() {
        return mode.equals("convert");
    }

}
