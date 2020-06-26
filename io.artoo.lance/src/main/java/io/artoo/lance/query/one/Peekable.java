package io.artoo.lance.query.one;


import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final Cons.Uni<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"));
  }

  default One<T> peek(final Runnable runnable) {
    nonNullable(runnable, "runnable");
    return peek(it -> runnable.run());
  }
}

final class Peek<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> cons;

  public Peek(final Queryable<T> queryable, final Cons.Uni<? super T> cons) {
    this.queryable = queryable;
    this.cons = cons;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    return queryable.cursor().peek(cons);
  }
}

