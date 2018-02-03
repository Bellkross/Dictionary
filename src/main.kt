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
            if(!key.isEmpty()) out.write("$key -> ${dictionary.coordinateInvertedIndex[key].toString()}\n")
        }
    }

//    println( dictionary.find("Харриман уже двенадцать лет возглавлявший Исследовательский центр", 1) )

}