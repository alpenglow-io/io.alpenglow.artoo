package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;

import static io.artoo.lance.func.Pred.not;
import static io.artoo.lance.query.many.Index.index;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred.Uni<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return new Skip<>(this, nonNullable(where, "where"));
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final Pred.Uni<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return new Take<>(this, nonNullable(where, "where"));
  }
}

final class Take<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Pred.Bi<? super Integer, ? super T> where;

  Take(final Queryable<T> queryable, final Pred.Bi<? super Integer, ? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }

  @Override
  public Cursor<T> cursor() {
    final var taken = Cursor.<T>local();

    final var cursor = queryable.cursor();
    var keepTaking = true;
    try {
      for (var index = index(); cursor.hasNext() && keepTaking; index.value++) {
        final var verified = cursor.next(it -> where.tryTest(index.value, it) ? it : null);
        taken.append(verified);
        keepTaking = verified != null;
      }
    } catch (Throwable cause) {
      taken.grab(cause);
    }

    return taken;
  }
}

final class Skip<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Pred.Bi<? super Integer, ? super T> where;

  Skip(final Queryable<T> queryable, final Pred.Bi<? super Integer, ? super T> where) {
    assert queryable != null && where != null;
    this.queryable = queryable;
    this.where = where;
  }

  @Override
  public Cursor<T> cursor() {
    final var skipped = Cursor.<T>local();

    final var cursor = queryable.cursor();
    try {
      for (var index = index(); cursor.hasNext(); index.value++) {
        skipped.append(
          cursor.next(it ->
            where.tryTest(index.value, it) && skipped.size() == 0
              ? null
              : it
          )
        );
      }
    } catch (Throwable cause) {
      skipped.grab(cause);
    }

    return skipped;
  }
}

