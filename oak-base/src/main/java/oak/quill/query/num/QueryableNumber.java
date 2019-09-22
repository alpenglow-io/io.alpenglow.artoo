package oak.quill.query.num;

import oak.collect.Many;
import oak.quill.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface QueryableNumber<N extends Number> extends Queryable<N>, AggregatableNumber<N> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <V extends Number> QueryableNumber<V> from(final V... numbers) {
    return new Query<>(Many.of(numbers));
  }
}

final class Query<N extends Number> implements QueryableNumber<N> {
  private final Many<N> numbers;

  @Contract(pure = true)
  Query(final Many<N> numbers) {this.numbers = numbers;}

  @NotNull
  @Override
  public final Iterator<N> iterator() {
    return numbers.iterator();
  }
}
