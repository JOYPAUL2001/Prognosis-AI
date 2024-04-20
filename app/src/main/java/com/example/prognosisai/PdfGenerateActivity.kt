package com.example.prognosisai

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prognosisai.Adapter.PdfDocumentAdapter
import com.example.prognosisai.databinding.ActivityPdfGenerateBinding
import com.example.prognosisai.di.Common
import com.example.prognosisai.utils.TokenManager
import com.google.firebase.database.DatabaseReference
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class PdfGenerateActivity : AppCompatActivity() {

    var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var file_name = "prognosis_AI_$timestamp.pdf"

    private lateinit var binding: ActivityPdfGenerateBinding

    @Inject
    lateinit var providesRealTimeDatabaseInstance: DatabaseReference

    @Inject
    lateinit var tokenManager : TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        if(bundle!=null){
            binding.pdfName.text = bundle.getString("Patient Name")
            binding.pdfAge.text = bundle.getString("Age")
            binding.pdfDOB.text = bundle.getString("Date Of Birth")
            binding.pdfResult.text = bundle.getString("Prediction Type")
            binding.pdflocation.text = bundle.getString("Location")
            binding.pdfID.text = bundle.getString("ID")
            binding.pdfGender.text = bundle.getString("Gender")
        }
        else{
            Toast.makeText(this@PdfGenerateActivity,"Failed to fetch Details",Toast.LENGTH_SHORT).show()
        }


        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    binding.createPDF.setOnClickListener {
                        createPDFFile(Common.getAppPath(this@PdfGenerateActivity)+file_name)
                    }
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                   // Toast.makeText(this@PdfGenerateActivity,"Failure3",Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                   // Toast.makeText(this@PdfGenerateActivity,"Failure4",Toast.LENGTH_SHORT).show()
                }

            })
            .check()



    }

    private fun createPDFFile(path: String) {
        if(File(path).exists()){
            File(path).delete()
           // Toast.makeText(this@PdfGenerateActivity,"Failure1",Toast.LENGTH_SHORT).show()
        }
        try {
           // Toast.makeText(this@PdfGenerateActivity,"Failure2",Toast.LENGTH_SHORT).show()
            val document = Document()

            PdfWriter.getInstance(document, FileOutputStream(path))

            Log.d("Joydeep deep", "createPDFFile: $path.message")

            document.open()





            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Prognosis AI")
            document.addCreator("A11")

            val colorAccent = BaseColor(0,153,204,255)
            val headingFontSize = 20.0f
//            val valueFontSize = 15.0f

            val fontName = BaseFont.createFont("res/font/brandon_medium.otf","UTF-8", BaseFont.EMBEDDED)


            val titleStyle = Font(fontName,40.0f, Font.NORMAL,BaseColor.BLACK)
            val subTitle = Font(fontName,25.0f,Font.NORMAL,BaseColor.BLACK)
//            val valueStyle = Font(fontName,headingFontSize,Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName,headingFontSize,Font.NORMAL,BaseColor.BLACK)
            val contentStyle = Font(fontName,headingFontSize,Font.NORMAL,colorAccent)


            addNewItem(document,"Prognosis AI", Element.ALIGN_CENTER,titleStyle)
            document.add(Paragraph("\n\n"))

            addNewItem(document,tokenManager.getHosName("Pdf_HosName").toString(), Element.ALIGN_CENTER,subTitle)
            document.add(Paragraph("\n\n\n"))

            addLineSeparator(document)


            addNewItemWithLeftAndRight(document,"Patient Name",binding.pdfName.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"Predicted Type",binding.pdfResult.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"Date Of Birth",binding.pdfDOB.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"Age",binding.pdfAge.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"Gender",binding.pdfGender.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"ID",binding.pdfID.text.toString(),headingStyle,contentStyle)
            addLineSeparator(document)
            addNewItemWithLeftAndRight(document,"Residence",binding.pdflocation.text.toString(),headingStyle,contentStyle)

            document.close()

            Toast.makeText(this@PdfGenerateActivity,"Successfully Generated!",Toast.LENGTH_LONG).show()

            printPDF()

        }catch (e: Exception){
            Log.d("Joydeep Paul", "createPDFFile: $e.message")

            Toast.makeText(this@PdfGenerateActivity,"Failure",Toast.LENGTH_SHORT).show()
        }

    }
    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
            val printAdapter = PdfDocumentAdapter(this@PdfGenerateActivity,Common.getAppPath(this@PdfGenerateActivity)+file_name)
            printManager.print("Document",printAdapter, PrintAttributes.Builder().build())

        }catch (e: Exception){
            Log.e("Deep",""+e.message)
        }
    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(
        document: Document,
        textLeft: String,
        textRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {

        val chunkTextLeft = Chunk(textLeft,leftStyle)
        val chunkTextRight = Chunk(textRight,rightStyle)

        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)

    }

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,64)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)

    }
    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, alignCenter: Int, style: Font) {
        val chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = alignCenter
        document.add(p)
    }
}