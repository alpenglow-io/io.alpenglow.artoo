package re.artoo.lance.value;

import re.artoo.lance.func.InvokeException;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;

import static re.artoo.lance.value.Default.Nothing;

public non-sealed interface Late<T> extends Let<T> {
  static <T> Late<T> init() {
    return new Late.Init<>();
  }

  Late<T> set(TrySupplier1<? extends T> suppl);

  sealed interface WriteOnce<T> extends Late<T> permits Late.Init {
  }

  @SuppressWarnings("unchecked")
  final class Init<T> implements WriteOnce<T> {
    private final Lock lock;
    private volatile Object value;

    private Init() {
      this.lock = Lock.reentrant();
      this.value = Nothing;
    }

    @Override
    public Late<T> set(final TrySupplier1<? extends T> suppl) {
      this.value = lock.write(suppl);
      return this;
    }

    @Override
    public <R> R let(final TryFunction1<? super T, ? extends R> func) {
      return lock
        .read(() -> func.invoke((T) value))
        .otherwise("Can't invoke", InvokeException::new);
    }
  }
}
