import java.util.*

class Helper {

    fun permutationIndex(inputString : String) : MutableList<String>{
        var string = "$inputString$"
        var i = string.length
        val result : MutableList<String> = LinkedList()
        while (--i >= 0) {
            result.add(string)
            string += string[0]
            string = string.substring(1)
        }

        //hello -> hello$, ello$h, llo$he, lo$hel, o$hell, $hello
        return result
    }

    fun changeJoker(inputString: String) : String {
        var string = "$$inputString"
        while (string[string.length-1] !='*'){
            string += string[0]
            string = string.substring(1)
        }

        return string
    }

    //for normal words
    fun kgramIndex(inputString: String, k : Int) : MutableList<String> {
        var string = "$$inputString$"
        var i = string.length
        val result : MutableList<String> = LinkedList()
        while (--i >= k-1) {
            result.add(string.substring(0,k))
            string = string.substring(1)
        }

        //castle -> $ca, cas, ast, stl, tle, le$
        return result
    }

    //for non-ended words
    fun kgramIndex2(inputString: String, k : Int) : MutableList<String> {
        var string = "$$inputString"
        var i = string.length
        val result : MutableList<String> = LinkedList()
        while (--i >= k-1) {
            result.add(string.substring(0,k))
            string = string.substring(1)
        }

        return result
    }

    //for non-started words
    fun kgramIndex3(inputString: String, k : Int) : MutableList<String> {
        var string = "$inputString$"
        var i = string.length
        val result : MutableList<String> = LinkedList()
        while (--i >= k-1) {
            result.add(string.substring(0,k))
            string = string.substring(1)
        }

        return result
    }

}