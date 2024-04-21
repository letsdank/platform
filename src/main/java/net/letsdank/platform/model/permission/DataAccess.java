package net.letsdank.platform.model.permission;

import jakarta.persistence.Entity;

import java.lang.annotation.Annotation;

public class DataAccess {
    public static boolean canEditEntity(Class<?> entityClass) {
        if (!hasAnnotation(entityClass, Entity.class)) {
            throw new IllegalArgumentException("Invalid entityClass: can't find an @Entity annotation");
        }

        // TODO: Not implemented
        return true;
    }

    private static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Annotation a : clazz.getAnnotations()) {
            if (a.getClass() == annotationClass) return true;
        }
        return false;
    }
}
