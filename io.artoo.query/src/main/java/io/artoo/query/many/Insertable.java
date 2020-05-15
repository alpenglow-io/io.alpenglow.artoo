package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;

import static io.artoo.type.Nullability.nonNullable;

public interface Insertable<T extends Record> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, (i, it) -> {}, Many.from(nonNullable(values, "values")))::iterator;
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new Insert<>(this, (i, it) -> {}, nonNullable(queryable, "queryable"))::iterator;
  }
}

// TODO: where and select are missing
final class Insert<T extends Record, Q extends Queryable<T>> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Q values;

  Insert(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Q values) {
    this.queryable = queryable;
    this.peek = peek;
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
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
    return array.iterator();
  }
}
