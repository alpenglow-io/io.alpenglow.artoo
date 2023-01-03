package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Every;
import re.artoo.lance.query.closure.None;
import re.artoo.lance.query.closure.Some;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(final Class<R> type) {
    return every(type::isInstance);
  }

  default One<Boolean> every(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Every<>(where)).keepNull();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new None<>(where)).keepNull();
  }

  default One<Boolean> some() { return this.some(t -> true); }

  default One<Boolean> some(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Some<>(where)).keepNull().or(() -> Cursor.open(false));
  }
}


