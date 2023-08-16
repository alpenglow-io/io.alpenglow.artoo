package re.artoo.lance.query.many;

import re.artoo.lance.experimental.Array;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

import java.util.Comparator;

import static re.artoo.lance.query.many.Order.Arrange.ASCENDING;

@FunctionalInterface
public interface Order<T> extends Many<T> {
  default <R> Order<T> by(TryFunction1<? super T, ? extends R> field) {
    return by(field, ASCENDING);
  }

  default <R> Order<T> by(TryFunction1<? super T, ? extends R> field, Arrange arrange) {
    return by((o1, o2) -> switch (arrange) {
      case Order.Arrange it when it == Arrange.ASCENDING && o1.hashCode() > o2.hashCode() -> 1;
      case Order.Arrange it when it == Arrange.ASCENDING && o1.hashCode() < o2.hashCode() -> -1;
      case Order.Arrange it when it == Arrange.DESCENDING && o1.hashCode() < o2.hashCode() -> 1;
      case Order.Arrange it when it == Arrange.DESCENDING && o1.hashCode() > o2.hashCode() -> -1;
      default -> 0;
    });
  }

  default Order<T> by(Comparator<T> comparator) {
    return () -> cursor()
      .fold(Array.<T>none(), Array::push)
      .map(it -> it.sortBy(comparator))
      .flatMap(Array::cursor);
  }

  enum Arrange {ASCENDING, DESCENDING}
}
