package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.func.$2.IntPred;
import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.$2.IntFunc.identity;
import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.apply(param));
  }

  default Many<T> where(final IntPred<? super T> where) {
    return where(where, identity());
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    nonNullable(type, "type");
    return where((index, it) -> type.isInstance(it), (index, it) -> type.cast(it));
  }

  default <R> Many<R> where(final IntPred<? super T> where, final IntFunc<? super T, ? extends R> select) {
    return new Where<>(this, IntCons.nothing(), nonNullable(where, "where"), nonNullable(select, "select"));
  }
}

final class Where<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntPred<? super T> where;
  private final IntFunc<? super T, ? extends R> select;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final IntCons<? super T> peek, final IntPred<? super T> where, final IntFunc<? super T, ? extends R> select) {
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
