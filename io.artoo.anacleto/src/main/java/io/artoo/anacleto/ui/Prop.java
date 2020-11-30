package io.artoo.anacleto.ui;

public sealed interface Prop<T, E extends Element<T>> permits Prop.Component {

  E element();

  final class Component<T, E extends Element<T>> implements Prop<T, E> {
    private final Scene scene;
    private final Id id;
    private final E element;

    private Component(final Scene scene, final Id id, final E element) {
      this.scene = scene;
      this.id = id;
      this.element = element;
    }

    @Override
    public E element() {
      scene.prop(id, element);
      return element;
    }
  }
}
