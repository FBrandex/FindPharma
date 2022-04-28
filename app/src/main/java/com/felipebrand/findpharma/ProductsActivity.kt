package com.felipebrand.findpharma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.felipebrand.findpharma.Adapter
import com.felipebrand.findpharma.mapas.Buscador
import com.felipebrand.findpharma.mapas.Farmacia
import com.felipebrand.findpharma.mapas.Produto
import com.google.firebase.database.*

class ProductsActivity : AppCompatActivity() {
    private lateinit var loja:Farmacia
    private lateinit var loja_lista:List<Farmacia?>
    private lateinit var buscador:List<Buscador>
    private var distancia = 0
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private var lista_prod:List<Produto?> = arrayListOf()
    private lateinit var text_find:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        userRecyclerView = findViewById(R.id.lista_prods)
        text_find = findViewById(R.id.textFind)

        var dados: Bundle? = intent.extras



        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayout.VERTICAL))
        userRecyclerView.adapter = adapter



    }

fun ordenarDistancia(){
    var troca = true
    var aux:Produto
    var distancia1 = 0.0
    var distancia2 = 0.0
    var i =0
    var j =0
    var k =0
    while(troca){
        troca = false
        for( i in (lista_prod.indices)-1){
            j = lista_prod[i]!!.getId_buca()
            k = lista_prod[i+1]!!.getId_buca()
            distancia1 = buscador.get(j).getDistancia_loja().toDouble()
            distancia2 = buscador.get(k).getDistancia_loja().toDouble()
            if(distancia1 >distancia2){
                aux = lista_prod.get(i)!!



            }



        }
    }
}
}