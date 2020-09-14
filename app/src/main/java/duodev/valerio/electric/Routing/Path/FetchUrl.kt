package duodev.valerio.electric.Routing.Path

import android.os.AsyncTask
import android.util.Log
import duodev.valerio.electric.Routing.RoutingFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class FetchURL(var mContext: RoutingFragment) :
    AsyncTask<String?, Void?, String>() {
    var directionMode = "driving"

    override fun doInBackground(vararg strings: String?): String? {
        // For storing data from web service
        var data = ""
        directionMode = "driving"
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0].toString())
//            Log.d("mylog", "Background task data $data")
        } catch (e: Exception) {
            Log.d("Background Task", e.toString())
        }
        return data
    }

    override fun onPostExecute(s: String) {
        super.onPostExecute(s)
        val parserTask = PointsParser(mContext, directionMode)
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s)
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection
            // Connecting to url
            urlConnection.connect()
            // Reading data from url
            iStream = urlConnection!!.inputStream
            val br =
                BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
//            Log.d("mylog", "Downloaded URL: $data")
            br.close()
        } catch (e: Exception) {
//            Log.d("mylog", "Exception downloading URL: $e")
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }
}