package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Reduce<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation;

  public Reduce(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    super("reduce", "reducible");
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && fetch.hasElement()) {
      hasElement = true;
      class Reduced { ELEMENT value = fetch.next(); }
      final var reduced = new Reduced();
      while (fetch.hasElement()) {
        reduced.value = fetch.element((index, element) -> operation.invoke(index, reduced.value, element));
      }
      element = reduced.value;
    }
    return hasElement;
  }
}
