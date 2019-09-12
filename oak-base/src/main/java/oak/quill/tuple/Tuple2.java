package oak.quill.tuple;

import oak.func.con.Consumer2;
import oak.func.fun.Function2;
import oak.func.pre.Predicate2;
import oak.quill.single.Nullable;
import org.jetbrains.annotations.Contract;

import static java.util.Objects.requireNonNull;
import static oak.type.Nullability.nonNullable;

public final class Tuple2<V1, V2> implements Projection2<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  Tuple2(final V1 v1, final V2 v2) {
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
  public final <T extends Tuple> T selection(Function2<? super V1, ? super V2, ? extends T> flatMap) {
    return nonNullable(flatMap, "flatMap").apply(v1, v2);
  }
}
