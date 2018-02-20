import java.io.File
import java.time.LocalTime

const val mFilepath = "D:\\IntelIJ\\txtFiles"
val files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

val helper = Helper()

fun main(args: Array<String>) {

    val time11: Long = LocalTime.now().toNanoOfDay()
    indexing()
    val time22 = LocalTime.now().toNanoOfDay()
    println("Time of indexing -> ${(time22 - time11) / 1000000000} sec")

    val time1: Long = LocalTime.now().toNanoOfDay()
    helper.merge(File("D:\\IntelIJ\\Dictionary\\files").listFiles())
    val time2 = LocalTime.now().toNanoOfDay()
    println("Time of merging -> ${(time2 - time1) / 1000000000} sec")
    println("All time -> ${(time11 - time2) / 1000000000} sec")

}

fun indexing(): ArrayList<File> {
    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File
    var doc : Document?

    val lim = 8008544104
    var j = 0

    files.forEachIndexed { i, file ->
        System.gc()
        println("file #$i / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")
        doc = Document(file.absolutePath, i)
        dictionary.addDocument(doc!!)
        if (Runtime.getRuntime().freeMemory() < lim) {
            println("free memory < $lim bytes")
            fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\files\\${j}_${i}outputInvertedIndex.txt")
            resFiles.add(fileInvertedIndex)
            fileInvertedIndex.bufferedWriter().use { out ->
                dictionary.list.forEach { key ->
                    if (!key.isEmpty()) out.write("$key -> ${dictionary.invertedIndex[key].toString()}\n")
                }
            }

            dictionary.invertedIndex.clear()
            dictionary.list.clear()
            j = i
            System.gc()
        }
        doc = null
        System.gc()
    }

    fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\files\\last_outputInvertedIndex.txt")
    resFiles.add(fileInvertedIndex)
    fileInvertedIndex.bufferedWriter().use { out ->
        dictionary.list.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.invertedIndex[key].toString()}\n")
        }
    }

    return resFiles

}