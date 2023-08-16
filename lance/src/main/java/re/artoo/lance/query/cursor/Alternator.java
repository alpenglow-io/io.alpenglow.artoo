package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.*;

public sealed interface Alternator<ELEMENT> extends Fetch<ELEMENT> permits Cursor {
  default <FETCH extends Fetch<ELEMENT>> Cursor<ELEMENT> or(FETCH otherwise) {
    return new Or<>(this, otherwise);
  }

  default Cursor<ELEMENT> or(final String message, final TryFunction1<? super String, ? extends Throwable> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Exceptionally<>(this, (__, throwable) -> catch$.invoke(throwable));
  }

  default Cursor<ELEMENT> exceptionally(TryIntConsumer1<? super Throwable> catch$) {
    return new Exceptionally<>(this, catch$);
  }

  default Cursor<ELEMENT> eventually(TryFunction1<? super Throwable, ? extends ELEMENT> recover) {
    return new Eventually<>(this, (__, throwable) -> recover.invoke(throwable));
  }

  default Cursor<ELEMENT> eventually(TrySupplier1<? extends ELEMENT> recover) {
    return new Eventually<>(this, (__, ___) -> recover.invoke());
  }

  interface TryFunc1<T, R> extends TryFunction1<T, R> {
  }
}

