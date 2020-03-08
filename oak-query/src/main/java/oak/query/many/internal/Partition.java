package oak.query.many.internal;

import oak.func.$2.ConsInt;
import oak.func.$2.PredInt;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Partition<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final PredInt<? super T> where;

  @Contract(pure = true)
  public Partition(final Queryable<T> queryable, final ConsInt<? super T> peek, final PredInt<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    var cursor = queryable.iterator();
    var verified = false;
    if (cursor.hasNext()) {
      do
      {
        final var it = cursor.next();
        peek.applyInt(index, it);
        verified = where.verify(index, it);
        if (it != null && verified) {
          result.add(it);
        }
        index++;
      } while (cursor.hasNext() && verified);
    }
    return result.iterator();
  }
}
