package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... elements) {
    return new Insert<>(this, Many.from(nonNullable(elements, "elements")));
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new Insert<>(this, nonNullable(queryable, "queryable"));
  }
}

final class Insert<T, Q extends Queryable<T>> implements Many<T> {
  private final Queryable<T> queryable;
  private final Q elements;

  Insert(final Queryable<T> queryable, final Q elements) {
    assert queryable != null && elements != null;
    this.queryable = queryable;
    this.elements = elements;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var cursor = queryable.cursor();

    for (final var element : elements) {
      cursor.append(element);
    }

    return cursor;
  }
}
