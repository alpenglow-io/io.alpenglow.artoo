package io.artoo.frost;

import io.artoo.frost.dropbox.Dropbox;

import static io.artoo.frost.scene.Scene.scene;
import static io.artoo.frost.ui.Folders.foldersOf;

public class FrostCli {
  public static void main(String[] args) {
    final var dropbox = Dropbox.client(System.getenv("DBX_ACCESS_TOKEN"));
    scene().get(scene ->
      scene.open(
        scene.sectionBorder(location -> location
          .top(scene.label("Something nice over here!"))
          .left(foldersOf(dropbox, scene))
          .center(scene.textArea())
          .bottom(scene.buttonClose())
        )
      )
    );
  }
}
