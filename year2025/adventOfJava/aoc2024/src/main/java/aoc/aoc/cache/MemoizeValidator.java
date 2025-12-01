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
            if (illegalMethodModifierConfiguration(modifiers))
                throw new IllegalStateException(buildExceptionMessage(method));
        }
    }

    private static boolean illegalMethodModifierConfiguration(int modifiers) {
        return notPublicOrProtected(modifiers) || Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
    }

    private static boolean notPublicOrProtected(int modifiers) {
        return !(Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers));
    }

    private static String buildExceptionMessage(Method method) {
        return "@%s annotation can only be applied to non-static public or private methods: %s"
                .formatted(
                        Memoize.class.getName(),
                        "%s#%s".formatted(method.getDeclaringClass().getName(), method.getName())
                );
    }
}
