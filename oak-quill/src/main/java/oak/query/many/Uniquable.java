package oak.query.many;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.Pred.tautology;
import static oak.type.Nullability.*;
import static oak.type.Nullability.nonNullable;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index);
  }

  default One<T> first() {
    return new Unique<>(this, IntCons.nothing(), true, false, tautology());
  }

  default One<T> first(final Pred<? super T> where) {
    return new Unique<>(this, IntCons.nothing(), true, false, nonNullable(where, "where"));
  }

  default One<T> last() {
    return last(tautology());
  }

  default One<T> last(final Pred<? super T> where) {
    return new Unique<>(this, IntCons.nothing(), false, false, nonNullable(where, "where"));
  }

  default One<T> single() {
    return single(tautology());
  }

  default One<T> single(final Pred<? super T> where) {
    return new Unique<>(this, IntCons.nothing(), false, true, nonNullable(where, "where"));
  }
}

final class At<T> implements One<T> {
  private final Queryable<T> queryable;
  private final int index;

  @Contract(pure = true)
  At(final Queryable<T> queryable, final int index) {
    this.queryable = queryable;
    this.index = index;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var count = 0;
    T returned = null;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && count <= index; count++) {
      returned = iterator.next();
    }
    if (count < index)
      returned = null;
    return Cursor.of(returned);
  }
}

final class Unique<T> implements One<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final boolean first;
  private final boolean single;
  private final Pred<? super T> where;

  @Contract(pure = true)
  Unique(final Queryable<T> queryable, final IntCons<? super T> peek, final boolean first, final boolean single, final Pred<? super T> where) {
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
