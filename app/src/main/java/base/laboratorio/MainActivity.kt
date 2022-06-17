package base.laboratorio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var buttonPlay: Button
    private lateinit var buttonStop: Button
    private lateinit var buttonAnterior: Button
    private lateinit var buttonSiguiente: Button
    private lateinit var textView: TextView

    private var contador: Int = 1

    private lateinit var rootTree : DocumentFile

    private lateinit var mediaPlayer: MediaPlayer

    private var documentsFiles: ArrayList<DocumentFile> = arrayListOf()

    companion object {
        var OPEN_DIRECTORY_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonStop = findViewById(R.id.buttonStop)
        buttonAnterior = findViewById(R.id.buttonAtras)
        buttonSiguiente = findViewById(R.id.buttonNext)
        textView = findViewById(R.id.textView)

        mediaPlayer = MediaPlayer()

        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                var directoryUri = data?.data ?: return
                Log.e("Directory ", directoryUri.toString())
                rootTree = DocumentFile.fromTreeUri(this, directoryUri)!!

                for (file in rootTree!!.listFiles()) {

                    documentsFiles.add(file)

                }
            }
        }

        setOnClickListeners(this)
    }

    private fun setOnClickListeners(context: Context) {

        buttonPlay.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            textView.setText(documentsFiles[contador].name)
            mediaPlayer.setDataSource(this, documentsFiles[contador].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
            Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }



        buttonStop.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }

        buttonSiguiente.setOnClickListener{
            textView.setText(documentsFiles[contador++].name)
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, documentsFiles[contador++].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

        buttonAnterior.setOnClickListener {
            textView.setText(documentsFiles[contador--].name)
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, documentsFiles[contador--].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}