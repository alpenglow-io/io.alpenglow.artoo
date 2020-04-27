package artoo.query.one;

import artoo.func.$2.ConsInt;
import artoo.func.Cons;
import artoo.func.Exec;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.one.impl.Sneakable;

import static artoo.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final ConsInt<? super T> peek) {
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
