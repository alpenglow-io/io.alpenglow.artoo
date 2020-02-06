package dev.lug.oak.query.tuple3;

import dev.lug.oak.func.con.Consumer3;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.pre.Predicate3;
import dev.lug.oak.query.One;
import dev.lug.oak.query.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Tuple3<V1, V2, V3> extends Tuple {
  <R> One<R> select(Function3<? super V1, ? super V2, ? super V3, ? extends R> map);
  Tuple3<V1, V2, V3> peek(Consumer3<? super V1, ? super V2, ? super V3> peek);
  <T extends Tuple> One<T> selection(Function3<? super V1, ? super V2, ? super V3, ? extends T> flatMap);
  Tuple3<V1, V2, V3> where(Predicate3<? super V1, ? super V2, ? super V3> filter);
}

final class Default3<V1, V2, V3> extends Triple<V1, V2, V3> implements Tuple3<V1, V2, V3> {
  Default3(V1 v1, V2 v2, V3 v3) {
    super(v1, v2, v3);
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
