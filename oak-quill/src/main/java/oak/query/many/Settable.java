package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.$2.IntCons.nothing;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(tautology());
  }

  default Many<T> distinct(final Pred<? super T> where) {
    return new Distinct<>(this, nothing(), nonNullable(where, "where"));
  }
}

final class Distinct<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final Pred<? super T> where;

  @Contract(pure = true)
  Distinct(final Queryable<T> queryable, final IntCons<? super T> peek, final Pred<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var it = iterator.next();
      peek.applyInt(index, it);
      if (nonNull(it) && where.test(it) && !result.contains(it) || !where.test(it))
        result.add(it);
    }
    return result.iterator();
  }
}
