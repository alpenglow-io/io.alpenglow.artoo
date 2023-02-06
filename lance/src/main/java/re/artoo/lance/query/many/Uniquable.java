package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().filter((idx, it) -> idx == index);
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final TryPredicate1<? super T> where) {
    return () -> cursor().filter(where).reduceRight((first, element) -> element != null ? element : first);
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
      .foldLeft(
        null, // single
        false, // single has been found?
        (index, found, single, element) -> (element != null || found),
        (index, found, single, element) -> found && element != null
          ? null
          : !found && element == null
          ? single
          : element
      );
  }
}

