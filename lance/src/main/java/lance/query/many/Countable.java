package lance.query.many;

import lance.func.Pred;
import lance.literator.Cursor;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Count;

public interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return count(it -> true);
  }

  default One<Integer> count(final Pred.TryPredicate<? super T> where) {
    return () -> cursor().map(new Count<>(where)).or(() -> Cursor.open(0)).keepNull();
  }
}

