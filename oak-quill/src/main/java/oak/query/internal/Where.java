package oak.query.internal;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.func.$2.IntPred;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Where<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntPred<? super T> where;
  private final IntFunc<? super T, ? extends R> select;

  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final IntCons<? super T> peek, final IntPred<? super T> where, final IntFunc<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var result = new ArrayList<R>();
    var index = 0;
    for (final var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var it = cursor.next();
      peek.acceptInt(index, it);
      if (it != null && where.verify(index, it)) {
        result.add(select.applyInt(index, it));
      }
    }
    return result.iterator();
  }
}
