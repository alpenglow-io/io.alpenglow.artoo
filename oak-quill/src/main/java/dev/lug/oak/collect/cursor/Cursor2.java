package dev.lug.oak.collect.cursor;

import dev.lug.oak.collect.Iterator2;
import dev.lug.oak.func.as.As2;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.type.AsInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

@SuppressWarnings("unchecked")
public interface Cursor2<V1, V2> extends Iterator2<V1, V2>, As2<V1, V2> {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <T1, T2> Cursor2<T1, T2> once(T1 value1, T2 value2) {
    return new Once2<>(value1, value2);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2> Cursor2<V1, V2> none() {
    return (Cursor2<V1, V2>) Default.None;
  }
}

enum Default {
  ;
  public static Cursor2<?, ?> None = new None2<>();
  public static As2<?, ?> Nothing = new As2<>() {
    @Override
    public <T> T as(Function2<? super Object, ? super Object, T> as) {
      return null;
    }
  };
}

@SuppressWarnings("unchecked")
final class None2<V1, V2> implements Cursor2<V1, V2> {
  @Override
  public final <T> T as(Function2<V1, V2, T> as) {
    return null;
  }

  @Override
  public final boolean hasNext() {
    return false;
  }

  @Override
  public final As2<V1, V2> next() {
    return (As2<V1, V2>) Default.Nothing;
  }
}

final class Once2<V1, V2> implements Cursor2<V1, V2> {
  private final V1 value1;
  private final V2 value2;
  private boolean notRead;

  Once2(final V1 value1, final V2 value2) {
    this.value1 = value1;
    this.value2 = value2;
    this.notRead = true;
  }

  @Override
  public final boolean hasNext() {
    var has = notRead;
    notRead = false;
    return has;
  }

  @Override
  public final As2<V1, V2> next() {
    return this;
  }

  @Override
  public final <T> T as(@NotNull Function2<V1, V2, T> as) {
    return as.apply(value1, value2);
  }
}

final class Forward2<V1, V2> implements Cursor2<V1, V2> {
  private final V1[] values1;
  private final V2[] values2;
  private final Index index;

  Forward2(final V1[] values1, final V2[] values2) {
    this(values1, values2, new Index());
  }
  private Forward2(final V1[] values1, final V2[] values2, final Index index) {
    this.values1 = values1;
    this.values2 = values2;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return this.index.eval() < this.values1.length;
  }

  @NotNull
  @Override
  public final As2<V1, V2> next() {
    return this;
  }

  @Override
  public final <T> T as(@NotNull Function2<V1, V2, T> as) {
    var index = this.index.eval();
    return as.apply(values1[index], values2[index]);
  }
}
