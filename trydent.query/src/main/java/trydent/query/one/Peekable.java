package trydent.query.one;

import trydent.func.$2.ConsInt;
import trydent.func.Cons;
import trydent.func.Exec;
import trydent.query.Queryable;
import trydent.query.One;
import trydent.query.one.internal.Sneakable;

import static trydent.type.Nullability.nonNullable;

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
