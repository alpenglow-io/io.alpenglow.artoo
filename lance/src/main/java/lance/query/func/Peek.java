package lance.query.func;

import lance.func.Cons.MaybeBiConsumer;
import lance.func.Func;

@SuppressWarnings("unchecked")
public final class Peek<T, R> implements Func.MaybeFunction<T, R> {
  private final Peeked peeked = new Peeked();
  private final MaybeBiConsumer<? super Integer, ? super T> peek;

  public Peek(final MaybeBiConsumer<? super Integer, ? super T> peek) {
    assert peek != null;
    this.peek = peek;
  }

  @Override
  public final R tryApply(final T element) throws Throwable {
    peek.tryAccept(peeked.index++, element);
    return (R) element;
  }

  private static final class Peeked {
    private int index;
  }
}
