package oak.desktop.component;

import javafx.scene.Parent;

interface Source<P extends Parent> extends Component {
  P element();

  @Override
  default Parent get() {
    return element();
  }
}
