package dev.lug.oak.collect.cursor;

import dev.lug.oak.collect.Iterable2;
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

  @Nullable
  @Contract(pure = true)
  static <V2, V1> Cursor2<V1, V2> none() {
    return (Cursor2<V1, V2>) Default.None;
  }

  final class T2<V1, V2> implements Iterable2<V1, V2> {
    private final V1 value1;
    private final V2 value2;

    public T2(V1 value1, V2 value2) {
      this.value1 = value1;
      this.value2 = value2;
    }

    @NotNull
    @Override
    public Iterator<As2<V1, V2>> iterator() {
      return Cursor2.once(value1, value2);
    }
  }

  static void main(String[] args) {
    final var t2 = new T2<>(12, 13);

    for (final var tuple : t2)
      System.out.println(tuple.<String>as((v1, v2) -> String.format("Ciao %d, %d", v1, v2)));

  }
}

enum Default {
  ;
  public static None2<?, ?> None = new None2<>();
}

final class Index implements AsInt {
  private int value;
  private final StampedLock stamp;

  Index() { this(0, new StampedLock()); }
  private Index(final int value, StampedLock stamp) {
    this.value = value;
    this.stamp = stamp;
  }

  public final Index inc() {
    final var write = this.stamp.asWriteLock();
    try {
      write.lock();
      value++;
    } finally {
      write.unlock();
    }
    return this;
  }

  @Override
  public final int eval() {
    final var read = this.stamp.asReadLock();
    try {
      read.lock();
      return this.value;
    } finally {
      read.unlock();
    }
  }
}

final class None2<V1, V2> implements Cursor2<V1, V2> {
  @Override
  public final <T> T as(Function2<V1, V2, T> as) {
    throw new IllegalStateException("None has no state.");
  }

  @Override
  public final boolean hasNext() {
    return false;
  }

  @Override
  public final As2<V1, V2> next() {
    throw new IllegalStateException("None has no state.");
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
