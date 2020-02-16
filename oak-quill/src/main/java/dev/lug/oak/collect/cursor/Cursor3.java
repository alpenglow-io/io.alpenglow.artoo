package dev.lug.oak.collect.cursor;

import dev.lug.oak.collect.Iterator3;
import dev.lug.oak.type.union.Union3;
import dev.lug.oak.func.fun.Function3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static dev.lug.oak.type.Nullability.nonNullable;

@SuppressWarnings("unchecked")
public interface Cursor3<V1, V2, V3> extends Iterator3<V1, V2, V3>, Union3<V1, V2, V3> {
  static <T1, T2, T3> Cursor3<T1, T2, T3> forward(final Union3<T1, T2, T3>... values) {
    return new Forward3<>(Arrays.copyOf(values, values.length));
  }

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <T1, T2, T3> Cursor3<T1, T2, T3> once(final T1 value1, final T2 value2, final T3 value3) {
    return new Once3<>(value1, value2, value3);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> Cursor3<V1, V2, V3> none() {
    return (Cursor3<V1, V2, V3>) Default.None3;
  }

  @Override
  default Union3<V1, V2, V3> next() { return this; }

  @Override
  default boolean hasNext() { return false; }
}

final class None3<V1, V2, V3> implements Cursor3<V1, V2, V3> {
  @Override
  public final <T> T as(@NotNull Function3<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(null, null, null);
  }
}

final class Once3<V1, V2, V3> implements Cursor3<V1, V2, V3> {
  private final V1 value1;
  private final V2 value2;
  private final V3 value3;
  private boolean notRead;

  Once3(final V1 value1, final V2 value2, final V3 value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.notRead = true;
  }

  @Override
  public final boolean hasNext() {
    var has = notRead;
    notRead = false;
    return has;
  }

  @Override
  public final <T> T as(@NotNull Function3<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(value1, value2, value3);
  }
}

final class Forward3<V1, V2, V3> implements Cursor3<V1, V2, V3> {
  private final Union3<V1, V2, V3>[] unions;
  private final Index index;

  Forward3(final Union3<V1, V2, V3>[] unions) { this(unions, new Index()); }
  private Forward3(final Union3<V1, V2, V3>[] unions, final Index index) {
    this.unions = unions;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return this.index.eval() < this.unions.length;
  }

  @Override
  public final <T> T as(@NotNull final Function3<V1, V2, V3, T> as) {
    return this.unions[this.index.eval()].as(nonNullable(as, "as"));
  }
}
