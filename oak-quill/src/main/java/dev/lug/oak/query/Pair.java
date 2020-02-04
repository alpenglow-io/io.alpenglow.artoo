package dev.lug.oak.query;

import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.pre.Predicate2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

public abstract class Pair<V1, V2> implements Tuple2<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  protected Pair(final V1 v1, final V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public final <R> One<R> select(final Function2<? super V1, ? super V2, ? extends R> map) {
    return One.of(nonNullable(map, "map").apply(v1, v2));
  }

  @Override
  @Contract("_ -> this")
  public final Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek) {
    nonNullable(peek, "peek").accept(v1, v2);
    return this;
  }

  @Override
  public final <T extends Tuple> One<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap) {
    return One.of(nonNullable(flatMap, "flatMap").apply(v1, v2));
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter) {
    return nonNullable(filter, "filter").test(v1, v2) ? this : new None2<>();
  }

  @Override
  public final void eventually(Consumer2<? super V1, ? super V2> then) {
    nonNullable(then, "then").accept(v1, v2);
  }

  @Override
  @Contract(value = "null -> false", pure = true)
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final var pair = (Pair<?, ?>) o;
    if (!v1.equals(pair.v1)) return false;
    return v2.equals(pair.v2);
  }

  @Override
  public int hashCode() {
    var result = v1.hashCode();
    result = 31 * result + v2.hashCode();
    return result;
  }
}
