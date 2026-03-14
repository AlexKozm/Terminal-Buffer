package org.example.scrollback

import org.example.models.Cell
import org.example.models.Line
import org.example.models.ScrolledLines
import org.example.screen.joinToStr
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal fun lineOf(len: Int, char: Char) = (1..len).map { Cell(char) }.let { Line(it) }

class MutableScrollBackImplTest {
    @Test
    fun `append less than max amount of lines`() {
        val scrollback = MutableScrollBackImpl(3, 4)
        val toAppend = (1..3 * 2)
            .map { Cell('o') }
            .chunked(3).map { Line(it) }
            .let { ScrolledLines(it) }

        scrollback.append(toAppend)
        val res = scrollback.content.joinToStr()
        assertEquals("o".repeat(3 * 2), res)
    }

    @Test
    fun `append full scrollback`() {
        val scrollback = MutableScrollBackImpl(3, 4)
        val toAppend = (1..3 * 3)
            .map { Cell('o') }
            .chunked(3).map { Line(it) }
            .let { ScrolledLines(it) }

        scrollback.append(toAppend)
        val res = scrollback.content.joinToStr()
        assertEquals("o".repeat(3 * 3), res)
    }

    @Test
    fun `move some existed lines out`() {
        val scrollback = MutableScrollBackImpl(3, 4)
        val fill = (1..3).map { lineOf(3, '1') }.let { ScrolledLines(it) }
        scrollback.append(fill)

        val toAppend = (1..3).map { lineOf(3, '2') }.let { ScrolledLines(it) }
        scrollback.append(toAppend)

        val res = scrollback.content.joinToStr()
        assertEquals("1".repeat(3) + "2".repeat(3 * 3), res)
    }

    @Test
    fun `write more than the size of the scrollback`() {
        val scrollback = MutableScrollBackImpl(3, 4)
        val fill = (1..3).map { lineOf(3, '1') }.let { ScrolledLines(it) }
        scrollback.append(fill)

        val toAppend = ((1..3).map { lineOf(3, '2') } +
                        (1..3).map { lineOf(3, '3') }).let { ScrolledLines(it) }
        scrollback.append(toAppend)

        val res = scrollback.content.joinToStr()
        assertEquals("2".repeat(3) + "3".repeat(3 * 3), res)
    }

    @Test
    fun `char at`() {
        val scrollback = MutableScrollBackImpl(2, 3)
        val fill = (0..< 2*3)
            .map { Cell(it.toString()[0]) }
            .chunked(3)
            .map { Line(it) }
            .let { ScrolledLines(it) }

        scrollback.append(fill)

        assertEquals(Cell('5'), scrollback.cellAt(1, 2))
        assertEquals(Cell.Empty, scrollback.cellAt(5, 10))
    }
}