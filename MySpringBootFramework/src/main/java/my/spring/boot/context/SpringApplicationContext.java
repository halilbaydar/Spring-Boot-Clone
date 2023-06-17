package my.spring.boot.context;

import my.spring.boot.annotation.MyAutowired;
import my.spring.boot.annotation.MyComponentScan;
import my.spring.boot.stream.StreamWrapper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SpringApplicationContext {
    private static final ConcurrentHashMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public SpringApplicationContext(Class<?> springBootMain) {
        try {
            MySpring.initializeSpringContext(springBootMain);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Stream<Map.Entry<Class<?>, Object>> getInstanceStream() {
        return SpringApplicationContext.instances.entrySet().stream();
    }

    public <T> T getComponent(Class<T> componentClass) {
        T t = (T) instances.get(componentClass);
        if (t == null) {
            throw new RuntimeException("not found");
        }
        Field[] declaredFields = componentClass.getDeclaredFields();
        injectClass(t, declaredFields);
        return t;
    }

    private <T> void injectClass(T t, Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                field.setAccessible(true);
                Class<?> typeOfClass = field.getType();
                Object innerObject = instances.get(typeOfClass);
                if (innerObject == null) {
                    throw new RuntimeException("in order to inject class you have to annotate with pure spring annotations");
                }
                try {
                    field.set(t, innerObject);
                    Field[] innerDeclaredFields = typeOfClass.getDeclaredFields();
                    injectClass(typeOfClass, innerDeclaredFields);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class MySpring {
        public static void initializeSpringContext(Class<?> bootClass) {

            if (!bootClass.isAnnotationPresent(MyComponentScan.class)) {
                throw new RuntimeException("app config class has to be annotated with configuration interface");
            }

            MyComponentScan myComponentScan = bootClass.getAnnotation(MyComponentScan.class);
            String[] path = myComponentScan.path();
            Class<? extends Annotation>[] supportedComponents = myComponentScan.supportedComponents();

            Stream<Class<?>> classes = findAllClasses(bootClass, path);

            classes
                    .parallel()
                    .forEach(StreamWrapper.foreachWrapper(cls -> {
                        if (Stream.of(supportedComponents).anyMatch(cls::isAnnotationPresent)) {
                            Constructor<?> constructor = cls.getDeclaredConstructor();
                            Object instance = constructor.newInstance();
                            instances.put(cls, instance);
                        }
                    }));
        }

        private static Stream<Class<?>> findAllClasses(Class<?> bootClass, String... packageNames) {
            return Stream.of(packageNames)
                    .parallel()
                    .map(packageName -> packageName.length() > 1 ? packageName.replace(".", "/") : packageName)
                    .map(packageName -> Objects.requireNonNull(bootClass.getResource(packageName)))
                    .map(StreamWrapper.mapWrapper(URL::toURI))
                    .filter(Objects::nonNull)
                    .filter(uri -> uri.getScheme().equals("file")) //TODO include jar files
                    .map(StreamWrapper.mapWrapper(uri -> findAllPackageClasses(Paths.get(uri))))
                    .filter(Objects::nonNull)
                    .flatMap(Arrays::stream);
        }

        private static Class<?>[] findAllPackageClasses(Path paramPath) throws IOException {
            return Stream.of(findAllFilesUnderParentPackage(paramPath))
                    .map(StreamWrapper.mapWrapper(Class::forName))
                    .filter(Objects::nonNull)
                    .toArray(Class[]::new);
        }

        private static String[] findAllFilesUnderParentPackage(Path path) throws IOException {
            return Files.walk(path)
                    .filter(Files::exists)
                    .filter(Files::isRegularFile)
                    .filter(path1 -> path1.getFileName().toString().endsWith(".class"))
                    .map(mPath -> mPath.toString().split("classes/")[1]
                            .replace(File.separator, ".")
                            .replaceFirst("\\.class$", ""))
                    .toArray(String[]::new);
        }
    }
}
