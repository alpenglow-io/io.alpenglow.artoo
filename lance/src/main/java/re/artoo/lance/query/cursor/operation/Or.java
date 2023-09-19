package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Or<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TrySupplier1<? extends Fetchable<ELEMENT>> vice;
  private boolean exhausted;

  public Or(Fetchable<ELEMENT> fetchable, TrySupplier1<? extends Fetchable<ELEMENT>> vice) {
    this.fetchable = fetchable;
    this.vice = new TrySupplier1<>() {
      private lazy lazy;

      @Override
      public Fetchable<ELEMENT> invoke() throws java.lang.Throwable {
        return (lazy == null ? (lazy = new lazy()) : lazy).fetchable;
      }

      class lazy {
        final Fetchable<ELEMENT> fetchable = vice.invoke();

        lazy() throws java.lang.Throwable {
        }
      }
    };
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    boolean hasElement = fetchable.canFetch();
    if (hasElement) exhausted = true;
    return hasElement || (!exhausted && vice.invoke() != null && vice.invoke().canFetch());
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    return canFetch() && fetchable.canFetch() && exhausted
      ? fetchable.fetch(then)
      : !exhausted && vice.invoke() != null && vice.invoke().canFetch()
      ? vice.invoke().fetch(then)
      : throwing(() -> Fetchable.Exception.of("or", "elseable"));
  }
}
