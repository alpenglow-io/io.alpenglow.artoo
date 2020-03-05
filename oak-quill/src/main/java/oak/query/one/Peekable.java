package oak.query.one;

import oak.func.$2.IntCons;
import oak.func.Cons;
import oak.func.Exec;
import oak.query.Queryable;
import oak.query.One;
import oak.query.one.internal.Sneakable;

import static oak.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final IntCons<? super T> peek) {
    return new Sneakable.Sneak<>(this, peek);
  }

  default One<T> peek(final Cons<? super T> peek) {
    nonNullable(peek, "peek");
    return peek((index, it) -> peek.accept(it));
  }

  default One<T> peek(final Exec exec) {
    nonNullable(exec, "exec");
    return peek((index, it) -> exec.execute());
  }
}
