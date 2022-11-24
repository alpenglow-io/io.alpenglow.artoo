package lance.query.many;

import lance.func.Pred;
import lance.query.One;
import lance.Queryable;
import lance.query.func.At;
import lance.query.func.First;
import lance.query.func.Last;
import lance.query.func.Single;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map(new At<>(index)).keepNull();
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new First<>(where)).skipNull();
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new Last<>(where)).skipNull();
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new Single<>(where)).keepNull();
  }
}

