package oak.desktop.property;

import javafx.scene.control.TextInputControl;

public interface InputProperty {
  static InputProperty value(String value) {
    return it -> it.setText(value);
  }

  static InputProperty prompt(String prompt) {
    return it -> it.setPromptText(prompt);
  }

  void onInput(TextInputControl input);
}
