package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Filter<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntPredicate1<? super ELEMENT> condition;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Filter(Fetchable<ELEMENT> fetchable, TryIntPredicate1<? super ELEMENT> condition) {
    this.fetchable = fetchable;
    this.condition = condition;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    if (!hasElement && (hasElement = fetchable.canFetch())) {
      do {
        element = fetchable.fetch((index, element) -> {
          this.index = index;
          return element;
        });
      } while (!condition.invoke(index, element) && (hasElement = fetchable.canFetch()));
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(index, element) : throwing(() -> Fetchable.Exception.of("filter", "filterable"));
    } finally {
      hasElement = false;
    }
  }
}
