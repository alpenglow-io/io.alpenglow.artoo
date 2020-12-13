package io.artoo.frost.scene.scene;

import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.Id;
import io.artoo.frost.scene.Scene;

public interface Propable {
  <T> Scene prop(Id id, Element<T> element);
  default <T> Scene prop(String id, Element<T> element) {
    return prop(Id.from(id), element);
  }
  default <T> Scene prop(Element<T> element) {
    return prop(Id.random(), element);
  }
}
