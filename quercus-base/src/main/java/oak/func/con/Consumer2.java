package oak.func.con;

import oak.func.Functional;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface Consumer2<T1, T2> extends BiConsumer<T1, T2>, Functional.Con {
}
