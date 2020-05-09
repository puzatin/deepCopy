package net.puzatin.copy;

import java.lang.reflect.*;
import java.util.*;

public class CopyUtils {

    private CopyUtils (){

    }

    private static final List<Class<?>> immutableClasses = List.of(String.class, Integer.class, Boolean.class, Float.class, Long.class, Double.class, Byte.class, Character.class, Short.class, UUID.class);
    private static final Map<Object, Object> copied = new IdentityHashMap<>();

    public static <T> T deepCopy(T object) {

        Class<?> clazz = object.getClass();

        T newInstance;

        if(clazz.isArray())
            newInstance = newArrayInstance(object);
                else newInstance = (T) newInstance(clazz);

        copied.put(object, newInstance);

        return copyFields(clazz, object, newInstance);

    }


    private static <T> T copyFields(Class<?> clazz, T object, T newInstance) {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){

            field.setAccessible(true);
            if(!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object value = field.get(object);
                    if (value != null) {
                        if (isImmutable(value.getClass())) {
                            field.set(newInstance, value);
                        }
                        else {
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
        return newInstance;
    }


    private static Constructor<?> getConstructorForInstance(Class<?> clazz) {

        Map<Constructor<?>, Integer> map = new HashMap<>();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                if(!Modifier.isPrivate(constructor.getModifiers()))
                    if (constructor.getParameterCount() == 0)
                         return constructor;
                     else map.put(constructor, constructor.getParameterCount());
            }

            if(map.isEmpty())
                throw new NoSuchElementException("You can't clone a singleton!");

        Optional<Map.Entry<Constructor<?>, Integer>> constructor = map.entrySet().stream()
                .min(Map.Entry.comparingByValue());

        return constructor.map(Map.Entry::getKey).orElse(null);

    }



    private static <T> T newInstance(Class<T> clazz){

        Constructor<?> constructor = getConstructorForInstance(clazz);

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] par = new Object[parameterTypes.length];

            for (int i = 0; i < par.length; i++) {
                if(parameterTypes[i].getTypeName().equals("boolean")){
                    par[i] = false;
                } else if (parameterTypes[i].isPrimitive())
                        par[i] = 0;
                    else par[i] = null;
            }

        try {
           return (T) constructor.newInstance(par);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static <T> T newArrayInstance(T o){

        Class<?> clazz = o.getClass();

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
