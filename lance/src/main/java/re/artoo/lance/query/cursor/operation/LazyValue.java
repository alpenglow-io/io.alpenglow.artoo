package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.experimental.value.Lazy;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;

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
  public boolean canFetch() {
    return !fetched;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return apply.invoke(0, elements.value());
    } finally {
      fetched = true;
    }
  }
}
