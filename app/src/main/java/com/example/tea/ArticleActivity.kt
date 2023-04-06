package com.example.tea

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tea.api.Api

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val arguments = intent.extras
        val id = arguments!!["id"].toString()

        val backBtn : Button = findViewById(R.id.back_from_article_button);

        getArticle(id)

        backBtn.setOnClickListener {
            finish()
        }

    }

    fun getArticle(id : String){
        val  api : Api = Api(this)
        val article = api.getArticle(id)

        val theme : TextView = findViewById(R.id.article_theme)
        val text : TextView = findViewById(R.id.article_text)
        val date : TextView = findViewById(R.id.article_date)
        val image : ImageView = findViewById(R.id.article_image)

        if (article != null) {
            theme.text = article.title
            text.text = article.description
            date.text = article.dateOfPublication
            image.setImageBitmap(convert(article.photo))
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
}