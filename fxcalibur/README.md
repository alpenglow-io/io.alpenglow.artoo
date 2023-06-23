```java
var size = Bind.button(size::medium);

Application app = () -> 
  window(background.transparent, border.radius(14), window.size.half_screen, font.load(""),
    grid.container(gutter.gap(2),
      grid(
        button.primary(button.size.bind(size), value("Primary"))
      ),
      grid(
        button.secondary(size.bind(size), value.text("Secondary"), mouse.clicked(size::large)))
      ),
      grid(() ->
        text.h1(font.family(""), font.size(12), font.ligature(true), value())
      ),
      grid(
        button.success(size.auto,
          $(
            loading.circle(color.white),
            loading.circle(color.white),
            loading.circle(color.white)
          )
        )
    )
  )
  
```
