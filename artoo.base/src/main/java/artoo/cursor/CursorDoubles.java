package artoo.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

final class CursorDoubles implements Cursor<Double> {
  private final double[] doubles;
  private final AtomicInteger index;

  CursorDoubles(final double[] doubles) {
    this(
      doubles,
      new AtomicInteger(0)
    );
  }
  @Contract(pure = true)
  private CursorDoubles(double[] doubles, final AtomicInteger index) {
    this.doubles = doubles;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return doubles.length > 0 && index.get() < doubles.length;
  }

  @Override
  @Nullable
  public final Double next() {
    return doubles.length > 0 ? doubles[index.getAndIncrement()] : null;
  }
}
