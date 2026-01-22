package core.play.utils;

import scala.Tuple2;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;

public class ScalaUtils {

    public static <T, R> Tuple2<T, R> toTuple(final T key, final R value) {
        return new Tuple2<>(key, value);
    }

    @SafeVarargs
    public static <T> Seq<T> toSeq(final T... args) {
        final List<T> headers = Arrays.asList(args);
        return JavaConversions.asScalaBuffer(headers).toSeq();
    }

}
