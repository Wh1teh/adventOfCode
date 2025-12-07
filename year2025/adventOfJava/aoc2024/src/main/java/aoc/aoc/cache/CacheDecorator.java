package aoc.aoc.cache;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class CacheDecorator {

    private static final Object VOID = new Object();
    
    private final Map<Identifier, Object> cache = new ConcurrentHashMap<>();
    private final Map<Method, ArgFilter> filterArgsCache = new ConcurrentHashMap<>();

    private static final class Identifier {
        private final Method method;
        private final Object[] args;
        private final int hash;

        Identifier(Method method, Object[] args) {
            this.method = method;
            this.args = args;
            this.hash = computeHash(method, args);
        }

        private static int computeHash(Method m, Object[] args) {
            int h = m.hashCode();
            for (Object o : args)
                h = 31 * h + (o != null ? o.hashCode() : 0);

            return h;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (!(other instanceof Identifier o))
                return false;
            if (!method.equals(o.method))
                return false;
            return Arrays.equals(args, o.args);
        }
    }

    @SuppressWarnings("unused")
    @RuntimeType
    public Object intercept(@AllArguments Object[] args,
                            @Origin Method method,
                            @SuperCall Callable<?> originalMethod) throws Exception {

        var filter = filterArgsCache.computeIfAbsent(method, this::computeFilter);

        var filteredArgs = filter.project(args);

        var key = new Identifier(method, filteredArgs);
        var value = cache.get(key);
        if (value != null) return value;

        value = originalMethod.call();
        cache.put(key, value != null ? value : VOID);
        return value;
    }

    private ArgFilter computeFilter(Method m) {
        var paramAnnotations = m.getParameterAnnotations();
        int[] ignored = computeIgnoredIndices(paramAnnotations);
        return new ArgFilter(paramAnnotations.length, ignored);
    }

    private int[] computeIgnoredIndices(Annotation[][] paramAnnotations) {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < paramAnnotations.length; i++)
            for (Annotation a : paramAnnotations[i])
                if (a.annotationType() == MemoIgnore.class)
                    list.add(i);

        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private static class ArgFilter {
        final int[] kept;

        ArgFilter(int paramCount, int[] ignored) {
            if (ignored.length == 0)
                kept = null;
            else
                kept = buildKept(paramCount, ignored);
        }

        private int[] buildKept(int n, int[] ignored) {
            int[] tmp = new int[n - ignored.length];
            int ti = 0;
            int j = 0;
            for (int i = 0; i < n; i++) {
                if (j < ignored.length && ignored[j] == i)
                    j++;
                else
                    tmp[ti++] = i;
            }
            return tmp;
        }

        Object[] project(Object[] args) {
            if (kept == null)
                return args;
            var out = new Object[kept.length];
            for (int i = 0; i < kept.length; i++)
                out[i] = args[kept[i]];

            return out;
        }
    }
}
