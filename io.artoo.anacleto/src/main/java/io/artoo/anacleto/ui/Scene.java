package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.Window;
import io.artoo.lance.type.Value;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public sealed interface Scene permits Scene.Impl {
  static Scene scene() {
    return new Impl(new ConcurrentHashMap<>(), Value.late());
  }

  Window window();

  Scene open(Frame frame);

  default Props prop() {
    return new Props(Id.random(), this);
  }

  <T> Scene prop(Id id, Element<T> element);
  default <T> Scene prop(String id, Element<T> element) {
    return prop(Id.from(id), element);
  }
  default <T> Scene prop(Element<T> element) {
    return prop(Id.random(), element);
  }

  Collection<Element<?>> elements();

  final class Impl implements Scene {
    private final Map<Id, Element<?>> elements;
    private final Value<Frame> frame;

    private Impl(final Map<Id, Element<?>> elements, final Value<Frame> frame) {
      this.elements = elements;
      this.frame = frame;
    }

    @Override
    public Window window() {
      return (Window) elements.get(Modal.FullSize.ID).content();
    }

    @Override
    public Scene open(final Frame frame) {
      this.frame.set(frame).get().render(this);
      return this;
    }

    @Override
    public <T> Scene prop(final Id id, final Element<T> element) {
      elements.put(id, new Element.Attached<>(element));
      return this;
    }

    @Override
    public Collection<Element<?>> elements() {
      return elements.values();
    }

   /* @Override
    public <T> T append(final Node<T> node) {
      final var length = props.get().length;

      props
        .set(() -> Arrays.copyOf(props.get(), length + 1))
        .get()[props.get().length - 1] = node;

      elements.put(node.id(), props.get()[length].element());
      return (T) elements.get(node.id());
    }*/


  }
}
