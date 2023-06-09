package re.artoo.fxcalibur.ui;

import javafx.css.Styleable;
import javafx.scene.paint.Color;

public sealed interface Attribute<ELEMENT extends Styleable> {
  ELEMENT apply(ELEMENT element);


  enum color implements Attribute<Styleable> { primary(Color.rgb(0, 114, 245)), secondary(Color.web(""));
    private final Color color;

    color(Color color) {
      this.color = color;
    }

    @Override
    public Styleable apply(Styleable styleable) {
      return null;
    }
  }
}
