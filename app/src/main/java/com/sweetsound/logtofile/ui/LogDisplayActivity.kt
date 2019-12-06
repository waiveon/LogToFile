package com.sweetsound.logtofile.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sweetsound.logtofile.LogToFile
import com.sweetsound.logtofile.R
import kotlinx.android.synthetic.main.activity_log_display.*

class LogDisplayActivity(): Activity() {
    private lateinit var mLogToFile: LogToFile

    companion object {
        lateinit var mFilePath: String

        fun open(context: Context, filePath: String?) {
            context.startActivity(Intent(context, LogDisplayActivity::class.java))


            mFilePath = filePath ?: context.getFilesDir().getAbsolutePath()
        }

        fun open(context: Context) {
            open(context, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_log_display)

        mLogToFile = LogToFile(mFilePath)

        load_button.setOnClickListener {
            val fileNameList = mLogToFile.getFileNameList();

            if (fileNameList.size > 0) {
                val alertBuilder = AlertDialog.Builder(this)

                alertBuilder.setTitle(getString(R.string.file_list))
                alertBuilder.setNegativeButton(R.string.cancel, null)
                alertBuilder.setSingleChoiceItems(fileNameList.toTypedArray(), -1, DialogInterface.OnClickListener { dialog, which ->
                    log_textview.text = mLogToFile.read(fileNameList.get(which))
                    dialog.dismiss()
                })

                alertBuilder.show()
            } else {
                Toast.makeText(this, R.string.not_exist_file, Toast.LENGTH_SHORT).show()
            }
        }

        del_button.setOnClickListener {
            val fileNameList = mLogToFile.getFileNameList();

            if (fileNameList.size > 0) {
                val checkedList = List<Boolean>(fileNameList.size) {false}.toTypedArray()
                val alertBuilder = AlertDialog.Builder(this)

                alertBuilder.setTitle(getString(R.string.file_list))
                alertBuilder.setNegativeButton(R.string.cancel, null)
                alertBuilder.setMultiChoiceItems(fileNameList.toTypedArray(), null, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    checkedList[which] = isChecked
                })

                alertBuilder.setPositiveButton(R.string.del) {
                        dialog, which ->

                    checkedList.forEachIndexed { index, isChecked ->
                        if (isChecked == true) {
                            mLogToFile.delete(fileNameList.get(index))
                            log_textview.text = ""
                        }
                    }

                    dialog.dismiss()
                }

                alertBuilder.show()
            } else {
                Toast.makeText(this, R.string.not_exist_file, Toast.LENGTH_SHORT).show()
            }
        }
    }
}