package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Every;
import re.artoo.lance.query.closure.None;
import re.artoo.lance.query.closure.Some;

import java.util.Objects;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(Class<R> type) {
    return () -> cursor().map((index, it) -> type.isInstance(it)).reduce(true, (isIt, element) -> isIt && element);
  }
  default One<Boolean> every(TryPredicate1<? super T> where) {
    return () -> cursor().map((index, it) -> where.invoke(it)).reduce(true, (isIt, element) -> isIt && element);
  }
  default One<Boolean> every(TryIntPredicate1<? super T> where) {
    return () -> cursor().map(where::invoke).reduce(true, (isIt, element) -> isIt && element);
  }
  default One<Boolean> none() {
    return none(Objects::isNull);
  }
  default <R> One<Boolean> none(Class<R> type) {
    return none((index, it) -> !type.isInstance(it));
  }
  default One<Boolean> none(TryPredicate1<? super T> where) {
    return none((index, it) -> where.invoke(it));
  }
  default One<Boolean> none(TryIntPredicate1<? super T> where) {
    return () -> cursor().map(where::invoke).reduce(true, (isIt, element) -> isIt && !element);
  }
  default One<Boolean> some() { return some(Objects::nonNull); }
  default <R> One<Boolean> some(Class<R> type) { return some(type::isInstance); }
  default One<Boolean> some(TryPredicate1<? super T> where) {
    return some((index, it) -> where.invoke(it));
  }
  default One<Boolean> some(TryIntPredicate1<? super T> where) {
    return () -> cursor().map(where::invoke).reduce(true, (isIt, element) -> isIt || element);
  }
}


