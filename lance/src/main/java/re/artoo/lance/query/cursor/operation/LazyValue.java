package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.value.Lazy;

public final class LazyValue<ELEMENT> implements Cursor<ELEMENT> {
  private final Lazy<ELEMENT> elements;
  private boolean fetched;

  public LazyValue(TrySupplier1<? extends ELEMENT> elements) {
    this(Lazy.lazy(elements));
  }

  private LazyValue(Lazy<ELEMENT> elements) {
    this.elements = elements;
  }

  @Override
  public boolean hasElement() {
    return !fetched;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return apply.invoke(0, elements.value());
    } finally {
      fetched = true;
    }
  }
}
