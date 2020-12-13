package io.artoo.frost.scene;

import io.artoo.frost.scene.element.Modal;
import io.artoo.frost.scene.element.Section;
import io.artoo.frost.scene.scene.Buttonable;
import io.artoo.frost.scene.scene.Labelable;
import io.artoo.frost.scene.scene.Modalable;
import io.artoo.frost.scene.scene.Sectionable;
import io.artoo.frost.scene.scene.Textable;
import io.artoo.lance.type.Value;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public sealed interface Scene extends Sectionable, Textable, Modalable, Buttonable, Labelable permits Scene.Frame {
  static Scene frame() {
    return new Frame(new ConcurrentHashMap<>(), Value.late());
  }

  Scene open(Modal modal);
  Scene open(Section section);

  Collection<Element<?>> elements();

  final class Frame implements Scene {
    private final Map<Id, Element<?>> elements;
    private final Value<io.artoo.frost.scene.Frame> frame;

    private Frame(final Map<Id, Element<?>> elements, final Value<io.artoo.frost.scene.Frame> frame) {
      this.elements = elements;
      this.frame = frame;
    }

    @Override
    public Scene open(final Modal modal) {
      this.frame.set(io.artoo.frost.scene.Frame.terminal(modal)).get().render(this);
      return this;
    }

    @Override
    public Scene open(final Section section) {
      this.frame.set(io.artoo.frost.scene.Frame.terminal(Modal.fullSize("", section))).get().render(this);
      return this;
    }

    @Override
    public <T> Scene prop(final Id id, final Element<T> element) {
      elements.put(id, element);
      return this;
    }

    @Override
    public Collection<Element<?>> elements() {
      return elements.values();
    }
  }
}
