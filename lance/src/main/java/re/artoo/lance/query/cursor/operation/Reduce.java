package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Reduce<ELEMENT> extends Current<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation;

  public Reduce(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    super(fetch, "reduce", "reducible");
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      fetch.element(this::set);
      while (fetch.hasElement()) {
        this.element = fetch.element((index, element) -> operation.invoke(index, this.element, element));
      }
    }
    return hasElement;
  }
}
