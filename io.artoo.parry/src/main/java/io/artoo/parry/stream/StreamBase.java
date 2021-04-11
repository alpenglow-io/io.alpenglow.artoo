package io.artoo.parry.stream;

import io.artoo.parry.async.Handler;

public interface StreamBase {

  /**
   * Set an exception handler.
   *
   * @param handler the handler
   * @return a reference to this, so the API can be used fluently
   */
  StreamBase exceptionHandler(Handler<Throwable> handler);
}
