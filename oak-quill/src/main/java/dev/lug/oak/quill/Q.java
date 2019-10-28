package dev.lug.oak.quill;

import dev.lug.oak.quill.query.Queryable;
import dev.lug.oak.quill.tuple.Tuple;
import dev.lug.oak.quill.tuple.Tuple2;
import dev.lug.oak.quill.tuple.Tuple3;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.fun.IntFunction2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Q {
  ;

  @Contract(value = "_ -> param1", pure = true)
  public static <T, R> AsJust<T, R> just(final AsJust<T, R> just) { return just; }

  @Contract(value = "_ -> param1", pure = true)
  public static <T1, T2, R> AsJust2<T1, T2, R> just(final AsJust2<T1, T2, R> just2) { return just2; }

  @Contract(value = "_ -> param1", pure = true)
  public static <T1, T2, T3, R> Function3<T1, T2, T3, R> just(final Function3<T1, T2, T3, R> just3) { return just3; }

  @NotNull
  @Contract(pure = true)
  public static <T, R> AsMany<T, Structable<R>> many(final FromArray<T, R> array) {
    return it -> Queryable.from(array.apply(it));
  }

  @Contract(value = "_ -> param1", pure = true)
  public static <T, U extends Tuple, S extends Structable<U>> Function1<T, S> tuples2(final Function1<T, S> many) { return many; }

  @Contract(value = "_ -> param1", pure = true)
  public static <T, R> IntFunction2<T, R> ith(final IntFunction2<T, R> index) { return index; }

  @Contract(value = "_ -> param1", pure = true)
  public static <V, R1, R2, T extends Tuple2<R1, R2>> Function1<V, T> tuple2(final Function1<V, T> tuple2) { return tuple2; }

  @Contract(value = "_ -> param1", pure = true)
  public static <V, R1, R2, R3, T extends Tuple3<R1, R2, R3>> Function1<V, T> tuple3(final Function1<V, T> tuple3) { return tuple3; }

  public interface AsJust<T, R> extends Function1<T, R> {}
  public interface AsJust2<T1, T2, R> extends Function2<T1, T2, R> {}
  public interface AsJust3<T1, T2, T3, R> extends Function3<T1, T2, T3, R> {}

  public interface AsMany<T, S extends Structable> extends Function1<T, S> {}
  public interface FromArray<T, R> extends Function1<T, R[]> {}
  public interface Just2AsManyTuple2<V1, V2, S extends Structable> extends Function2<V1, V2, S> {}

  public interface JustAsTuple2<V, T extends Tuple> extends Function1<V, T> {}
  public interface Just2AsTuple2<V1, V2, T extends Tuple> extends Function2<V1, V2, T> {}
  public interface JustAsTuple3<V, T extends Tuple> extends Function1<V, T> {}
  public interface Just3AsTuple3<V1, V2, V3, T extends Tuple> extends Function3<V1, V2, V3, T> {}
}
