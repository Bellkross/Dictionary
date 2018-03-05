import java.io.File
import java.time.LocalTime
import java.util.TreeSet

const val mFilepath = "D:\\IntelIJ\\test\\"
var files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

val helper = Helper()

/*
Collection -> 4,83 GB
Size of inverted index -> 114 MB
Size of dictionary -> 2,4 GB
Size of pointers -> 1,05 GB
Together -> 3,57 GB
Time of indexing -> 5515 sec (1h32m)
Time of merging+compressing -> 3237 sec (54 min)
All time -> 2h 26m
 */
fun main(args: Array<String>) {


    while (!helper.check(files)) {
        helper.split(files, mFilepath)
        files = File(mFilepath).listFiles()
    }
    val time11: Long = LocalTime.now().toNanoOfDay()
    indexing()
    val time22 = LocalTime.now().toNanoOfDay()
    println("Time of indexing -> ${(time22 - time11) / 1000000000} sec")


    val time1: Long = LocalTime.now().toNanoOfDay()
    helper.mergeBytes(File("D:\\IntelIJ\\Dictionary\\index").listFiles())
    val time2 = LocalTime.now().toNanoOfDay()
    println("Time of merging -> ${(time2 - time1) / 1000000000} sec")
    //println("All time -> ${(time2 - time11) / 1000000000} sec")

}

fun indexing(): ArrayList<File> {

    val resFiles = ArrayList<File>()
    var fileInvertedIndex: File
    var doc: Document

    val lim = 5049619648

    var j = 0
    var count = 0

    files.forEachIndexed { i, file ->
        doc = Document(file.absolutePath, i)
        dictionary.addDocument(doc)
        if (Runtime.getRuntime().freeMemory() < lim||++count>100) {
            println("free memory < $lim bytes or files > 100")
            fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\${j}_${i}_invertedIndex.txt")
            resFiles.add(fileInvertedIndex)
            fileInvertedIndex.bufferedWriter().use { out ->
                dictionary.list = TreeSet<String>(dictionary.list).toMutableList()
                dictionary.list.forEach {
                    out.write("$it -> ${dictionary.invertedIndex[it]}\n")
                }
            }

            count = 0
            dictionary.invertedIndex.clear()
            dictionary.list.clear()
            j = i
        }
        println("file #$i / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")
        System.gc()
    }

    //prev+1 & this -> word
    fileInvertedIndex = File("D:\\IntelIJ\\Dictionary\\index\\last_invertedIndex.txt")
    fileInvertedIndex.bufferedWriter().use { out ->
        dictionary.list = TreeSet<String>(dictionary.list).toMutableList()
        dictionary.list.forEach {
            out.write("$it -> ${dictionary.invertedIndex[it]}\n")
        }
    }
    dictionary.invertedIndex.clear()
    dictionary.list.clear()
    println("last file / ${files.size} indexed, memory -> ${Runtime.getRuntime().freeMemory()}")


    return resFiles

}