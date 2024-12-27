package aoc.aoc.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MemoizeValidator {

    private MemoizeValidator() {
    }

    public static void validateMemoizeUsage(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Memoize.class))
                continue;

            int modifiers = method.getModifiers();
            if (!(Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)))
                throw new IllegalStateException(buildExceptionMessage(method));
        }
    }

    private static String buildExceptionMessage(Method method) {
        return "@%s annotation can only be applied to public or private methods: %s"
                .formatted(
                        Memoize.class.getName(),
                        "%s#%s".formatted(method.getDeclaringClass().getName(), method.getName())
                );
    }
}
