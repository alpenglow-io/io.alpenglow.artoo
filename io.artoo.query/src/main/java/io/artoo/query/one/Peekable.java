package io.artoo.query.one;

import io.artoo.func.Cons;
import io.artoo.func.Exec;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.one.impl.Peek;

import static io.artoo.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final Cons<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"))::iterator;
  }

  default One<T> peek(final Exec exec) {
    nonNullable(exec, "exec");
    return peek(it -> exec.execute());
  }
}
