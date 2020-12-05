package io.artoo.anacleto.ui.scene;

import io.artoo.anacleto.ui.Element;
import io.artoo.anacleto.ui.Id;
import io.artoo.anacleto.ui.Scene;

public interface Propable {
  <T> Scene prop(Id id, Element<T> element);
  default <T> Scene prop(String id, Element<T> element) {
    return prop(Id.from(id), element);
  }
  default <T> Scene prop(Element<T> element) {
    return prop(Id.random(), element);
  }
}
