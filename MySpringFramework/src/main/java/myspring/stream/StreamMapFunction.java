package myspring.stream;

//from https://www.oreilly.com/content/handling-checked-exceptions-in-java-streams/
@FunctionalInterface
public interface StreamMapFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}

