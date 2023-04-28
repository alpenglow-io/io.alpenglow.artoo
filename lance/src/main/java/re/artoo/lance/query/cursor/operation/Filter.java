package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Filter<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryIntPredicate1<? super ELEMENT> condition;

  public Filter(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    super("filter", "filterable");
    this.fetch = fetch;
    this.condition = condition;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> presenceOnly() {
    return (index, element) -> element != null;
  }

  public static <ELEMENT> TryIntPredicate1<? super ELEMENT> absenceOnly() {
    return (index, element) -> element == null;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      do {
        //noinspection AssignmentUsedAsCondition
        if (hasElement = fetch.hasElement()) fetch.element(this::set);
      } while (!condition.invoke(index, element));
    }
    return hasElement;
  }
}
