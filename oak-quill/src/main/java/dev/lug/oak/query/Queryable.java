package dev.lug.oak.query;

import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.fun.Function4;
import dev.lug.oak.func.fun.IntFunction2;
import dev.lug.oak.func.pre.IntPredicate2;
import dev.lug.oak.query.tuple3.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Queryable<T> extends Iterable<T> {
  default void eventually(final Consumer1<T> consuming) {
    for (final var value : this) consuming.accept(value);
  }

  enum P {
    ;

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> IntFunction2<T, R> ith(final IntFunction2<T, R> index) { return index; }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> AnyAsAny<T, R> as(final AnyAsAny<T, R> asAny) { return asAny; }

    @Contract(value = "_ -> param1", pure = true)
    public static <T1, T2, R> Tuple2AsAny<T1, T2, R> as(final Tuple2AsAny<T1, T2, R> asAny) { return asAny; }

    public static <V1, V2, T1, T2, T extends Tuple2<T1, T2>> Tuple2AsTuple2<V1, V2, T1, T2, T> as(final Tuple2AsTuple2<V1, V2, T1, T2, T> as) {
      return as;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> as(final Function3<T1, T2, T3, R> asAny) { return asAny; }

    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T, R, Q extends Queryable<R>> AnyAsQueryable<T, R, Q> array(final AnyAsArray<T, R> array) {
      return it -> (Q) Many.from(array.apply(it));
    }

    public static <T, R, Q extends Queryable<R>> AnyAsQueryable<T, R, Q> many(final AnyAsQueryable<T, R, Q> many) {
      return many;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, U extends Tuple, Q extends Queryable<U>> Function1<T, Q> tuples2(final Function1<T, Q> many) { return many; }

    @Contract(value = "_ -> param1", pure = true)
    public static <V, R1, R2, T extends Tuple2<R1, R2>> Function1<V, T> tuple2(final Function1<V, T> tuple2) { return tuple2; }

    @Contract(value = "_ -> param1", pure = true)
    public static <V, R1, R2, R3, T extends Tuple3<R1, R2, R3>> Function1<V, T> tuple3(final Function1<V, T> tuple3) { return tuple3; }
  }

  enum F {
    ;

    @Contract(value = "_ -> param1", pure = true)
    public static <T> IntPredicate2<T> ith(final IntPredicate2<T> index) { return index; }
  }

  interface AnyAsAny<T, R> extends Function1<T, R> {}
  interface Tuple2AsAny<T1, T2, R> extends Function2<T1, T2, R> {}
  interface Tuple3AsAny<T1, T2, T3, R> extends Function3<T1, T2, T3, R> {}

  interface AnyAsArray<T, R> extends Function1<T, R[]> {}

  interface AnyAsQueryable<T, R, Q extends Queryable<R>> extends Function1<T, Q> {}
  interface Tuple2AsQueryable<V1, V2, R, Q extends Queryable<R>> extends Function2<V1, V2, Q> {}
  interface Tuple3AsQueryable<V1, V2, V3, R, Q extends Queryable<R>> extends Function3<V1, V2, V3, Q> {}

  interface AnyAsTuple<V, T extends Tuple> extends Function1<V, T> {}
  interface Tuple2AsTuple<V1, V2, T extends Tuple> extends Function2<V1, V2, T> {}
  interface Tuple3AsTuple<V1, V2, V3, T extends Tuple> extends Function3<V1, V2, V3, T> {}
}
