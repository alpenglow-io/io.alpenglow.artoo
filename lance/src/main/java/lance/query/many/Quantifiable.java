package lance.query.many;

import lance.func.Pred;
import lance.literator.Cursor;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Every;
import lance.query.func.Some;
import lance.query.func.None;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(final Class<R> type) {
    return every(type::isInstance);
  }

  default One<Boolean> every(final Pred.TryPredicate<? super T> where) {
    return () -> cursor().map(new Every<>(where)).keepNull();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final Pred.TryPredicate<? super T> where) {
    return () -> cursor().map(new None<>(where)).keepNull();
  }

  default One<Boolean> some() { return this.some(t -> true); }

  default One<Boolean> some(final Pred.TryPredicate<? super T> where) {
    return () -> cursor().map(new Some<>(where)).keepNull().or(() -> Cursor.open(false));
  }
}


