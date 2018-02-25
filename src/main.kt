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
    helper.merge(File("D:\\IntelIJ\\Dictionary\\index").listFiles())
    val time2 = LocalTime.now().toNanoOfDay()
    println("Time of merging -> ${(time1 - time2) / 1000000000} sec")
    //println("All time -> ${(time2 - time11) / 1000000000} sec")

}

fun indexing(): ArrayList<File> {

    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File
    var doc: Document?

    //val lim = 193937592
    //val lim = 68544104
    val lim = 68544104
    var j = 0

    files.forEachIndexed { i, file ->
        doc = Document(file.absolutePath, i)
        dictionary.addDocument(doc!!)
        if (Runtime.getRuntime().freeMemory() < lim) {
            println("free memory < $lim bytes")
            dictionary.compress()
            fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\${j}_${i}_invertedIndex.txt")
            resFiles.add(fileInvertedIndex)
            fileInvertedIndex.bufferedWriter().use { out ->
                out.write(dictionary.compressedInvertedIndexBytes.toString())
            }

            dictionary = Dictionary()
            j = i
        }
        doc = null
        println("file #$i / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")
    }

    //prev+1 & this -> word
    dictionary.compress()
    val fileCompressedII2 = File("D:\\IntelIJ\\Dictionary\\index\\last_invertedIndex.txt")
    fileCompressedII2.bufferedWriter().use { out ->
        out.write(dictionary.compressedInvertedIndexBytes.toString())
    }
    println("file last file / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")


    return resFiles

}