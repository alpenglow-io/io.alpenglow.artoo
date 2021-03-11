package io.artoo.frost.ui;

import com.dropbox.core.v2.files.FolderMetadata;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import io.artoo.frost.dropbox.Dropbox;
import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.Scene;
import io.artoo.frost.scene.element.Section;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;

public final class Folders implements Section {
  private final Dropbox dropbox;
  private final Scene scene;

  public static Section foldersOf(Dropbox dropbox, Scene scene) {
    return new Folders(dropbox, scene);
  }

  private Folders(final Dropbox dropbox, final Scene scene) {
    this.dropbox = dropbox;
    this.scene = scene;
  }

  @Override
  public <R> R let(final Func.Uni<? super Panel, ? extends R> func) {
    return scene.sectionVertical(withButtons()).let(func);
  }

  private Many<Element<? extends Component>> withButtons() {
    return dropbox
      .foldersOf("/docs")
      .select(FolderMetadata::getName)
      .select(String::trim)
      .order()
      .select(folder -> scene.button(folder));
  }
}
