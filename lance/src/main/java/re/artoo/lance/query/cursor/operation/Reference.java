package re.artoo.lance.query.cursor.operation;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

sealed interface Reference<ELEMENT> {
  static <ELEMENT> Reference<ELEMENT> iterative() {
    return new Iterative<>();
  }
  int indexPlusPlus();
  int plusPlusIndex();

  ELEMENT element();
  Reference<ELEMENT> element(ELEMENT element);
  boolean isFetched();
  default boolean isNotFetched() { return !isFetched(); }

  final class Iterative<ELEMENT> implements Reference<ELEMENT> {
    private final AtomicInteger index;
    private final AtomicReference<ELEMENT> reference;
    private final AtomicBoolean fetched;
    private Iterative() {
      this.index = new AtomicInteger(0);
      this.reference = new AtomicReference<>();
      this.fetched = new AtomicBoolean(false);
    }
    @Override
    public int indexPlusPlus() {
      return index.getAndIncrement();
    }
    @Override
    public int plusPlusIndex() {
      return index.incrementAndGet();
    }
    @Override
    public ELEMENT element() {
      var element = reference.getAndSet(null);
      fetched.set(true);
      return element;
    }
    @Override
    public Reference<ELEMENT> element(ELEMENT element) {
      reference.set(element);
      fetched.set(false);
      return this;
    }

    @Override
    public boolean isFetched() {
      return fetched.get();
    }
  }
}
