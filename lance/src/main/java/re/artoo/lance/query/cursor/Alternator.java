package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Catch;
import re.artoo.lance.query.cursor.operation.Er;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.Or;

import static re.artoo.lance.query.cursor.operation.Filter.presenceOnly;

public sealed interface Alternator<ELEMENT> extends Fetch<ELEMENT> permits Cursor {
  default <F extends Fetch<ELEMENT>> Cursor<ELEMENT> or(final F alternative) {
    return new Or<>(new Filter<>(this, presenceOnly()), alternative);
  }


  default <E extends RuntimeException> Cursor<ELEMENT> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

