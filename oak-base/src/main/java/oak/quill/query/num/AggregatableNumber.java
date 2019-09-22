package oak.quill.query.num;

import oak.func.fun.Function2;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public interface AggregatableNumber<N extends Number> extends StructableNumber<N> {
  default Single<N> max() {
    return new MinMax<>(this, Math::max);
  }

  default Single<N> min() {
    return new MinMax<>(this, Math::min);
  }
}

final class MinMax<N extends Number> implements Single<N> {
  private final StructableNumber<N> structable;
  private final Function2<Double, Double, Double> operation;

  @Contract(pure = true)
  MinMax(final StructableNumber<N> structable, final Function2<Double, Double, Double> operation) {
    this.structable = structable;
    this.operation = operation;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final N get() {
    N result = null;
    for (final var value : structable) {
      result = isNull(result)
        ? value
        : isNull(value)
        ? result
        : operation.apply(result.doubleValue(), value.doubleValue()) == result.doubleValue()
        ? result
        : value;
    }
    if (isNull(result)) throw new IllegalStateException("Query can't be solved, since Queryable is empty.");
    return result;
  }
}
