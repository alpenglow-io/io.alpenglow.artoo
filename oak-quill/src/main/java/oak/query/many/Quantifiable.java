package oak.query.many;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.func.$2.IntPred;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.func.$2.IntPred.tautology;
import static oak.type.Nullability.nonNullable;

// TODO: replace One<Boolean> with OneBoolean (internal primitive boolean and not boxed-boolean)
public interface Quantifiable<T> extends Queryable<T> {
  default <C> One<Boolean> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Boolean> all(final Pred<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Boolean> all(final IntPred<? super T> where) {
    return new Quantify<>(this, IntCons.nothing(), false, nonNullable(where, "where"));
  }

  default One<Boolean> any() { return this.any(tautology()); }

  default One<Boolean> any(final IntPred<? super T> where) {
    return new Quantify<>(this, IntCons.nothing(), true, nonNullable(where, "where"));
  }
}

final class Quantify<T> implements One<Boolean> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final boolean once;
  private final IntPred<? super T> where;

  @Contract(pure = true)
  Quantify(final Queryable<T> queryable, IntCons<? super T> peek, final boolean once, final IntPred<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.once = once;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<Boolean> iterator() {
    var all = !once;
    var any = once;
    var index = 0;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && (all || !any); index++) {
      final var it = iterator.next();
      peek.acceptInt(index, it);
      all = any = it != null && where.test(index, it);
    }
    return Cursor.of(once || all);
  }
}
