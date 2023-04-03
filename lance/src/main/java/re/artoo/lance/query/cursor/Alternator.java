package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Catch;
import re.artoo.lance.query.cursor.operation.PresenceOnly;
import re.artoo.lance.query.cursor.operation.Er;
import re.artoo.lance.query.cursor.operation.Or;

public sealed interface Alternator<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <C extends Cursor<ELEMENT>> Cursor<ELEMENT> or(final TrySupplier1<? extends C> alternative) {
    return new Or<>(new PresenceOnly<>(this), null);
  }


  default <E extends RuntimeException> Cursor<ELEMENT> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

