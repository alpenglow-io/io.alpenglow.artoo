package io.artoo.frost.scene;

import io.artoo.frost.scene.element.Modal;
import io.artoo.frost.scene.element.Section;
import io.artoo.frost.scene.scene.Buttonable;
import io.artoo.frost.scene.scene.Labelable;
import io.artoo.frost.scene.scene.Modalable;
import io.artoo.frost.scene.scene.Sectionable;
import io.artoo.frost.scene.scene.Textable;
import io.artoo.lance.func.Func;
import io.artoo.lance.type.Late;
import io.artoo.lance.type.Let;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public sealed interface Scene extends Sectionable, Textable, Modalable, Buttonable, Labelable, Let<Scene> permits Scene.Internal {
  static Scene scene() {
    return new Internal(new ConcurrentHashMap<>(), Late.init());
  }

  Scene open(Modal modal);

  Scene open(Section section);

  Collection<Element<?>> elements();

  final class Internal implements Scene {
    private final Map<Id, Element<?>> elements;
    private final Late<Frame> frame;

    private Internal(final Map<Id, Element<?>> elements, final Late<Frame> frame) {
      this.elements = elements;
      this.frame = frame;
    }

    @Override
    public Scene open(final Modal modal) {
      return this.frame
        .set(() -> Frame.terminal(modal))
        .get(frame -> frame.render(this))
        .let(it -> this);
    }

    @Override
    public Scene open(final Section section) {
      return this.frame
        .set(() -> Frame.terminal(Modal.fullSize("", section)))
        .get(frame -> frame.render(this))
        .let(it -> this);
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

    @Override
    public final <R> R let(final Func.Uni<? super Scene, ? extends R> func) {
      return func.apply(this);
    }
  }
}
