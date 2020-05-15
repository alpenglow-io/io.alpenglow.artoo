package io.artoor.query.one;



import io.artoor.query.One;
import io.artoor.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

import static io.artoor.type.Nullability.nonNullable;

public interface Peekable<T extends Record> extends Queryable<T> {
  default One<T> peek(final Consumer<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"))::iterator;
  }

  default One<T> peek(final Runnable runnable) {
    nonNullable(runnable, "runnable");
    return peek(it -> runnable.run());
  }
}

final class Peek<T extends Record> implements Peekable<T> {
  private final Queryable<T> queryable;
  private final Consumer<? super T> cons;

  public Peek(final Queryable<T> queryable, final Consumer<? super T> cons) {
    this.queryable = queryable;
    this.cons = cons;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.cursor();
    if (cursor.hasNext()) cons.accept(cursor.next());
    cursor.resume();
    return cursor;
  }
}

