package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.First;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map((idx, it) -> index == idx ? it : null).keepNull();
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
    return () -> cursor().filter(where).reduceLeft((last, element) -> element != null ? element : last);
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final TryPredicate1<? super T> where) {
    record Single<T>(boolean found, T element) {
    }
    return () -> cursor()
      .filter(where)
      .<Single<T>>foldLeft(
        new Single<>(false, null),
        (single, element) -> single.found && element != null
          ? new Single<>(true, null)
          : !single.found && element == null
          ? single
          : new Single<>(true, element)
      )
      .filter(Single::found)
      .map(Single::element);
  }
}

