import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

object SongBook {

    private val songbook = mutableListOf<Song>()
    private val jsonString: String = File("./src/main/resources/data.json").readText(Charsets.UTF_8)

    @JvmStatic
    fun main(args: Array<String>) {

        val job = CoroutineScope(Dispatchers.IO).launch {
            loadData()
        }

        job.start()
        if (job.isCompleted) job.cancel()

        // wait...
        Thread.sleep(200)



        // examples
        // print all songs
        songbook.forEach {
            println("ID: ${it.id} ${it.tytul}")
        }

        // search by word
        val searchList = SongBook search "jezus"
        println("\nSearch:")
        searchList.forEach {
            println("ID: ${it.id} ${it.tytul}")
        }

        // search by id
        val findId: Song? = SongBook searchById 1200
        println("\nSearch ID: 1500")
        println("ID: ${findId?.id} ${findId?.tytul}")


    }

    // --------------------------------------------------------------------------------------------------------------------
    // FUNCTIONS
    // --------------------------------------------------------------------------------------------------------------------

    // load data from file
    private fun loadData() {
        try {
            if (jsonString.isNotEmpty()) {
                val listTools: Array<Song> = Gson().fromJson<Array<Song>>(jsonString, Array<Song>::class.java)
                listTools.forEach {
                    songbook.add(it)
                }
            } else {
                println("\nFile is empty")
            }
        } catch (ie: IOException) {
            println("ERROR $ie")
        } finally {

        }
    }

    // search data
    private infix fun search(data: String): MutableList<Song> {

        val searchSongs = mutableListOf<Song>()
        songbook.forEach {
            if (it.tytul.contains(data, true) || it.tekst.contains(data, true)) {
                searchSongs.add(it)
            }
        }
        return searchSongs
    }

    // search song by id
    private infix fun searchById(id: Int): Song? {

        val findSongId = songbook.filter { it.id == id }
        return if (findSongId.isNotEmpty()) {
            findSongId[0]
        } else {
            null
        }

    }


}