package aoc.aoc.cache;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class CacheDecorator {

    private final Map<Identifier, Object> cache = new ConcurrentHashMap<>();

    private record Identifier(Object method, Integer hash) {
    }

    @SuppressWarnings("unused")
    @RuntimeType
    public Object intercept(@AllArguments Object[] args,
                            @Origin Method method,
                            @SuperCall Callable<?> originalMethod) throws Exception {

        var identifier = new Identifier(method, Arrays.hashCode(args));
        var memoized = cache.get(identifier);
        if (memoized != null)
            return memoized;

        var result = originalMethod.call();

        cache.put(identifier, result);
        return result;
    }
}
