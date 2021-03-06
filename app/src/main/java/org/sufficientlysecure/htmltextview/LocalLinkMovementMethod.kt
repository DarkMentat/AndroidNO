/*
 * Copyright (C) 2015 Heliangwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sufficientlysecure.htmltextview

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView


//hack to allow customize behavior on link click

class ClickableLocalLinkMovementMethod(
        private val onClick: (View, URLSpan) -> Unit
) : LinkMovementMethod() {

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            val link = buffer.getSpans(off, off, URLSpan::class.java)

            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    onClick(widget, link[0])

                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]))
                }

                if (widget is HtmlTextView) {
                    widget.linkHit = true
                }
                return true
            } else {
                Selection.removeSelection(buffer)
                Touch.onTouchEvent(widget, buffer, event)
                return false
            }
        }
        return Touch.onTouchEvent(widget, buffer, event)
    }
}
