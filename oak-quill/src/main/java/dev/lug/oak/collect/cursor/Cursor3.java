package dev.lug.oak.collect.cursor;

import dev.lug.oak.collect.Iterator3;
import dev.lug.oak.func.as.As3;
import dev.lug.oak.func.fun.Function3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

@SuppressWarnings("unchecked")
public interface Cursor3<V1, V2, V3> extends Iterator3<V1, V2, V3>, As3<V1, V2, V3> {
  static <T1, T2, T3> Cursor3<T1, T2, T3> once(final T1 value1, final T2 value2, final T3 value3) {
    return new Once3<>(value1, value2, value3);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> Cursor3<V1, V2, V3> none() {
    return (Cursor3<V1, V2, V3>) Default3.None;
  }

  @Override
  default As3<V1, V2, V3> next() { return this; }
}

enum Default3 {
  ;
  public static Cursor3<?, ?, ?> None = new None3<>();
}

final class None3<V1, V2, V3> implements Cursor3<V1, V2, V3> {
  @Override
  public final <T> T as(@NotNull Function3<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(null, null, null);
  }

  @Override
  public final boolean hasNext() {
    return false;
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
