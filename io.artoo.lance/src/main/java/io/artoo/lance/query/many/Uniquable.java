package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.query.many.Index.index;
import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.type.Nullability.nullable;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index);
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final Pred.Uni<? super T> where) {
    return new First<>(this, nonNullable(where, "where"));
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Pred.Uni<? super T> where) {
    return new Last<>(this, nonNullable(where, "where"));
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Pred.Uni<? super T> where) {
    return new Single<>(this, nonNullable(where, "where"));
  }
}

final class At<T> implements One<T> {
  private final Queryable<T> queryable;
  private final int at;

  @Contract(pure = true)
  At(final Queryable<T> queryable, final int at) {
    this.queryable = queryable;
    this.at = at;
  }

  @SuppressWarnings("unchecked")
  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var found = Cursor.<T>local();

    final var cursor = queryable.cursor();
    try {
      for (var index = index(); cursor.hasNext() && index.value <= at; index.value++) {
        found.set(
          cursor.<T>fetch(next -> index.value == at ? next : null)
        );
      }
    } catch (Throwable throwable) {
      found.grab(throwable);
    }

    return found;
  }
}

final class First<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred.Uni<? super T> where;

  First(final Queryable<T> queryable, final Pred.Uni<? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Cursor<T> cursor() {
    final var first = Cursor.<T>local();

    final var cursor = queryable.cursor();
    try {
      while (cursor.hasNext() && !first.hasNext()) {
        first.set(
          cursor.<T>fetch(next -> where.tryTest(next) ? next : null)
        );
      }
    } catch (Throwable throwable) {
      first.grab(throwable);
    }

    return first;
  }
}

final class Last<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred.Uni<? super T> where;

  Last(final Queryable<T> queryable, final Pred.Uni<? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Cursor<T> cursor() {
    final var last = Cursor.<T>local();

    final var cursor = queryable.cursor();
    try {
      while (cursor.hasNext()) {
        last.set(
          cursor.<T>fetch(next -> where.tryTest(next) ? next : null)
        );
      }
    } catch (Throwable throwable) {
      last.grab(throwable);
    }

    return last;
  }
}

final class Single<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred.Uni<? super T> where;

  Single(final Queryable<T> queryable, final Pred.Uni<? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Cursor<T> cursor() {
    final var single = Cursor.<T>local();

    final var cursor = queryable.cursor();

    try {
      while (cursor.hasNext() && single.size() < 2) {
        single.append(
          cursor.<T>fetch(next ->
            where.tryTest(next)
              ? next
              : null
          )
        );
      }
    } catch (Throwable throwable) {
      single.grab(throwable);
    }

    return single.size() == 1 ? single : single.reset();
  }
}
