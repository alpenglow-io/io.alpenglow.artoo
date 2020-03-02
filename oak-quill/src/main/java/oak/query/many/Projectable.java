package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.internal.Selection;
import oak.union.$2.Union;

import static oak.func.$2.IntCons.nothing;
import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectIth<T, R> extends IntFunc<T, R> {}
  interface SelectMany<T, R, M extends Many<R>> extends Func<T, M> {}
  interface SelectIthMany<T, R, M extends Many<R>> extends IntFunc<T, M> {}
  interface SelectUnion2<T, R1, R2> extends oak.func.Func<T, Union<R1, R2>> {}
  interface SelectUnion3<T, R1, R2, R3> extends oak.func.Func<T, oak.union.$3.Union<R1, R2, R3>> {}

  static <T, R> SelectIth<T, R> ith(final SelectIth<T, R> selectIth) {
    return selectIth;
  }

  static <T, R, M extends Many<R>> SelectIthMany<T, R, M> ithMany(final SelectIthMany<T, R, M> selectIthMany) {
    return selectIthMany;
  }

  default <R> Many<R> select(SelectIth<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return () -> new oak.query.internal.Select<T, R>(this, nothing(), select).iterator();
  }

  default <R> Many<R> select(Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return () -> new oak.query.internal.Select<T, R>(this, nothing(), (index, value) -> select.apply(value)).iterator();
  }

  default <R, M extends Many<R>> Many<R> select(SelectIthMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return () -> new Selection<>(this, nothing(), select).iterator();
  }

  default <R, M extends Many<R>> Many<R> select(SelectMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return () -> new Selection<>(this, nothing(), (index, value) -> select.apply(value)).iterator();
  }

}

