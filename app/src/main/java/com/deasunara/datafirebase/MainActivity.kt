package com.deasunara.datafirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var etNama : EditText
    private lateinit var etAlamat : EditText
    private lateinit var btnSave : Button
    private lateinit var listMhs : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var mhsList: MutableList<Mahasiswa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("mahasiswa")

        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        btnSave =findViewById(R.id.btnSave)
        listMhs =findViewById(R.id.lvMhs)
        btnSave.setOnClickListener(this)

        mhsList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    if(p0.exists()){
                        mhsList.clear()
                        for(h in p0.children){
                            val mahasiswa = h.getValue(Mahasiswa::class.java)
                            if (mahasiswa != null){
                                mhsList.add(mahasiswa)
                            }
                        }
                        val Adapter = mahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhsList)
                        listMhs.adapter = Adapter
                    }
                }
            }

        })



    }
    override fun onClick(p0: View?) {
       saveData()
    }
    private fun saveData(){
        val nama : String = etNama.text.toString().trim()
        val alamat : String = etAlamat.text.toString().trim()

        if(nama.isEmpty()){
            etNama.error="Isi Nama!"
        }

        if (alamat.isEmpty()){
            etAlamat.error="Isi Alamat!"
            return
        }


        val mhsId = ref.push().key

        val mhs = Mahasiswa(mhsId,nama,alamat)

        if (mhsId != null)

        ref.child(mhsId).setValue(mhs).addOnCompleteListener {
            etNama.text.clear()
            etAlamat.text.clear()
            Toast.makeText(applicationContext,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }

    }
}
