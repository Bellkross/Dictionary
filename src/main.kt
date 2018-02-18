import java.io.File
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val mFilepath = "D:\\IntelIJ\\txtFiles"
val files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

val helper = Helper()

fun main(args: Array<String>) {
    val resFiles = ArrayList<File>()
    var fileInvertedIndex : File


    val time1 : Long = LocalTime.now().toNanoOfDay()

    val lim = 170544104
    var j = 0

    files.forEachIndexed { i, file ->
        dictionary.addDocument(Document(file.absolutePath, i))
        //        println("free memory -> ${Runtime.getRuntime().freeMemory()}")
        if (Runtime.getRuntime().freeMemory() < lim) {
            println("free memory < $lim bytes")
            fileInvertedIndex = File("${j}_${i}outputInvertedIndex.txt")
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

    fileInvertedIndex = File("last_outputInvertedIndex.txt")
    resFiles.add(fileInvertedIndex)
    fileInvertedIndex.bufferedWriter().use { out ->
        dictionary.list.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.invertedIndex[key].toString()}\n")
        }
    }

    println("let's merge!")

    File("fileII.txt").createNewFile()
    val file = File("fileII.txt")
    resFiles.forEach {
        helper.mergeFiles2(file.path, it.path, file.path)
        println("file ${file.path} and ${it.path} merged into ${file.path}")
    }

/*
coordinated index
    val resFiles = ArrayList<File>()
    var fileCoordinateInvertedIndex : File

    val lim = 170544104
    var j = 0
    files.forEachIndexed { i, file ->
        dictionary.addDocument(Document(file.absolutePath, i))
//        println("free memory -> ${Runtime.getRuntime().freeMemory()}")
        if (Runtime.getRuntime().freeMemory() < lim) {
            println("free memory < $lim bytes")
            fileCoordinateInvertedIndex = File("${j}_${i}outputCoordinateInvertedIndex.txt")
            resFiles.add(fileCoordinateInvertedIndex)
            fileCoordinateInvertedIndex.bufferedWriter().use { out ->
                dictionary.list.forEach { key ->
                    if (!key.isEmpty()) out.write("$key -> ${dictionary.coordinateInvertedIndex[key].toString()}\n")
                }
            }
            dictionary.coordinateInvertedIndex.clear()
            dictionary.list.clear()
            j = i
        }
    }


    fileCoordinateInvertedIndex = File("outputCoordinateInvertedIndex.txt")
    resFiles.add(fileCoordinateInvertedIndex)
    fileCoordinateInvertedIndex.bufferedWriter().use { out ->
        dictionary.list.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.coordinateInvertedIndex[key].toString()}\n")
        }
    }
    println("let's merge!")

    File("file.txt").createNewFile()
    val file = File("file.txt")
    resFiles.forEach {
        helper.mergeFiles(file.path, it.path, file.path)
        println("file ${file.path} and ${it.path} merged into ${file.path}")
    }
*/

    val time2 = LocalTime.now().toNanoOfDay()

    println("Time -> ${(time2 - time1)/1000000000} sec")
    /*
    val filePermutationIndex = File("outputPermutationIndex.txt")
    filePermutationIndex.bufferedWriter().use { out ->
        dictionary.list.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.permutationIndex[key].toString()}\n")
        }
    }

    val fileTrigramIndex = File("outputTrigramIndex.txt")
    fileTrigramIndex.bufferedWriter().use { out ->
        dictionary.trigramList.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.trigramIndex[key].toString()}\n")
        }
    }

    val fileTree = File("outputTree.txt")
    fileTree.bufferedWriter().use { out ->
        out.write("${dictionary.tree}")
    }

    run {
        println(dictionary.trigramFind("Gla*st*ne"))
        println(dictionary.bigramFind("Gla*e"))
        println( dictionary.find("Харриман вот уже лет", 10) )
        /*
        [$Gl, Gla, ne$]
        {9=[48966, 49649]}
        [$G, Gl, la, e$]
        {9=[48966, 49649]}
        {2=[28]}
         */
    }
    */

}