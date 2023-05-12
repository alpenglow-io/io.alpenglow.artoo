package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Fold<ELEMENT, FOLDED> implements Cursor<FOLDED>, Exceptionable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation;
  private FOLDED folded;
  private boolean hasFolded = true;

  public Fold(Fetch<ELEMENT> fetch, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    this.fetch = fetch;
    this.operation = operation;
    this.folded = initial;
  }
  @Override
  public boolean hasElement() throws Throwable {
    if (hasFolded) {
      while (fetch.hasElement()) {
        folded = fetch.element((index, element) -> operation.invoke(index, folded, element));
      }
    }
    return hasFolded;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super FOLDED, ? extends NEXT> then) throws Throwable {
    try {
      return hasFolded || hasElement() ? then.invoke(0, folded) : raise(() -> FetchException.of("fold", "foldable"));
    } finally {
      hasFolded = false;
    }
  }
}
