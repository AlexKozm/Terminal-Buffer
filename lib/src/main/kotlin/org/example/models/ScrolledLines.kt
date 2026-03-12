package org.example.models

import org.example.screen.Screen
import org.example.scrollback.ScrollBack

/**
 * DTO for moving lines from [Screen] to [ScrollBack]
 */
@JvmInline
internal value class ScrolledLines(val lines: List<Line>)