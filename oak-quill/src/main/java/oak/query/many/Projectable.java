package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.Func;
import oak.union.$2.Union;

public interface Projectable<T> extends Selectable<T> {
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
}
