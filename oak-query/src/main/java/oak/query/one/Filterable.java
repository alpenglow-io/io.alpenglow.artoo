package oak.query.one;

import oak.func.$2.FuncInt;
import oak.func.$2.PredInt;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.internal.Where;
import oak.query.One;

import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {

  static <T> PredInt<T> ith(PredInt<T> whereIth) {
    return whereIth;
  }

  default One<T> where(Pred<? super T> where) {
    nonNullable(where, "where");
    return new Where<>(this, (index, it) -> where.test(it), FuncInt.identity())::iterator;
  }

  default One<T> where(PredInt<? super T> whereIth) {
    nonNullable(whereIth, "whereIth");
    return new Where<>(this, whereIth, FuncInt.identity())::iterator;
  }

  default <R> One<R> ofType(Class<? extends R> type) {
    nonNullable(type, "type");
    return new Where<T, R>(this, ((index, it) -> type.isInstance(it)), (index, it) -> type.cast(it))::iterator;
  }
}
