package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.type.Eitherable;
import io.artoo.lance.type.Sequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<>(this, (i, it) -> {}, select);
  }

  default <R> Many<R> select(Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select((index, value) -> select.apply(value));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, select);
  }

  default <R, M extends Many<R>> Many<R> selectMany(Function<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (index, it) -> select.apply(it));
  }
}

final class Select<T, R> implements Many<R>, Eitherable {
  private final Queryable<T> queryable;
  private final Cons.Bi<? super Integer, ? super T> peek;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final Cons.Bi<? super Integer, ? super T> peek, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var sequence = Sequence.<R>empty();
    var index = 0;
    for (var cursor = queryable.cursor(); cursor.hasNext(); index++) {
      final var idx = index;
      final var next = cursor.next();
      peek.accept(index, next);
      either(
        () -> select.tryApply(idx, next),
        sequence::concat,
        er -> {}
      );
    }
    return sequence.cursor();
  }
}

final class SelectMany<T, R, Q extends Queryable<R>> implements Many<R>, Eitherable {
  private final Queryable<T> queryable;
  private final Func.Bi<? super Integer, ? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final Func.Bi<? super Integer, ? super T, ? extends Q> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var sequence = Sequence.<R>empty();
    var index = 0;
    for (var cursor = queryable.cursor(); cursor.hasNext(); index++) {
      final var idx = index;
/*      cursor.either(element ->
        either(
          () -> select.invoke(idx, element),
          res -> res.eventually(sequence::concat),
          err -> sequence.cought(err)
        )
      );*/
    }
    return sequence.cursor();
  }
}

