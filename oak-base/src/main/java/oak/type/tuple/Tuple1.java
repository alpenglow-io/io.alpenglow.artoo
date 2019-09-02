package oak.type.tuple;

import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.pre.Predicate1;

import static java.util.Objects.requireNonNull;

public final class Tuple1<V> implements Tuple {
  private static final String NOT_NULL = "Functional-Interface must be not null";

  private final V value;

  Tuple1(V value) {
    this.value = value;
  }

  public <R> Tuple1<R> map(Function1<? super V, R> f) {
    return Tuple.tuple(requireNonNull(f, NOT_NULL).apply(value));
  }

  public <T extends Tuple> T flatMap(Function1<? super V, ? extends T> f) {
    return requireNonNull(f, NOT_NULL).apply(value);
  }

  public Tuple1<V> peek(Consumer1<? super V> c) {
    requireNonNull(c, NOT_NULL).accept(value);
    return this;
  }

  public boolean matches(Predicate1<? super V> p) {
    return requireNonNull(p, NOT_NULL).test(value);
  }

  public <R> R then(Function1<? super V, R> f) {
    return requireNonNull(f, NOT_NULL).apply(value);
  }
}
