package my.spring.boot.stream;

//from https://www.oreilly.com/content/handling-checked-exceptions-in-java-streams/
@FunctionalInterface
public interface StreamForeachFunction<T, E extends Exception> {
    void apply(T t) throws E;
}

