package oak.collect.seq;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.ThreadLocal.withInitial;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static oak.collect.cursor.Cursor.jump;
import static oak.collect.cursor.Cursor.none;
import static oak.collect.cursor.Cursor.once;

public interface Sequence<T> extends Iterable<T> {
  Sequence<T> add(T value);
  long size();
}

final class LocalSequence<T> implements Sequence<T> {
  private final ThreadLocal<Item<T>> first;
  private final ThreadLocal<Item<T>> last;
  private final AtomicLong size;

  LocalSequence() {
    this(
      new ThreadLocal<>(),
      new ThreadLocal<>(),
      new AtomicLong(0)
    );
  }
  private LocalSequence(final ThreadLocal<Item<T>> first, final ThreadLocal<Item<T>> last, final AtomicLong size) {
    this.first = first;
    this.last = last;
    this.size = size;
  }

  @Override
  public Sequence<T> add(T value) {
    final var item = new Item<T>(value);

    if (isNull(first.get())) {
      first.set(item);
    } else if (isNull(first.get().next())) {
      first.get().next(item);
    } else {
      last.get().next(item);
    }

    last.set(item);
    size.incrementAndGet();

    return this;
  }

  @Override
  public final long size() {
    return this.size.get();
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return nonNull(first.get()) ? first.get().iterator() : none();
  }
}

final class Item<T> implements Iterable<T> {
  private final ThreadLocal<T> value;
  private final ThreadLocal<Item<T>> next;

  Item() {
    this(new ThreadLocal<>(), new ThreadLocal<>());
    new LinkedList<>();
  }
  Item(final T value) {
    this(withInitial(() -> value), new ThreadLocal<>());
  }
  private Item(final ThreadLocal<T> value, final ThreadLocal<Item<T>> next) {
    this.value = value;
    this.next = next;
  }

  public final Item<T> next() {
    return this.next.get();
  }

  public void next(Item<T> item) {
    this.next.set(item);
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return isNull(next.get()) ? once(value.get()) : jump(once(value.get()), next.get().iterator());
  }
}
