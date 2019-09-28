package oak.quill.tuple;

import oak.func.con.Consumer2;
import oak.func.fun.Function2;
import oak.func.pre.Predicate2;
import oak.quill.single.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static oak.type.Nullability.nonNullable;
import static oak.type.Nullability.nonNullableState;

public interface Tuple2<V1, V2> extends Tuple {
  <R> Nullable<R> select(Function2<? super V1, ? super V2, ? extends R> map);
  Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek);
  <T extends Tuple> Nullable<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap);
  Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter);
}

final class Pair<V1, V2> implements Tuple2<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  Pair(final V1 v1, final V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public final <R> Nullable<R> select(final Function2<? super V1, ? super V2, ? extends R> map) {
    return Nullable.of(nonNullable(map, "map").apply(v1, v2));
  }

  @Override
  @Contract("_ -> this")
  public final Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek) {
    nonNullable(peek, "peek").accept(v1, v2);
    return this;
  }

  @Override
  public final <T extends Tuple> Nullable<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap) {
    return Nullable.of(nonNullable(flatMap, "flatMap").apply(v1, v2));
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter) {
    return nonNullable(filter, "filter").test(v1, v2) ? this : new Empty2<>();
  }
}

final class Empty2<V1, V2> implements Tuple2<V1, V2> {
  @Contract(value = "_ -> this", pure = true)
  @Override
  public final Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter) {
    return this;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <R> Nullable<R> select(Function2<? super V1, ? super V2, ? extends R> map) {
    return Nullable.none();
  }

  @Contract("_ -> this")
  @Override
  public final Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek) {
    return this;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <T extends Tuple> Nullable<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap) {
    return Nullable.none();
  }
}
