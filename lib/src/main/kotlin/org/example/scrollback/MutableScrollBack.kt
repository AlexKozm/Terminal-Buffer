package org.example.scrollback

import org.example.models.ScrolledLines

internal interface MutableScrollBack : ScrollBack {
    fun append(list: ScrolledLines)
}