package org.example

import org.example.models.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MutableTerminalBufferImplTest {

    @Test
    fun `cursor starts at (0, 0) after initialization`() {
        val buffer: MutableTerminalBuffer = TODO()
        assertEquals(Position(0, 0), buffer.cursor)
    }
}