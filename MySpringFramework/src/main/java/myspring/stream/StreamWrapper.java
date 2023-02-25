package myspring.stream;

import java.util.function.Consumer;
import java.util.function.Function;

//from https://www.oreilly.com/content/handling-checked-exceptions-in-java-streams/
public final class StreamWrapper {
    public static <T, R, E extends Exception>
    Function<T, R> mapWrapper(StreamMapFunction<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, E extends Exception>
    Consumer<T> foreachWrapper(StreamForeachFunction<T, E> fe) {
        return arg -> {
            try {
                fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
