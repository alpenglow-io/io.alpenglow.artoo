package io.artoo.parry.eventbus;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentCyclicSequence<T> implements Iterable<T>, Iterator<T> {

  private static final Object[] EMPTY_ARRAY = new Object[0];

  private final AtomicInteger pos;
  private final Object[] elements;

  /**
   * Create a new empty sequence.
   */
  public ConcurrentCyclicSequence() {
    this(0, EMPTY_ARRAY);
  }

  /**
   * Create a new empty sequence.
   */
  @SafeVarargs
  public ConcurrentCyclicSequence(T... elements) {
    this(0, Arrays.copyOf(elements, elements.length, Object[].class));
  }

  private ConcurrentCyclicSequence(int pos, Object[] elements) {
    this.pos = new AtomicInteger(pos);
    this.elements = elements;
  }

  /**
   * @return the current index
   */
  public int index() {
    return elements.length > 0 ? pos.get() % elements.length : 0;
  }

  /**
   * @return the first element or {@code null} when the sequence is empty
   */
  @SuppressWarnings("unchecked")
  public T first() {
    return (T) (elements.length > 0 ? elements[0] : null);
  }

  /**
   * Copy the current sequence, add {@code element} at the tail of this sequence and returns it.
   *
   * @param element the element to add
   * @return the resulting sequence
   */
  public ConcurrentCyclicSequence<T> add(T element) {
    var len = elements.length;
    var copy = Arrays.copyOf(elements, len + 1);
    copy[len] = element;
    return new ConcurrentCyclicSequence<>(pos.get(), copy);
  }

  /**
   * Remove the first occurrence of {@code element} in this sequence and returns it.
   * <p/>
   * If the sequence does not contains {@code element}, this instance is returned instead.
   *
   * @param element the element to remove
   * @return the resulting sequence
   */
  public ConcurrentCyclicSequence<T> remove(T element) {
    var len = elements.length;
    for (var i = 0; i < len; i++) {
      if (Objects.equals(element, elements[i])) {
        if (len > 1) {
          var copy = new Object[len - 1];
          System.arraycopy(elements, 0, copy, 0, i);
          System.arraycopy(elements, i + 1, copy, i, len - i - 1);
          return new ConcurrentCyclicSequence<>(pos.get() % copy.length, copy);
        } else {
          return new ConcurrentCyclicSequence<>();
        }
      }
    }
    return this;
  }

  /**
   * @return always {@code true}
   */
  @Override
  public boolean hasNext() {
    return true;
  }

  /**
   * @return the next element in the sequence
   */
  @SuppressWarnings("unchecked")
  @Override
  public T next() {
    return switch (elements.length) {
      case 0 -> null;
      case 1 -> (T) elements[0];
      default -> {
        int p;
        p = pos.getAndIncrement();
        yield (T) elements[Math.abs(p % elements.length)];
      }
    };
  }

  /**
   * @return the size of this sequence
   */
  public int size() {
    return elements.length;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<T> iterator() {
    return Arrays.asList((T[]) elements).iterator();
  }
}

