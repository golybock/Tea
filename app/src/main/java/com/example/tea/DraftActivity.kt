package com.example.tea

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tea.api.Api
import com.example.tea.database.DatabaseHelper
import com.example.tea.models.article.Article
import com.example.tea.models.article.ArticleDomain
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DraftActivity : AppCompatActivity() {

    lateinit var article : ArticleDomain

    var imageBitmap: Bitmap? = null
    lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        val arguments = intent.extras
        val id = arguments!!["id"].toString()

        image = findViewById(R.id.article_image_draft)
        val backBtn : Button = findViewById(R.id.back_from_draft_button);
        val publish : Button = findViewById(R.id.publish_draft_button)
        val image_btn : Button = findViewById(R.id.draft_choose_image)

        getArticle(id)

        backBtn.setOnClickListener {
            finish()
        }

        image_btn.setOnClickListener {
            imagePickDialog()
        }

        publish.setOnClickListener {
            val api = Api(this)

            val theme : TextView = findViewById(R.id.article_theme)
            val text : TextView = findViewById(R.id.article_text)

            article.description = text.text.toString()
            article.title = theme.text.toString()
            val bitmap: Bitmap? =  imageBitmap

            if (bitmap != null) {
                val bos: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                val image:ByteArray = bos.toByteArray()
                val base64Encoded = java.util.Base64.getEncoder().encodeToString(image)
                bitmap.recycle()

                article.photo = base64Encoded
            }
            val res = api.createArticle(article)
            if(res){

                val db = DatabaseHelper(this, null)
                db.deleteDraft(article.id)

                Toast.makeText(this, "Опубликовано!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Какие-то данные неверны", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getArticle(id : String){
        val  db = DatabaseHelper(this, null)
        article = db.getArticle(id)

        val theme : TextView = findViewById(R.id.article_theme)
        val text : TextView = findViewById(R.id.article_text)
        val image : ImageView = findViewById(R.id.article_image_draft)

        if (article != null) {
            theme.text = article.title
            text.text = article.description
            val text = "2022-01-06 20:30:45"
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val localDateTime = LocalDateTime.parse(text, pattern)

            if(article.photo.length > 100){
                image.setImageBitmap(convert(article.photo))
            }
            else{
                image.setImageBitmap(
                    BitmapFactory.decodeResource(this.resources,
                        R.drawable.baseline_hide_image_24))
            }

        }
        else{
            theme.text = "Не удалось загрузить"
        }
    }

    @Throws(IllegalArgumentException::class)
    fun convert(base64Str: String): Bitmap? {
        val decodedBytes: ByteArray = android.util.Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            android.util.Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1888 && resultCode == RESULT_OK) {
            imageBitmap = data?.extras!!["data"] as Bitmap?
            image.setImageBitmap(imageBitmap)
        }
        if (resultCode == RESULT_OK && requestCode == 100) {
            val uri = data?.data
            if(uri!=null){
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                imageBitmap = bitmap
                image.setImageBitmap(imageBitmap)
            }

        }
    }

    private fun imagePickDialog() {
        val options = arrayOf("Камера", "Галерея")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Выберите изображение")
        builder.setItems(options) { dialogInterface, i ->
            if (i == 0) {
                if (checkPermission()) {
                    val camera = Intent("android.media.action.IMAGE_CAPTURE")
                    startActivityForResult(camera, 1888)

                } else {
                    requestPermission();
                }
            } else {
                pickFromStorage()
            }
        }
        builder.create().show()
    }

    private fun pickFromStorage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(Manifest.permission.CAMERA),
            200
        )
    }
}