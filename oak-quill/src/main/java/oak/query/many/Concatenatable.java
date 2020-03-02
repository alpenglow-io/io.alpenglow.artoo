package oak.query.many;

import oak.func.$2.IntCons;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, IntCons.nothing(), nonNullable(queryable, "queryable"));
  }
}

// TODO: where and select are missing
final class Concat<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final Queryable<T> others;

  @Contract(pure = true)
  Concat(final Queryable<T> queryable, final IntCons<? super T> peek, final Queryable<T> others) {
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
      peek.acceptInt(index, value);
      if (value != null) array.add(value);
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
