package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Filter<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntPredicate1<? super ELEMENT> condition;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Filter(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    this.fetch = fetch;
    this.condition = condition;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      do {
        element = fetch.element((index, element) -> {
          this.index = index;
          return element;
        });
      } while (!condition.invoke(index, element) && (hasElement = fetch.hasElement()));
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(index, element) : raise(() -> Fetch.Exception.of("filter", "filterable"));
    } finally {
      hasElement = false;
    }
  }
}
