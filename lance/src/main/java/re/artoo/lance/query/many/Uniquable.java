package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Closure;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.First;
import re.artoo.lance.query.closure.Last;
import re.artoo.lance.query.closure.Single;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map(Closure.at(index)).keepNull();
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new First<>(where)).skipNull();
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Last<>(where)).skipNull();
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Single<>(where)).keepNull();
  }
}

