package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().filter((idx, it) -> idx == index);
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final TryPredicate1<? super T> where) {
    return () -> cursor()
      .filter(where)
      .reduce((index, first, element) -> index == 0 && element != null ? element : null);
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final TryPredicate1<? super T> where) {
    return () -> cursor().filter(where).reduce((last, element) -> element != null ? element : last);
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final TryPredicate1<? super T> where) {
    return () -> cursor()
      .filter(where)
      .peek(System.out::println)
      .<Pair<Boolean, T>>fold(Tuple.of(false, null), (index, single, element) ->
        !single.first() && element != null
          ? single.both(true, element)
          : single.first() && element != null
          ? single.letSecond(null)
          : single
      )
      .map(Pair::second);
  }
}

