package org.example.models


/*
TODO: research what ERASED state means. Adjust this type.
Maybe erased cell has no attributes,
maybe we could represent erased cell with space.

For now I will space to simplify cursor movements.

Some sources:

https://ecma-international.org/wp-content/uploads/ECMA-48_5th_edition_june_1991.pdf
6.1.1 Presentation component
...
Depending on the implementation, there may or may not be a distinction between a character position in
the erased state and a character position imaging SPACE.

================
Testing `echo -e "\u001B[41m\u001B[5B\u001B[6Caaa\u001B[6Da"` in kde konsole
shows, that probable an erased sell is a cell without char and attributes
 */
sealed interface SomeCell
data class Cell(
    val char: Char,
    val attributes: Attributes = Attributes()
): SomeCell {
    object Empty: SomeCell
}