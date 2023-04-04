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
    again:
    if (fetch.hasNext())
      if (fetch.next((index, element) -> condition.test(index, element) && value.set(index, element)))
        return true;
      else
        break again;

    return false;
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return hasNext() ? value.then(then) : FetchException.byThrowingCantFetchNextElement("filter", "filterable");
  }
}
