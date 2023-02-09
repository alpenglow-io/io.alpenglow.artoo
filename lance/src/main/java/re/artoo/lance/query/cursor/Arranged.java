package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Arranged<T> implements Cursor<T> {
  private final T[] elements;
  private int forward;
  private int backward;

  public Arranged(T[] elements) {
    this(0, elements, elements.length - 1);
  }
  private Arranged(int forward, T[] elements, int backward) {
    this.forward = forward;
    this.elements = elements;
    this.backward = backward;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(forward, elements[forward++]);
  }

  @Override
  public Head<T> head() {
    return new Arranged<>(elements);
  }

  @Override
  public boolean hasNext() {
    return forward < elements.length;
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(backward, elements[backward--]);
  }

  @Override
  public Tail<T> tail() {
    return new Arranged<>(elements);
  }

  @Override
  public boolean hasPrior() {
    return backward >= 0;
  }

  @Override
  public Head<T> replicate() {
    return new Arranged<>(elements);
  }
}
