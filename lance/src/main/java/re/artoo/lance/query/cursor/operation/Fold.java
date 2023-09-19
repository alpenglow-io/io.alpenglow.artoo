package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Fold<ELEMENT, FOLDED> implements Cursor<FOLDED>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation;
  private FOLDED folded;
  private boolean hasFolded = true;

  public Fold(Fetchable<ELEMENT> fetchable, FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    this.fetchable = fetchable;
    this.operation = operation;
    this.folded = initial;
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (hasFolded) {
      while (fetchable.canFetch()) {
        folded = fetchable.fetch((index, element) -> operation.invoke(index, folded, element));
      }
    }
    return hasFolded;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super FOLDED, ? extends NEXT> then) throws Throwable {
    try {
      return hasFolded || canFetch() ? then.invoke(0, folded) : throwing(() -> Fetchable.Exception.of("fold", "foldable"));
    } finally {
      hasFolded = false;
    }
  }
}
