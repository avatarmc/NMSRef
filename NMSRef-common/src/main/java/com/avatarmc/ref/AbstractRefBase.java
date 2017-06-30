package com.avatarmc.ref;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.SneakyThrows;

public class AbstractRefBase {
    private static final Constructor<MethodHandles.Lookup> NEW_LOOKUP;
    static {
        try {
            NEW_LOOKUP = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class);
            NEW_LOOKUP.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandles.Lookup getLookup(Class<?> clazz) {
        try {
            return NEW_LOOKUP.newInstance(clazz);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows(Throwable.class)
    protected static final RuntimeException sneakyThrow(Throwable e) {
        throw e;
    }

    public interface Getter<T, I> {
        T get(I instance);
    }

    public interface Accessor<T, I> extends Getter<T, I> {
        void set(I instance, T value);
    }

    public interface StaticGetter<T> {
        T get();
    }

    public interface StaticAccessor<T> extends StaticGetter<T> {
        void set(T value);
    }

    protected static <T, I> Accessor<T, I> access(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String fieldName) {
        MethodHandle setter;
        MethodHandle getter;

        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            setter = lookup.unreflectSetter(field);
            getter = lookup.unreflectGetter(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                    "Failed to locate field, available fields: "
                            + Arrays.stream(clazz.getDeclaredFields())
                                    .map(Field::getName).toArray(),
                    e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return new Accessor<T, I>() {
            @SneakyThrows(Throwable.class)
            @Override
            public T get(I instance) {
                return (T) getter.invoke(instance);
            }

            @SneakyThrows(Throwable.class)
            @Override
            public void set(I instance, T value) {
                setter.invoke(instance, value);
            }
        };
    }

    protected static <T, I> Getter<T, I> accessFinal(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String fieldName) {
        MethodHandle getter;

        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            getter = lookup.unreflectGetter(field);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return new Getter<T, I>() {
            @SneakyThrows(Throwable.class)
            @Override
            public T get(I instance) {
                return (T) getter.invoke(instance);
            }
        };
    }

    protected static <T> StaticAccessor<T> staticAccess(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String fieldName) {
        MethodHandle setter;
        MethodHandle getter;

        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            setter = lookup.unreflectSetter(field);
            getter = lookup.unreflectGetter(field);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return new StaticAccessor<T>() {
            @SneakyThrows(Throwable.class)
            @Override
            public T get() {
                return (T) getter.invoke();
            }

            @SneakyThrows(Throwable.class)
            @Override
            public void set(T value) {
                setter.invoke(value);
            }
        };
    }

    protected static <T> StaticGetter<T> staticAccessFinal(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String fieldName) {
        MethodHandle getter;

        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            getter = lookup.unreflectGetter(field);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return new StaticGetter<T>() {
            @SneakyThrows(Throwable.class)
            @Override
            public T get() {
                return (T) getter.invoke();
            }
        };
    }

    protected static MethodHandle methodAccess(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String methodName,
            Class<?> lambda) {
        Class<?>[] parameterTypes = lambda.getMethods()[0].getParameterTypes();
        Class<?>[] targetTypes = new Class<?>[parameterTypes.length - 1];

        System.arraycopy(
                parameterTypes, 1, targetTypes, 0, parameterTypes.length - 1);

        return methodAccess(lookup, clazz, methodName, targetTypes);
    }

    protected static MethodHandle staticMethodAccess(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String methodName,
            Class<?> lambda) {
        Class<?>[] parameterTypes = lambda.getMethods()[0].getParameterTypes();

        return methodAccess(lookup, clazz, methodName, parameterTypes);
    }

    private static MethodHandle methodAccess(
            MethodHandles.Lookup lookup,
            Class<?> clazz,
            String methodName,
            Class<?>[] targetTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, targetTypes);
            method.setAccessible(true);

            return lookup.unreflect(method);
        } catch (NoSuchMethodException e) {
            StringBuilder s = new StringBuilder(e.getMessage());
            s.append("\nContains methods:");
            for (Method declared : clazz.getDeclaredMethods()) {
                s.append("\n- ").append(declared.toString());
            }

            throw new RuntimeException(s.toString(), e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
