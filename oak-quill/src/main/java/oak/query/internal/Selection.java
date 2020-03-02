package oak.query.internal;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Selection<T, R, Q extends Queryable<R>> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntFunc<? super T, ? extends Q> select;

  public Selection(final Queryable<T> queryable, final IntCons<? super T> peek, final IntFunc<? super T, ? extends Q> select) {
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
      var it = cursor.next();
      peek.applyInt(index, it);
      if (it != null) {
        final var queried = select.applyInt(index, it);
        if (queried != null) {
          for (final var selected : queried) {
            if (selected != null) array.add(selected);
          }
        }
      }
    }
    return array.iterator();
  }
}
