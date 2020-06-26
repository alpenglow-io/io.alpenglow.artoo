package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.artoo.lance.func.Pred.not;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred.Uni<? super T> where) {
    return skipWhile((index, it) -> !where.test(it));
  }

  default Many<T> skipWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return new Partition<>(this, it -> {}, not(nonNullable(where, "where")));
  }

  default Many<T> take(final int until) {
    return new Partition<>(this, it -> {}, (index, it) -> index < until);
  }

  default Many<T> takeWhile(final Pred.Uni<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return new Partition<>(this, it -> {}, nonNullable(where, "where"));
  }
}

final class Skip<T> implements Many<T> {

  @Override
  public Cursor<T> cursor() {
    return null;
  }
}

final class Partition<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Pred.Bi<? super Integer, ? super T> where;

  @Contract(pure = true)
  public Partition(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Pred.Bi<? super Integer, ? super T> where) {
    assert queryable != null && peek != null && where != null;
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var result = new ArrayList<T>();
    var index = 0;
    var cursor = queryable.cursor();
    var verified = false;
    if (cursor.hasNext()) {
      do
      {
        final var element = cursor.next();
        if (element != null) {
          peek.accept(element);
          verified = where.test(index, element);
          if (verified)
            result.add(element);
        }
        index++;
      } while (cursor.hasNext() && verified);
    }
    return Cursor.from(result.iterator());
  }
}

