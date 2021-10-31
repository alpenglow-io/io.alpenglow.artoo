package io.artoo.frost.scene.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.element.Section;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;

public interface Sectionable extends Propable {
  default Section sectionBorder(Func.MaybeUnaryOperator<Section.Border.Location> location) {
    final var section = Section.border(location);
    this.prop(section);
    return section;
  }

  @SuppressWarnings("unchecked")
  default Section sectionHorizontal(Element<? extends Component>... components) {
    final var section = Section.horizontal(components);
    this.prop(section);
    return section;
  }

  @SuppressWarnings("unchecked")
  default Section sectionVertical(Element<? extends Component>... components) {
    final var section = Section.vertical(components);
    this.prop(section);
    return section;
  }

  default Section sectionVertical(Many<Element<? extends Component>> components) {
    final var section = Section.vertical(components);
    this.prop(section);
    return section;
  }
}
