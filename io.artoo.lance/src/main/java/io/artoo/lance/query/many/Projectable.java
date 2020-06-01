package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T extends Record> extends Queryable<T> {
  default <R extends Record> Many<R> select(BiFunction<? super Integer, ? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (i, it) -> {}, select)::iterator;
  }

  default <R extends Record> Many<R> select(Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select((index, value) -> select.apply(value));
  }

  default <R extends Record, M extends Many<R>> Many<R> selectMany(BiFunction<? super Integer, ? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (i, it) -> {}, select)::iterator;
  }

  default <R extends Record, M extends Many<R>> Many<R> selectMany(Function<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (i, it) -> {}, (index, it) -> select.apply(it))::iterator;
  }
}

final class Select<T extends Record, R extends Record> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiFunction<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiFunction<? super Integer, ? super T, ? extends R> select) {
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
      peek.accept(index, next);
      array.add(select.apply(index, next));
    }
    return array.iterator();
  }
}

final class SelectMany<T extends Record, R extends Record, Q extends Queryable<R>> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiFunction<? super Integer, ? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiFunction<? super Integer, ? super T, ? extends Q> select) {
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
      var it = cursor.next();
      if (it != null) {
        peek.accept(index, it);
        final var queried = select.apply(index, it);
        if (queried != null) {
          for (final var selected : queried) {
            if (selected != null)
              array.add(selected);
          }
        }
      }
    }
    return array.iterator();
  }
}

