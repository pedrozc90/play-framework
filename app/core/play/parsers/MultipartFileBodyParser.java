package core.play.parsers;

import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import play.api.http.HttpConfiguration;
import play.api.http.HttpErrorHandler;
import play.core.parsers.Multipart;
import play.libs.streams.Accumulator;
import play.mvc.BodyParser;
import play.mvc.Http;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class MultipartFileBodyParser extends BodyParser.DelegatingMultipartFormDataBodyParser<File> {

    @Inject
    public MultipartFileBodyParser(final Materializer materializer,
                                   final HttpConfiguration config,
                                   final HttpErrorHandler errorHandler) {
        super(materializer, config.parser().maxDiskBuffer(), errorHandler);
    }

    @Override
    public Function<Multipart.FileInfo, Accumulator<ByteString, Http.MultipartFormData.FilePart<File>>> createFilePartHandler() {
        return (fileInfo) -> {
            final String filename = fileInfo.fileName();
            final String partname = fileInfo.partName();
            final String contentType = fileInfo.contentType().getOrElse(null);
            final File file = generateTempFile();
            final String dispositionType = fileInfo.dispositionType();

            final Sink<ByteString, CompletionStage<IOResult>> sink = FileIO.toPath(file.toPath());

            return Accumulator.fromSink(
                sink.mapMaterializedValue((completionStage) ->
                    completionStage.thenApplyAsync(
                        (results) -> new Http.MultipartFormData.FilePart<>(
                            partname,
                            filename,
                            contentType,
                            file,
                            results.getCount(),
                            dispositionType
                        )
                    )
                )
            );
        };
    }

    /**
     * Generates a temp file directly without going through TemporaryFile.
     */
    private File generateTempFile() {
        try {
            final Path path = Files.createTempFile("multipartBody", "tempFile");
            return path.toFile();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
