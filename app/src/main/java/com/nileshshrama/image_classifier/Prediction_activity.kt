package com.nileshshrama.image_classifier

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.nileshshrama.image_classifier.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class Prediction_activity : AppCompatActivity() {
        lateinit var bitmap:Bitmap
        lateinit var image:ImageView
        lateinit var sltImg:Button
        lateinit var shwPred:Button
        lateinit var clsBtn:Button
        lateinit var pred:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)
        sltImg=findViewById(R.id.selectImg)
        shwPred=findViewById(R.id.showPredbtn)
        clsBtn=findViewById(R.id.closebtn)
        image=findViewById(R.id.image_view)
        pred=findViewById(R.id.predict)

        var labels=application.assets.open("labels.txt").bufferedReader().readLines()

        var imageProcessor=ImageProcessor.Builder()
            .add(ResizeOp(224,224,ResizeOp.ResizeMethod.BILINEAR)).build()



        sltImg.setOnClickListener {
            var intent: Intent =Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,100)
        }
        shwPred.setOnClickListener {
            if (image.getDrawable() == null) {
                Toast.makeText(this, "select the image", Toast.LENGTH_SHORT).show()
            } else {
                var tensorimage = TensorImage(DataType.UINT8)
                tensorimage.load(bitmap)
                tensorimage = imageProcessor.process(tensorimage)
                val model = MobilenetV110224Quant.newInstance(this)
                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputFeature0.loadBuffer(tensorimage.buffer)
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
                var maxindx = 0
                outputFeature0.forEachIndexed { index, fl ->
                    if (outputFeature0[maxindx] < fl) {
                        maxindx = index
                    }
                }
                pred.setText(labels[maxindx])
                model.close()
            }

            clsBtn.setOnClickListener {
                this.finish()
                System.exit(0)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode==100)
        {
            var uri=data?.data
            bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
            image.setImageBitmap(bitmap)

        }

    }
}