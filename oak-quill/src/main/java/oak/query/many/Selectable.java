package oak.query.many;

import oak.query.Queryable;

import static oak.func.$2.IntCons.nothing;
import static oak.type.Nullability.nonNullable;

interface Selectable<T> extends Queryable<T> {
  default <R> oak.query.Many<R> select(Projectable.SelectIth<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<>(this, nothing(), select);
  }

  default <R> oak.query.Many<R> select(Projectable.Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<>(this, nothing(), (index, value) -> select.apply(value));
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(Projectable.SelectIthMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return new Selection<>(new Select<>(this, nothing(), select), nothing());
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(Projectable.SelectMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return new Selection<>(new Select<>(this, nothing(), (index, value) -> select.apply(value)), nothing());
  }
}
