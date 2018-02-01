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
        dictionary.list.forEachIndexed { i, key ->
            if(i > 0) out.write("$key -> ${dictionary.coordinateInvertedIndex[key].toString()}\n")
        }
    }

}