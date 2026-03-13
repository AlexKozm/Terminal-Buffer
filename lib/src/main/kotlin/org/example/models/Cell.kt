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
 */
data class Cell(
    val char: Char,
    val attributes: Attributes = Attributes()
) {
    companion object {
        val EMPTY_CELL = Cell(' ')
    }
}