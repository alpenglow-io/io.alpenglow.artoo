package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Filter<ELEMENT>(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition, Next<ELEMENT> near) implements Cursor<ELEMENT> {
  public Filter(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    this(fetch, condition, new Next<>());
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() {
    return (index, element) -> element == null;
  }

  @Override
  public boolean hasElement() throws Throwable {
    do {
      near.hasElement = fetch.hasElement();
      if (near.hasElement) {
        fetch.element(near::set);
        if (condition.invoke(near.index, near.element)) break;
      }
    } while (fetch.hasElement());
    return near.hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
      return near.hasElement ? near.let(then) : FetchException.byThrowingCantFetchNextElement("filter", "filterable");
  }
}
