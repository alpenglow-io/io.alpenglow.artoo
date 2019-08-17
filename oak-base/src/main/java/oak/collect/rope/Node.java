package oak.collect.rope;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Node<T> extends Iterable<T> {
  Node<T> next(T value);

  Node<T> next();
}

final class EmptyNode<T> implements Node<T> {
  private static final String EMPTY_RING = "EmptyRing<?>()";

  @Override
  public final Node<T> next(T value) {
    return new SingleNode<>(value);
  }

  @Override
  public final Node<T> next() {
    return this;
  }

  @Override
  public final int hashCode() {
    return EMPTY_RING.hashCode();
  }

  @Override
  public final String toString() {
    return EMPTY_RING;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof EmptyNode && obj.hashCode() == this.hashCode();
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.none();
  }
}

final class SingleNode<T> implements Node<T> {
  private final T value;

  SingleNode(final T value) {
    this.value = value;
  }

  @Override
  public final Node<T> next(T value) {
    return new NextNode<>(this.value, new SingleNode<>(value));
  }

  @Override
  public final Node<T> next() {
    return new EmptyNode<>();
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.once(value);
  }
}

final class NextNode<T> implements Node<T> {
  private final T value;
  private final Node<T> next;

  NextNode(final T value, final Node<T> next) {
    this.value = value;
    this.next = next;
  }

  @Override
  public final Node<T> next(final T value) {
    return this.next.next(value);
  }

  @Override
  public final Node<T> next() {
    return next;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.jump(Cursor.once(value), next.iterator());
  }
}
