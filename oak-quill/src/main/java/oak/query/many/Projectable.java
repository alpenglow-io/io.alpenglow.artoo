package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.func.Func;
import oak.query.Queryable;
import oak.union.$2.Union;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.$2.IntCons.nothing;
import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectIth<T, R> extends IntFunc<T, R> {}
  interface SelectMany<T, R, M extends oak.query.Many<R>> extends Func<T, M> {}
  interface SelectIthMany<T, R, M extends oak.query.Many<R>> extends IntFunc<T, M> {}
  interface SelectUnion2<T, R1, R2> extends oak.func.Func<T, Union<R1, R2>> {}
  interface SelectUnion3<T, R1, R2, R3> extends oak.func.Func<T, oak.union.$3.Union<R1, R2, R3>> {}

  static <T, R> SelectIth<T, R> ith(final SelectIth<T, R> selectIth) {
    return selectIth;
  }

  static <T, R, M extends oak.query.Many<R>> SelectIthMany<T, R, M> ithMany(final SelectIthMany<T, R, M> selectIthMany) {
    return selectIthMany;
  }

  default <R> oak.query.Many<R> select(SelectIth<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Project<>(this, nothing(), select);
  }

  default <R> oak.query.Many<R> select(Projectable.Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Project<>(this, nothing(), (index, value) -> select.apply(value));
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(Projectable.SelectIthMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return new Projection<>(new Project<>(this, nothing(), select), nothing());
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(Projectable.SelectMany<? super T, ? extends R, M> select) {
    nonNullable(select, "select");
    return new Projection<>(new Project<>(this, nothing(), (index, value) -> select.apply(value)), nothing());
  }

}

final class Project<T, R> implements oak.query.Many<R> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntFunc<? super T, ? extends R> select;

  Project(final Queryable<T> queryable, final IntCons<? super T> peek, final IntFunc<? super T, ? extends R> select) {
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
      peek.acceptInt(index, next);
      array.add(select.applyInt(index, next));
    }
    return array.iterator();
  }
}

final class Projection<R, M extends oak.query.Many<R>> implements oak.query.Many<R> {
  private final Queryable<M> selected;
  private final IntCons<? super R> peek;

  Projection(final Queryable<M> selected, final IntCons<? super R> peek) {
    this.selected = selected;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    for (final var many : selected) {
      var index = 0;
      for (var cursor = many.iterator(); cursor.hasNext(); index++) {
        var it = cursor.next();
        peek.acceptInt(index, it);
        array.add(it);
      }
    }
    return array.iterator();
  }
}
