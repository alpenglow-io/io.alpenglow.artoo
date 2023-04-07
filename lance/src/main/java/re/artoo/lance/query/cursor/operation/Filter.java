package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

@SuppressWarnings("UnnecessaryBreak")
public record Filter<ELEMENT>(Fetch<ELEMENT> fetch, Value<ELEMENT> value, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Filter(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    this(fetch, new Value<>(), condition);
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() {
    return (index, element) -> element == null;
  }

  @Override
  public boolean hasNext() {
    if (!value.fetched) return true;
    while (fetch.hasNext()) {
      if (isTested())
        return true;
    }

    return false;
  }

  private Boolean isTested() {
    return fetch.next((index, element) -> condition.test(index, element) && value.set(index, element));
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    try {
      return hasNext() ? value.get(then) : FetchException.byThrowingCantFetchNextElement("filter", "filterable");
    } finally {
      value.fetched = true;
    }
  }
}
