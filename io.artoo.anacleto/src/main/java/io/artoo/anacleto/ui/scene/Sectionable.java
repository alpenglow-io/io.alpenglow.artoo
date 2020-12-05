package io.artoo.anacleto.ui.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.anacleto.ui.Element;
import io.artoo.anacleto.ui.element.Section;
import io.artoo.lance.func.Func;

public interface Sectionable extends Propable {
  default Section sectionBorder(Func.Unary<Section.Border.Location> location) {
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
}
