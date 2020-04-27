package artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$2.ConsInt;
import artoo.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: where and select are missing
public final class Concat<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final Queryable<T> others;

  @Contract(pure = true)
  public Concat(final Queryable<T> queryable, final ConsInt<? super T> peek, final Queryable<T> others) {
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
