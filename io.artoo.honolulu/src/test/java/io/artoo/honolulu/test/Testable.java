package io.artoo.honolulu.test;

import io.artoo.lance.func.Action;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import org.awaitility.Awaitility;

import java.util.Base64;

import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static io.vertx.core.buffer.Buffer.buffer;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface Testable {

  String ImageBase64 = "R0lGODlhDAAMAPIAAAAAAAAAANyuAP/gaF5LAP/zxAAAAAAAACH5BAUAAAAALAAAAAAMAAwAAAMlCLLcWjDKVYa9l46A+aCcF35ChWHamZUW0a7mQLiwWtuf0uxAAgA7";
  byte[] ImageBytes = Base64.getDecoder().decode(ImageBase64);
  Future<Buffer> ImageFutureOk = succeededFuture(buffer(ImageBytes));
  Future<Buffer> ImageFutureKo = failedFuture("Can't provide image");

  default void await(Action action) {
    await(2, action);
  }
  default void await(int timeout, Action action) {
    Awaitility.await().atMost(timeout, SECONDS).untilAsserted(action::tryExecute);
  }
}
