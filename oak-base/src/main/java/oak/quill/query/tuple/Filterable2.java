package oak.quill.query.tuple;

import oak.collect.Many;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.query.Filterable;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Filterable2<V1, V2> extends Filterable<Tuple2<V1, V2>> {
  default Queryable2<V1, V2> where(final Predicate2<? super V1, ? super V2> filter) {
    return new Where2<>(this, nonNullable(filter, "filter"));
  }
}

final class Where2<V1, V2> implements Queryable2<V1, V2> {

  private final Structable<Tuple2<V1, V2>> structable;
  private final Predicate2<? super V1, ? super V2> filter;

  @Contract(pure = true)
  Where2(final Structable<Tuple2<V1, V2>> structable, final Predicate2<? super V1, ? super V2> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    final var many = Many.<Tuple2<V1, V2>>of();
    for (final var tuple2 : structable) {
      tuple2
        .where(filter)
        .peek((v1, v2) -> many.add(Tuple.of(v1, v2)));
    }
    return many.iterator();
  }
}
