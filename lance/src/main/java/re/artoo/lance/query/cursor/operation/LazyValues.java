package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.value.Lazy;

public final class LazyValues<ELEMENT> implements Cursor<ELEMENT> {
  private final Lazy<ELEMENT[]> elements;
  private int index;

  public LazyValues(TrySupplier1<? extends ELEMENT[]> elements) {
    this(Lazy.lazy(elements), 0);
  }

  private LazyValues(Lazy<ELEMENT[]> elements, int index) {
    this.elements = elements;
    this.index = index;
  }

  @Override
  public boolean hasElement() {
    return index < elements.value().length;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return apply.invoke(index, elements.value()[index]);
    } finally {
      index++;
    }
  }
}
