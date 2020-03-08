package oak.query.many.internal;

import oak.cursor.Cursor;
import oak.func.$2.ConsInt;
import oak.func.Pred;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Unique<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final boolean first;
  private final boolean single;
  private final Pred<? super T> where;

  @Contract(pure = true)
  public Unique(final Queryable<T> queryable, final ConsInt<? super T> peek, final boolean first, final boolean single, final Pred<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.first = first;
    this.single = single;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    var done = false;
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext() && (!first || result == null) && !done; index++) {
      var it = cursor.next();
      peek.acceptInt(index, it);
      if (it != null && where.test(it)) {
        done = single && result != null;
        result = !single || result == null ? it : null;
      }
    }
    return Cursor.of(result);
  }
}
