package io.artoo.ladylake.type;

import io.artoo.ladylake.text.Text;

public interface SimpleName extends Text {
  static <T> SimpleName simpleNameOf(T any) {
    return new Type<>(any.getClass());
  }

  static <T> SimpleName simpleNameOf(Class<T> type) {
    return new Type<>(type);
  }
}

record Type<T>(Class<T> any) implements SimpleName {
  @Override
  public String toString() {
    return any.getSimpleName();
  }
}
