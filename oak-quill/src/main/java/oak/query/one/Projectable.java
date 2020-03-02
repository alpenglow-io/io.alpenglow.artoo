package oak.query.one;

import oak.func.$2.IntFunc;
import oak.func.Func;
import oak.query.internal.Selection;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Sneakable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectOne<T, R, O extends One<R>> extends Func<T, O> {}
  interface SelectIthOne<T, R, O extends One<R>> extends IntFunc<T, O> {}

  static <T, R> Select<T, R> as(Select<T, R> select) {
    return select;
  }

  static <T, R, O extends One<R>> SelectOne<T, R, O> one(SelectOne<T, R, O> selectOne) {
    return selectOne;
  }

  default <R> One<R> select(final Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return () -> new oak.query.internal.Select<T, R>(this, this.sneak(), (index, it) -> select.apply(it)).iterator();
  }

  default <R, O extends One<R>> One<R> select(final SelectOne<? super T, ? extends R, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return () -> new Selection<>(this, this.sneak(), (index, it) -> selectOne.apply(it)).iterator();
  }
}

