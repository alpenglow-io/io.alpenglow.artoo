package dev.lug.oak.query;

import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.pre.Predicate2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Tuple2<V1, V2> extends Tuple {
  <R> One<R> select(Function2<? super V1, ? super V2, ? extends R> map);
  Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek);
  <T extends Tuple> One<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap);
  Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter);
  void eventually(Consumer2<? super V1, ? super V2> then);
}

final class Default2<V1, V2> extends Pair<V1, V2> implements Tuple2<V1, V2> {
  protected Default2(V1 v1, V2 v2) {
    super(v1, v2);
  }
}

final class None2<V1, V2> implements Tuple2<V1, V2> {
  @Contract(value = "_ -> this", pure = true)
  @Override
  public final Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter) {
    return this;
  }

  @Override
  public final void eventually(Consumer2<? super V1, ? super V2> then) {
    // nothing
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <R> One<R> select(Function2<? super V1, ? super V2, ? extends R> map) {
    return One.none();
  }

  @Contract("_ -> this")
  @Override
  public final Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek) {
    return this;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final <T extends Tuple> One<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap) {
    return One.none();
  }
}

