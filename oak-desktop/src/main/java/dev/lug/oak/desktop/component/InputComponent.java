package dev.lug.oak.desktop.component;

import dev.lug.oak.desktop.property.IdProperty;
import dev.lug.oak.desktop.property.InputProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface InputComponent extends Component {
  static InputComponent textField(IdProperty id, InputProperty... properties) {
    return new InputComponentImpl<>(new ParentComponentImpl<>(TextField::new, id), properties);
  }

  static InputComponent passwordField(IdProperty id, InputProperty... properties) {
    return new InputComponentImpl<>(new ParentComponentImpl<>(PasswordField::new, id), properties);
  }

  static InputComponent textArea(IdProperty id, InputProperty... properties) {
    return new InputComponentImpl<>(new ParentComponentImpl<>(TextArea::new, id), properties);
  }
}
