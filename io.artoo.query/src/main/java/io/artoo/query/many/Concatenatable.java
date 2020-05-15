package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;

import static io.artoo.type.Nullability.nonNullable;

public interface Concatenatable<T extends Record> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, (i, it) -> {}, nonNullable(queryable, "queryable"))::iterator;
  }
}

// TODO: where and select are missing
final class Concat<T extends Record> implements Concatenatable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Queryable<T> others;

  @Contract(pure = true)
  Concat(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Queryable<T> others) {
    this.queryable = queryable;
    this.peek = peek;
    this.others = others;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    var index = 0;
    var cursor = queryable.iterator();
    var hasNext = cursor.hasNext();
    while (hasNext) {
      var value = cursor.next();
      peek.accept(index, value);
      if (value != null)
        array.add(value);
      index++;
      hasNext = cursor.hasNext();
      if (!hasNext) {
        cursor = others.iterator();
        hasNext = cursor.hasNext();
      }
    }
    return array.iterator();
  }
}
