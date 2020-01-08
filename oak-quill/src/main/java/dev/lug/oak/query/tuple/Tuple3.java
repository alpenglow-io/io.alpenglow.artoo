package dev.lug.oak.query.tuple;

import dev.lug.oak.func.con.Consumer3;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.pre.Predicate3;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.*;

public interface Tuple3<V1, V2, V3> extends Tuple {
  <R> One<R> select(Function3<? super V1, ? super V2, ? super V3, ? extends R> map);
  Tuple3<V1, V2, V3> peek(Consumer3<? super V1, ? super V2, ? super V3> peek);
  <T extends Tuple> One<T> selection(Function3<? super V1, ? super V2, ? super V3, ? extends T> flatMap);
  Tuple3<V1, V2, V3> where(Predicate3<? super V1, ? super V2, ? super V3> filter);

  abstract class Triple<V1, V2, V3> implements Tuple3<V1, V2, V3> {
    private final V1 v1;
    private final V2 v2;
    private final V3 v3;

    protected Triple(final V1 v1, final V2 v2, final V3 v3) {
      this.v1 = v1;
      this.v2 = v2;
      this.v3 = v3;
    }
  }
}

final class Default3<V1, V2, V3> implements Tuple3<V1, V2, V3> {
  private final V1 v1;
  private final V2 v2;
  private final V3 v3;

  @Contract(pure = true)
  Default3(V1 v1, V2 v2, V3 v3) {
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
  }

  @Override
  public final <R> One<R> select(Function3<? super V1, ? super V2, ? super V3, ? extends R> map) {
    return One.of(nonNullable(map, "map").apply(v1, v2, v3));
  }

  @Override
  @Contract("_ -> this")
  public final Tuple3<V1, V2, V3> peek(Consumer3<? super V1, ? super V2, ? super V3> peek) {
    nonNullable(peek, "peek").accept(v1, v2, v3);
    return this;
  }

  @Override
  public final <T extends Tuple> One<T> selection(Function3<? super V1, ? super V2, ? super V3, ? extends T> flatMap) {
    return One.of(nonNullable(flatMap, "flatMap").apply(v1, v2, v3));
  }

  @Override
  public Tuple3<V1, V2, V3> where(Predicate3<? super V1, ? super V2, ? super V3> filter) {
    return nonNullable(filter, "filter").test(v1, v2, v3) ? this : new None3<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var triple = (Default3<?, ?, ?>) o;
    if (!v1.equals(triple.v1)) return false;
    if (!v2.equals(triple.v2)) return false;
    return v3.equals(triple.v3);
  }

  @Override
  public int hashCode() {
    var result = v1.hashCode();
    result = 31 * result + v2.hashCode();
    result = 31 * result + v3.hashCode();
    return result;
  }
}

final class None3<V1, V2, V3> implements Tuple3<V1, V2, V3> {
  @NotNull
  @Contract(pure = true)
  @Override
  public final  <R> One<R> select(Function3<? super V1, ? super V2, ? super V3, ? extends R> map) {
    return One.none();
  }

  @Contract(value = "_ -> this", pure = true)
  @Override
  public final Tuple3<V1, V2, V3> peek(Consumer3<? super V1, ? super V2, ? super V3> peek) {
    return this;
  }

  @Override
  public final  <T extends Tuple> One<T> selection(Function3<? super V1, ? super V2, ? super V3, ? extends T> flatMap) {
    return One.none();
  }

  @Override
  public final Tuple3<V1, V2, V3> where(Predicate3<? super V1, ? super V2, ? super V3> filter) {
    return new None3<>();
  }
}
