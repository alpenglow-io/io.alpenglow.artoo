package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Or<ELEMENT> implements Cursor<ELEMENT>, Throwing {
  private final Fetch<ELEMENT> fetch;
  private final TrySupplier1<? extends Fetch<ELEMENT>> vice;
  private boolean exhausted;

  public Or(Fetch<ELEMENT> fetch, TrySupplier1<? extends Fetch<ELEMENT>> vice) {
    this.fetch = fetch;
    this.vice = new TrySupplier1<>() {
      class lazy {
        final Fetch<ELEMENT> fetch = vice.invoke();

        lazy() throws Throwable {
        }
      }
      private lazy lazy;

      @Override
      public Fetch<ELEMENT> invoke() throws Throwable {
        return (lazy == null ? (lazy = new lazy()) : lazy).fetch;
      }
    };
  }

  @Override
  public boolean hasElement() throws Throwable {
    boolean hasElement = fetch.hasElement();
    if (hasElement) exhausted = true;
    return hasElement || (!exhausted && vice.invoke() != null && vice.invoke().hasElement());
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() && fetch.hasElement() && exhausted
      ? fetch.element(then)
      : !exhausted && vice.invoke() != null && vice.invoke().hasElement()
      ? vice.invoke().element(then)
      : throwing(() -> Fetch.Exception.of("or", "elseable"));
  }
}
