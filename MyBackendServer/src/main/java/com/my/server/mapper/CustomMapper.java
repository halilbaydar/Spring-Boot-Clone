package com.my.server.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class CustomMapper {
    private static Field findSourceField(Field targetField, Set<Field> declaredSourceFields) {
        return declaredSourceFields.parallelStream().filter(field -> {
            field.setAccessible(true);
            String sourceFieldName = field.getName();
            return sourceFieldName.equals(targetField.getName()) &&
                    field.getType().equals(targetField.getType());
        }).findFirst().orElse(null);
    }

    public static Set<Field> findAllFields(Class<?> inputClass) {
        Set<Field> allFields = new HashSet<>();

        if (inputClass == null) {
            return allFields;
        }

        Field[] fields = inputClass.getDeclaredFields();
        allFields.addAll(Set.of(fields));
        allFields.addAll(findAllFields(inputClass.getSuperclass()));
        return allFields;
    }

    private static <O, Source, Target> O
    doMap(Object sourceInstance, Class<Source> sourceClass, Class<Target> targetClass) {
        Set<Field> declaredSourceFields = findAllFields(sourceClass);
        Set<Field> declaredTargetFields = findAllFields(targetClass);

        try {
            Object targetInstance = targetClass.getDeclaredConstructor().newInstance();

            for (Field field : declaredTargetFields) {
                Field sourceField = findSourceField(field, declaredSourceFields);
                if (sourceField != null) {
                    field.setAccessible(true);
                    field.set(targetInstance, sourceField.get(sourceInstance));
                }
            }
            return (O) targetInstance;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static <O, Target> O map(Object instance, Class<Target> targetClass) {
        if (instance.getClass().isPrimitive() || targetClass.isPrimitive()) {
            throw new RuntimeException("Provide a non primitive class");
        }

        if (instance.getClass().isInterface() || targetClass.isInterface()) {
            throw new RuntimeException("Provide a non interface class");
        }

        if (instance.getClass().isEnum() || targetClass.isEnum()) {
            throw new RuntimeException("Provide a non enum class");
        }

        if (Modifier.isAbstract(instance.getClass().getModifiers()) ||
                Modifier.isAbstract(targetClass.getModifiers())) {
            throw new RuntimeException("Provide a non abstract class");
        }

        return doMap(instance, instance.getClass(), targetClass);
    }
}
