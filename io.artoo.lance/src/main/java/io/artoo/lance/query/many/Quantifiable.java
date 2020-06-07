package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.value.Bool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.value.Bool.False;
import static io.artoo.lance.value.Bool.True;

public interface Quantifiable<T extends Record> extends Queryable<T> {
  default <C> One<Bool> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Bool> all(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Bool> all(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, Bool.False, nonNullable(where, "where"))::iterator;
  }

  default One<Bool> any() { return this.any((i, t) -> true); }

  default One<Bool> any(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, Bool.True, nonNullable(where, "where"))::iterator;
  }

  default One<Bool> any(final Predicate<? super T> where) {
    final var w = nonNullable(where, "where");
    return any((index, item) -> w.test(item));
  }
}

final class Quantify<T extends Record> implements Quantifiable<Bool> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Bool once;
  private final BiPredicate<? super Integer, ? super T> where;

  @Contract(pure = true)
  Quantify(final Queryable<T> queryable, BiConsumer<? super Integer, ? super T> peek, final Bool once, final BiPredicate<? super Integer, ? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.once = once;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<Bool> iterator() {
    var all = once.not();
    var any = once;
    var index = 0;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && (all.equals(True) || any.equals(False)); index++) {
      final var it = iterator.next();
      peek.accept(index, it);
      all = any = new Bool(it != null && where.test(index, it));
    }
    return Cursor.of(new Bool(once.equals(True) || all.equals(True)));
  }
}

