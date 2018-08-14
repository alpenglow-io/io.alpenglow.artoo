open module oak.desktop {
  requires oak.base;

  requires java.desktop;
  requires javafx.controls;
  requires javafx.graphics;
  requires controlsfx;

  exports oak.desktop.component;
  exports oak.desktop.component.grid;
  exports oak.desktop.event;
  exports oak.desktop.property;
}
