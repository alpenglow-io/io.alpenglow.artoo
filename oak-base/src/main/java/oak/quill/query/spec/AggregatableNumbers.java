package oak.quill.query.spec;

import oak.func.fun.Function2;
import oak.func.fun.IntFunction2;
import oak.func.sup.Supplier1;
import oak.quill.Structable;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static java.util.Objects.isNull;

@SuppressWarnings("unchecked")
public interface AggregatableNumbers<N extends Number> extends StructableNumber<N> {
  default Single<Double> average() {
    return switch (NumberType.valueOf(this.get().getSimpleName())) {
      case Double -> new NumbersAverage<>((StructableNumber<Double>) this, 0.0, Double::sum, (count, total) -> total / count);
      case Integer -> new NumbersAverage<>((StructableNumber<Integer>) this, 0, Integer::sum, (count, total) -> ((double) total) / count);
      case Long -> new NumbersAverage<>((StructableNumber<Long>) this, 0L, Long::sum, (count, total) -> (double) (total / count));
      case BigInteger -> new NumbersAverage<>((StructableNumber<BigInteger>) this, BigInteger.ZERO, BigInteger::add, (count, total) -> total.doubleValue() / count);
      case BigDecimal -> new NumbersAverage<>((StructableNumber<BigDecimal>) this, BigDecimal.ZERO, BigDecimal::add, (count, total) -> total.doubleValue() / count);
    };
  }
}

enum NumberType {
  Double,
  Integer,
  Long,
  BigInteger,
  BigDecimal
}

final class NumbersAverage<N extends Number, R extends Number> implements Single<R> {
  private final StructableNumber<N> structable;
  private final N seed;
  private final Function2<? super N, ? super N, ? extends N> add;
  private final IntFunction2<? super N, ? extends R> divide;

  @Contract(pure = true)
  NumbersAverage(
    final StructableNumber<N> structable,
    final N seed,
    final Function2<? super N, ? super N, ? extends N> add,
    final IntFunction2<? super N, ? extends R> divide
  ) {
    this.structable = structable;
    this.seed = seed;
    this.add = add;
    this.divide = divide;
  }

  @Override
  @NotNull
  public final R get() {
    var total = seed;
    var count = 0;
    for (final var cursor = structable.iterator(); cursor.hasNext(); count++) {
      final var next = cursor.next();
      total = isNull(next) ? total : add.apply(total, next);
    }
    if (count == 0) throw new IllegalStateException("Query can't be satisfied, Queryable is empty.");
    return divide.apply(count, total);
  }
}

