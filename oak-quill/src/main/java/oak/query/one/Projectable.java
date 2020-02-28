package oak.query.one;

import oak.cursor.Cursor;
import oak.func.Func;
import oak.query.Queryable;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectOne<T, R, O extends One<R>> extends Func<T, O> {}

  static <T, R> Select<T, R> as(Select<T, R> select) {
    return select;
  }

  static <T, R, O extends One<R>> SelectOne<T, R, O> one(SelectOne<T, R, O> selectOne) {
    return selectOne;
  }

  default <R> One<R> select(final Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return () -> {
      final var value = this.iterator().next();
      return value != null ? Cursor.of(select.apply(value)) : Cursor.none();
    };
  }

  default <R, O extends One<R>> One<R> select(final SelectOne<? super T, ? extends R, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return () -> select(as(selectOne::apply)).iterator().next().iterator();
  }
}

