package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Many;
import re.artoo.lance.scope.Let;

import static re.artoo.lance.scope.Let.lazy;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final TryPredicate1<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final TryPredicate2<? super Integer, ? super T> where) {
    return () -> cursor()
      .<Keep<T>>reduce(Keep.untilTrue(), (index, meanwhile, element) -> meanwhile.keep() && where.invoke(index, element)
        ? Keep.untilTrue()
        : Keep.untilFalse(element)
      )
      .map(Keep::element);
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final TryPredicate1<? super T> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final TryIntPredicate1<? super T> where) {
    return () -> cursor()
      .<Keep<T>>reduce(Keep.untilTrue(), (index, meanwhile, element) ->
        meanwhile.keep() && where.invoke(index, element)
          ? Keep.untilTrue(element)
          : Keep.untilFalse()
      )
      .map(Keep::element);
  }
}

@SuppressWarnings("unchecked")
record Keep<T>(boolean keep, T element) {
  private static final Let<Keep<Object>> untilFalse = lazy(() -> new Keep<>(false, null));
  private static final Let<Keep<Object>> untilTrue = lazy(() -> new Keep<>(true, null));

  static <T> Keep<T> untilTrue() {
    return (Keep<T>) untilTrue.let(it -> it);
  }

  static <T> Keep<T> untilTrue(T element) {
    return new Keep<>(true, element);
  }

  static <T> Keep<T> untilFalse(T element) {
    return new Keep<>(false, element);
  }

  static <T> Keep<T> untilFalse() {
    return (Keep<T>) untilFalse.let(it -> it);
  }
}
