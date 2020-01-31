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

        // print all songs
        songbook.forEach {
            println("ID: ${it.id} ${it.tytul}")
        }

    }

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


}