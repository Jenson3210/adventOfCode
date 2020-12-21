fun List<List<*>>.commonObjects(): List<*> {
    val list = this.first().toMutableList()
    for (l in this) {
        list.removeIf { !list.intersect(l).contains(it) }
    }
    return list
}