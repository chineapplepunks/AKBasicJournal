package com.akb.journal.entity

class EditHistory {
    var position = 0
    var history = ArrayList<EditHistoryItem>()

    fun add(item: EditHistoryItem) {
        while (history.size > position) {
            history.removeLast()
        }
        if(item.after.toString() == item.before.toString() && history.isNotEmpty()) {
            history.removeLast()
            position--
        }
        history.add(item)
        position++
    }

    fun getPrevious(): EditHistoryItem? {
        return if(position < 1) {
            null
        } else {
            position--
            history[position]
        }
    }

    fun getNext(): EditHistoryItem? {
        return if(position >= history.size) {
            null
        } else {
            val item = history[position]
            position++
            return item
        }
    }

    fun reset() {
        history.clear()
        position = 0
    }
}