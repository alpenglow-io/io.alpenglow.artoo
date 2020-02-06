package dev.lug.oak.query.many3;

import dev.lug.oak.query.Queryable3;
import dev.lug.oak.query.tuple3.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Many3<V1, V2, V3> extends Projectable3<V1, V2, V3>, Filterable3<V1, V2, V3> {

}

final class Many3<V1, V2, V3> implements Queryable3<V1, V2, V3> {
  private final Iterable<Tuple3<V1, V2, V3>> tuples;

  @Contract(pure = true)
  Many3(final Iterable<Tuple3<V1, V2, V3>> tuples) {
    this.tuples = tuples;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<V1, V2, V3>> iterator() {
    return tuples.iterator();
  }
}
