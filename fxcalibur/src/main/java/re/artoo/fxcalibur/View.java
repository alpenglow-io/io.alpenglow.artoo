package re.artoo.fxcalibur;

import javafx.scene.Scene;

import java.util.function.Supplier;

public interface View extends Supplier<Scene> {
  Scene render();
}
