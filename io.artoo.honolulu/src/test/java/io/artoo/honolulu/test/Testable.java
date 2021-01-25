package io.artoo.honolulu.test;

import io.artoo.lance.func.Action;
import org.awaitility.Awaitility;

import java.util.Base64;

import static java.util.concurrent.TimeUnit.SECONDS;

public interface Testable {

  String ImageBase64 = "R0lGODlhDAAMAPIAAAAAAAAAANyuAP/gaF5LAP/zxAAAAAAAACH5BAUAAAAALAAAAAAMAAwAAAMlCLLcWjDKVYa9l46A+aCcF35ChWHamZUW0a7mQLiwWtuf0uxAAgA7";
  byte[] ImageBytes = Base64.getDecoder().decode(ImageBase64);

  default void await(Action action) {
    await(2, action);
  }
  default void await(int timeout, Action action) {
    Awaitility.await().atMost(timeout, SECONDS).untilAsserted(action::tryExecute);
  }
}
