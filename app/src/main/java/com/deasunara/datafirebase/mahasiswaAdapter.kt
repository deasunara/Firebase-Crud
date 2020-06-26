package com.deasunara.datafirebase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class mahasiswaAdapter(val mCtx : Context, val layoutResId : Int, val mhsList :List<Mahasiswa>) :ArrayAdapter<Mahasiswa> (mCtx, layoutResId, mhsList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val  layoutInFlater : LayoutInflater = LayoutInflater.from(mCtx)

        val view : View = layoutInFlater.inflate(layoutResId, null)

        val tvNama : TextView = view.findViewById(R.id.tvNama)
        val tvAlamat : TextView = view.findViewById(R.id.tvAlamat)
        val tvEdit : TextView = view.findViewById(R.id.tvEdit)


        val mahasiswa : Mahasiswa = mhsList[position]
        tvEdit.setOnClickListener {
            showUpdateDialog(mahasiswa)
        }


        tvNama.text = mahasiswa.nama
        tvAlamat.text = mahasiswa.alamat


        return view
    }
    fun showUpdateDialog(mahasiswa: Mahasiswa) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("EditData")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val etNama = view.findViewById<EditText>(R.id.etNama)
        val etAlamat = view.findViewById<EditText>(R.id.etAlamat)

        etNama.setText(mahasiswa.nama)
        etAlamat.setText(mahasiswa.alamat)

        builder.setView(view)

        builder.setPositiveButton("Update"){ p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance().getReference("mahasiswa")

            val nama : String = etNama.text.toString().trim()
            val alamat :String = etAlamat.text.toString().trim()
            if (nama.isEmpty()){
                etNama.error="Mohon nama di isi"
                etNama.requestFocus()
                return@setPositiveButton
            }
            if (alamat.isEmpty()){
                etAlamat.error="Mohon nama di isi"
                etAlamat.requestFocus()
                return@setPositiveButton

            }
            val mahasiswa = Mahasiswa (mahasiswa.id, nama, alamat)

            dbMhs.child(mahasiswa.id!!).setValue(mahasiswa)

            Toast.makeText(mCtx, "Data berhasil di update", Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("no"){p0,p1 ->

        }
        builder.setNeutralButton("DELETE"){p0,p1 ->
            val dbMhs =FirebaseDatabase.getInstance().getReference("mahasiswa").child(mahasiswa.id)

            dbMhs.removeValue()

            Toast.makeText(mCtx, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

        }
        val  alert = builder.create()
        alert.show()
    }
}