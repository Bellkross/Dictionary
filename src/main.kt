import java.io.File
import java.time.LocalTime

const val mFilepath = "D:\\IntelIJ\\txtFiles"
val files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

val helper = Helper()

/*

All txt files -> 215 MB
InvertedIndexes -> 25 MB
Result -> 14 451 KB
Time -> 9501 sec(2.63 h)


file fileII.txt and 0_0outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 0_11outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 11_20outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 20_44outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 44_60outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 60_81outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and 81_128outputInvertedIndex.txt merged into fileII.txt
file fileII.txt and last_outputInvertedIndex.txt merged into fileII.txt
Time -> 9501 sec
 */
fun main(args: Array<String>) {

    val time1: Long = LocalTime.now().toNanoOfDay()

    //val files = indexing()

    helper.merge(File("D:\\IntelIJ\\Dictionary\\files").listFiles())

    val time2 = LocalTime.now().toNanoOfDay()

    println("Time -> ${(time2 - time1) / 1000000000} sec")

}

fun indexing(): ArrayList<File> {
    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File

    val lim = 170544104
    var j = 0

    files.forEachIndexed { i, file ->
        dictionary.addDocument(Document(file.absolutePath, i))
        //        println("free memory -> ${Runtime.getRuntime().freeMemory()}")
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
        }
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