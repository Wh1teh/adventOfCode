package aoc.aoc.cache;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;

public class MemoizeApplier {

    private MemoizeApplier() {
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T recreateWithMemoizeApplied(T object) {
        MemoizeValidator.validateMemoizeUsage(object.getClass());
        return (T) applyInterceptor(object.getClass());
    }

    private static <T> Object applyInterceptor(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T proxiedClass;
        try (var dynamicType = getDynamicType(clazz)) {
            proxiedClass = dynamicType
                    .load(MemoizeApplier.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        }

        return proxiedClass;
    }

    private static <T> DynamicType.Unloaded<T> getDynamicType(Class<T> clazz) {
        var interceptor = new CacheDecorator();
        return new ByteBuddy().subclass(clazz)
                .method(isAnnotatedWith(Memoize.class))
                .intercept(to(interceptor))
                .make();
    }
}
