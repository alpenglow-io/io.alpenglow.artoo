package oak.type.tuple;

import oak.func.con.Consumer2;
import oak.func.fun.Function2;
import oak.func.pre.Predicate2;

import static java.util.Objects.requireNonNull;

public final class Tuple2<V1, V2> implements Tuple {
  private final V1 v1;
  private final V2 v2;

  Tuple2(V1 v1, V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  public <R> Tuple1<R> map(Function2<? super V1, ? super V2, ? extends R> f) {
    return Tuple.of(requireNonNull(f, "Mapping must be not null").apply(v1, v2));
  }

  public <T extends Tuple> T flatMap(Function2<? super V1, ? super V2, ? extends T> f) {
    return requireNonNull(f, "Flat-Mapping must be not null").apply(v1, v2);
  }

  public Tuple2<? super V1, ? super V2> peek(Consumer2<? super V1, ? super V2> c) {
    requireNonNull(c, "Peeking must be not null").accept(v1, v2);
    return this;
  }

  public boolean matches(Predicate2<? super V1, ? super V2> p) {
    return requireNonNull(p, "Condition must be not null").test(v1, v2);
  }

  public <R> R then(Function2<? super V1, ? super V2, ? extends R> f) {
    return requireNonNull(f, "Then must be not null").apply(v1, v2);
  }
}
