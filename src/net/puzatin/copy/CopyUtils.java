package net.puzatin.copy;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.*;

public class CopyUtils {

    private CopyUtils (){

    }

    private static final List<Class<?>> immutableClasses = List.of(String.class, Integer.class, Boolean.class, Float.class, Long.class, Double.class, Byte.class, Character.class, Short.class, UUID.class);
    // IdentityHashMap т.к нам нужно сравнивать объекты по ссылкам, а не по equals
    private static final Map<Object, Object> copied = new IdentityHashMap<>();

    public static <T> T deepCopy(T object) {

        Class<?> clazz = object.getClass();

        T newInstance;

        if(clazz.isArray())
            newInstance = newArrayInstance(clazz ,object);
                else newInstance = (T) newEmptyInstance(clazz);

        copied.put(object, newInstance);

        return copyFields(clazz, object, newInstance);

    }


    private static <T> T copyFields(Class<?> clazz, T object, T newInstance) {

        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);

                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        Object value = field.get(object);
                        if (value != null) {
                            if (isImmutable(value.getClass()) || value.getClass().isEnum()) {
                                field.set(newInstance, value);
                            } else {
                                if (isCopied(value)) {
                                    field.set(newInstance, copied.get(value));
                                } else {
                                    Object valueCopy = deepCopy(value);
                                    field.set(newInstance, valueCopy);
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != Object.class && clazz != null);

        return newInstance;
    }

    private static <T> T newEmptyInstance(Class<T> clazz){

        T newEmptyInstance = null;

        try {
            newEmptyInstance = (T) ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, Object.class.getConstructor()).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return newEmptyInstance;
    }


    private static <T> T newArrayInstance(Class<?> clazz, T o){

        int length = Array.getLength(o);
        T newInstance = (T) Array.newInstance(clazz.componentType(), length);

        if(clazz.componentType().isPrimitive()) {
            System.arraycopy(o, 0, newInstance, 0, length);
                 return newInstance;
        }

        for (int i = 0; i < length; i++) {
            Object element = Array.get(o, i);

            if (element != null) {
                if (isImmutable(element.getClass())) {
                    Array.set(newInstance, i, Array.get(o, i));
                } else {
                    if(isCopied(element)){
                        Array.set(newInstance, i, copied.get(element));
                    } else {
                        Object copyElement = deepCopy(element);
                        Array.set(newInstance, i, copyElement);
                    }
                }
            }
        }

        return newInstance;
    }


    private static boolean isImmutable(Class<?> clazz) {

        for (Class<?> claz : immutableClasses) {
            if(clazz.equals(claz))
                return true;
        }
        return false;
    }

    private static boolean isCopied(Object o) {
        for (Object ob : copied.keySet()) {
            if (o == ob)
                return true;
        }

        return false;
    }

}
