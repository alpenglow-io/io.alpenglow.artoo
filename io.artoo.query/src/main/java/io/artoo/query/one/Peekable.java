package io.artoo.query.one;



import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.one.impl.Peek;

import java.util.function.Consumer;

import static io.artoo.type.Nullability.nonNullable;

public interface Peekable<T extends Record> extends Queryable<T> {
  default One<T> peek(final Consumer<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"))::iterator;
  }

  default One<T> peek(final Runnable runnable) {
    nonNullable(runnable, "runnable");
    return peek(it -> runnable.run());
  }
}
