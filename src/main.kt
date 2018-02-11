import java.io.File

const val mFilepath = "D:\\IntelIJ\\txtFiles"
val files = File(mFilepath).listFiles()

var dictionary: Dictionary = Dictionary()

fun main(args: Array<String>) {

    files.forEachIndexed { i, file ->
        dictionary.addDocument(Document(file.absolutePath, i))
    }

    val fileCoordinateInvertedIndex = File("outputCoordinateInvertedIndex.txt")
    fileCoordinateInvertedIndex.bufferedWriter().use { out ->
        dictionary.list.forEach { key ->
            if (!key.isEmpty()) out.write("$key -> ${dictionary.coordinateInvertedIndex[key].toString()}\n")
        }
    }

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
        println(dictionary.bigramFind("Gla*s*ne"))
        println( dictionary.find("Харриман вот уже лет", 10) )
    }

}