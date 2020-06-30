package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    return new All<>(this, it -> {}, nonNullable(where, "where"));
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    return new Any<>(this, it -> {}, nonNullable(where, "where"));
  }
}

final class Any<T> implements One<Boolean> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Pred.Uni<? super T> where;

  Any(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Pred.Uni<? super T> where) {
    assert queryable != null && peek != null && where != null;
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<Boolean> cursor() {
    var found = false;
    for (var iterator = queryable.iterator(); iterator.hasNext() && !found;) {
      final var it = iterator.next();
      if (it != null) {
        peek.accept(it);

        found = where.test(it);
      }
    }
    return Cursor.local(found);
  }
}

final class All<T> implements One<Boolean> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Pred.Uni<? super T> where;

  All(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Pred.Uni<? super T> where) {
    assert queryable != null && peek != null && where != null;
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<Boolean> cursor() {
    var found = true;
    for (var iterator = queryable.iterator(); iterator.hasNext() && found;) {
      final var it = iterator.next();
      if (it != null) {
        peek.accept(it);

        found = where.test(it);
      }
    }
    return Cursor.local(found);
  }
}
