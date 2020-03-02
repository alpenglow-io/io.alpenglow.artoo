package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.$2.IntPred;
import oak.func.Pred;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.$2.IntPred.not;
import static oak.type.Nullability.nonNullable;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred<? super T> where) {
    return skipWhile((index, it) -> !where.apply(it));
  }

  default Many<T> skipWhile(final IntPred<? super T> where) {
    return new Partition<>(this, IntCons.nothing(), not(nonNullable(where, "where")));
  }

  default Many<T> take(final int until) {
    return new Partition<>(this, IntCons.nothing(), (index, it) -> index < until);
  }

  default Many<T> takeWhile(final Pred<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final IntPred<? super T> where) {
    return new Partition<>(this, IntCons.nothing(), nonNullable(where, "where"));
  }
}

final class Partition<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntPred<? super T> where;

  @Contract(pure = true)
  Partition(final Queryable<T> queryable, final IntCons<? super T> peek, final IntPred<? super T> where) {
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
