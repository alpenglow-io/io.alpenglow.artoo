package oak.query.internal;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Select<T, R> implements oak.query.Queryable<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntFunc<? super T, ? extends R> select;

  public Select(final Queryable<T> queryable, final IntCons<? super T> peek, final IntFunc<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var next = cursor.next();
      peek.acceptInt(index, next);
      array.add(select.applyInt(index, next));
    }
    return array.iterator();
  }
}
