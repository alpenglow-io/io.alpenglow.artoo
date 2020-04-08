package trydent.query.internal;

import trydent.func.$2.ConsInt;
import trydent.func.$2.FuncInt;
import trydent.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Select<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final FuncInt<? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final FuncInt<? super T, ? extends R> select) {
    this(queryable, null, select);
  }
  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final ConsInt<? super T> peek, final FuncInt<? super T, ? extends R> select) {
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
