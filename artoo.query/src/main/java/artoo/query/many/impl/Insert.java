package artoo.query.many.impl;

import org.jetbrains.annotations.NotNull;
import artoo.func.$2.ConsInt;
import artoo.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.copyOf;

// TODO: where and select are missing
public final class Insert<T, Q extends Queryable<T>> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final Q values;

  public Insert(final Queryable<T> queryable, final ConsInt<? super T> peek, final Q values) {
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
      peek.acceptInt(index, it);
      array.add(it);
    }
    for (final var value : values) array.add(value);
    return array.iterator();
  }
}
