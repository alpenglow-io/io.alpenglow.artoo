package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Filter<ELEMENT> extends Current<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntPredicate1<? super ELEMENT> condition;

  public Filter(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    super(fetch, "filter", "filterable");
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
        fetch.element(this::set);
      } while (!condition.invoke(index, element) && (hasElement = fetch.hasElement()));
    }
    return hasElement;
  }
}
