package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Fold<ELEMENT, FOLDED> extends Current<FOLDED> implements Cursor<FOLDED> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation;
  public Fold(Fetch<ELEMENT> fetch, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    super(new Open<>(), "fold", "foldable");
    this.fetch = fetch;
    this.operation = operation;
    this.set(0, initial);
  }
  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      hasElement = true;
      while (fetch.hasElement()) {
        this.element = fetch.element((index, element) -> operation.invoke(index, this.element, element));
      }
    }
    return hasElement;
  }
}
