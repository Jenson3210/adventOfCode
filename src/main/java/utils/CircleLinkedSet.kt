package utils

class CircleLinkedSet<T>(): MutableCollection<T> {
    constructor(initial: Collection<T>): this(){
        addAll(initial)
    }

    /**
     * [previous] and [next] are null if this item is not in the circle (yet)
     */
    private class CircleLinkedSetItem<T>(val value: T, var previous: CircleLinkedSetItem<T>? = null, var next: CircleLinkedSetItem<T>? = null){
        fun toList(): List<T>{
            val  ll = ArrayList(listOf(value))
            var current = next
            while (current !== this && current != null){
                ll.add(current.value)
                current = current.next
            }
            return ll
        }
    }

    private val currentItems = HashMap<T, CircleLinkedSetItem<T>>()

    private var top: CircleLinkedSetItem<T>? = null

    /**
     * Return a link of [items], last link ends with next = null
     */
    private fun linkItems(items: List<T>): CircleLinkedSetItem<T>?{
        if (items.isEmpty()) return null
        val first = CircleLinkedSetItem(items.first())
        var current = first
        items.drop(1).forEach{
            CircleLinkedSetItem(it).also{ item ->
                current.next = item
                item.previous = current
                current = item
            }
        }
        return first
    }

    override val size
        get() = currentItems.keys.size

    override fun isEmpty() = top == null

    /**
     * Add an item to the end of the circle (ie. before top)
     * NOT THREAD SAFE
     */
    override fun add(element: T): Boolean {
        if (element in currentItems.keys) return false
        currentItems[element] = CircleLinkedSetItem(element).apply {
            if (top == null) {
                previous = this
                next = this
                top = this
            } else {
                previous = top!!.previous!!.also{ it.next = this }
                next = top!!.also { it.previous = this }
            }
        }
        return true
    }
    override fun addAll(elements: Collection<T>): Boolean {
        val newItems = elements.filter {it !in currentItems.keys}
        val itemsToAdd = newItems.map{CircleLinkedSetItem(it)}
        itemsToAdd.forEachIndexed { index, item ->
            item.previous = itemsToAdd.getOrNull(index-1)
            item.next = itemsToAdd.getOrNull(index+1)
            currentItems[newItems[index]] = item
        }

        if (top == null){
            top = itemsToAdd.first().also{ first ->
                first.previous = itemsToAdd.last().also {last -> last.next = first}
            }
        }
        return newItems.isNotEmpty()
    }

    fun insertAfter(target: T, itemsToInsert:List<T>){
        val before = currentItems[target] ?: throw(NoSuchElementException("Item $target not found in $this"))
        val after = before.next!!
        linkItems(itemsToInsert)?.let{ first ->
            var current = first
            currentItems[current.value] = current
            before.next = first.also{
                it.previous = before
            }
            while (current.next != null){
                current = current.next!!
                currentItems[current.value] = current
            }
            current.next = after.also{
                it.previous = current
            }
        }
    }

    /**
     * Remove the first [amount] items and return them as a List
     * If trying to remove more items than are in the circle, things may go wrong. (not checked)
     * @return the removed items as List
     */
    fun pop(amount: Int): List<T>{
        if (amount == 0 || top == null) error("Don't cut an empty list plz")
        val first = top!!
        var current = first
        repeat(amount-1){
            currentItems.remove(current.value)
            current = current.next!!
        }
        currentItems.remove(current.value)
        top = current.next
        current.next = null
        top?.previous = first.previous.also{ it?.next = top }

        return first.toList()
    }

    fun rotate(): CircleLinkedSet<T>{
        top = top?.next
        return this
    }

    fun rotateTo(target: T): CircleLinkedSet<T>{
        top = currentItems[target] ?: throw(NoSuchElementException("Item $target not found in $this"))
        return this
    }

    fun toList(): List<T> = top?.toList() ?: emptyList()

    fun first() = top!!.value
    fun firstOrNull() = top?.value

    fun last() = top!!.previous!!.value
    fun lastOrNull() = top?.previous?.value

    fun take(n: Int): List<T>{
        val result = ArrayList<T>(n)
        top?.let{ t ->
            result.add(t.value)
            var current = t.next!!
            while (current !== t){
                result.add(current.value)
                current = current.next!!
                if (result.size == n) return result
            }
        } ?: return emptyList()
        return result
    }

    fun takeLast(n: Int): List<T>{
        val result = ArrayList<T>(n)
        top?.let{ t ->
            var current = t.previous!!
            while (current !== t){
                result.add(current.value)
                current = current.previous!!
                if (result.size == n) return result.reversed()
            }
        } ?: return emptyList()
        return result.reversed()
    }

    fun takeFrom(pos: T, n: Int, offset: Int = 0): List<T>{
        val result = ArrayList<T>(n)
        currentItems[pos]?.let{ cp ->
            var t = cp
            repeat(offset){
                t = t.previous!!
            }
            result.add(t.value)
            var current = t.next!!
            while (current !== t){
                result.add(current.value)
                current = current.next!!
                if (result.size == n) return result
            }
        } ?: return emptyList()
        return result
    }

    override fun contains(element: T): Boolean =
        element in currentItems.keys

    override fun containsAll(elements: Collection<T>): Boolean =
        elements.all{ it in currentItems.keys }

    override fun iterator(): MutableIterator<T> = object: MutableIterator<T>{
        var done = false
        var current = top

        override fun hasNext(): Boolean =
            current == null || done

        override fun next(): T = current!!.value.also{
            if (current!!.next == top) done = true
            else current = current!!.next
        }

        override fun remove() {
            current!!.let{ c ->
                val next = c.next!!
                next.previous = c.previous!!.also{ p ->
                    p.next = next
                }
                currentItems.remove(c.value)
            }
        }
    }

    override fun clear() {
        currentItems.clear()
        top = null
    }

    override fun remove(element: T): Boolean {
        val c = currentItems[element] ?: return false
        c.previous!!.let{ p ->
            p.next = c.next!!.also{ n ->
                n.previous = p
            }
        }
        currentItems.remove(c.value)
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean =
        elements.map{
            remove(it)
        }.any()


    override fun retainAll(elements: Collection<T>): Boolean =
        currentItems.keys.filter{ it !in elements}.map{
            remove(it)
        }.any()
}