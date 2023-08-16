package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;

public interface Projectable<ELEMENT> extends Queryable<ELEMENT> {
  default <TARGET> One<TARGET> select(final TryFunction1<? super ELEMENT, ? extends TARGET> projection) {
    return () -> cursor().map(projection);
  }

  default <TARGET> One<TARGET> selection(final TryFunction1<? super ELEMENT, ? extends One<TARGET>> projection) {
    return () -> cursor().map(projection).flatMap(Queryable::cursor);
  }

  default <TARGET> Many<TARGET> selectMany(final TryFunction1<? super ELEMENT, ? extends Many<TARGET>> projection) {
    return () -> cursor().map(projection).flatMap(Queryable::cursor);
  }

  default <TARGET> One<TARGET> select(final TrySupplier1<? extends TARGET> projection) {
    return () -> cursor().map(__ -> projection.invoke());
  }

  default One<ELEMENT> each(TryConsumer1<? super ELEMENT> emptyProjection) {
    return () -> cursor().peek(emptyProjection);
  }
}
