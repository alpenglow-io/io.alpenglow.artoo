package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, (i, it) -> {}, Many.from(nonNullable(values, "values")));
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new Insert<>(this, (i, it) -> {}, nonNullable(queryable, "queryable"));
  }
}

// TODO: where and select are missing
final class Insert<T, Q extends Queryable<T>> implements Many<T> {
  private final Queryable<T> queryable;
  private final Cons.Bi<? super Integer, ? super T> peek;
  private final Q values;

  Insert(final Queryable<T> queryable, final Cons.Bi<? super Integer, ? super T> peek, final Q values) {
    this.queryable = queryable;
    this.peek = peek;
    this.values = values;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var array = new ArrayList<T>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.accept(index, it);
      if (it != null)
        array.add(it);
    }
    for (final var value : values)
      array.add(value);
    return Cursor.from(array.iterator());
  }
}
