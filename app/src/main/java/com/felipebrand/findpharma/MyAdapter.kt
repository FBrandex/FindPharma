package com.felipebrand.findpharma

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.felipebrand.findpharma.mapas.Buscador
import com.felipebrand.findpharma.mapas.Produto



 open class Adapter: RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private var lista_prod:List<Produto?> = arrayListOf()
    private var nomeFarma:String? = null
    private var distance = 0
    private var bus_index:List<Buscador?> = arrayListOf()
    @RequiresApi(Build.VERSION_CODES.N)
    val dec = DecimalFormat("#,###.00")

    fun Adaptador(lista_prod:List<Produto?>, nomeFarm:String, distance:Int){
        this.lista_prod = lista_prod
        this.nomeFarma = nomeFarm
        this.distance = distance
    }

    open fun Adaptador(lista_prod: List<Produto?>, bus_index: List<Buscador?>) {
        this.lista_prod = lista_prod
        this.bus_index = bus_index
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista: View = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return MyViewHolder(itemLista)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prodaux: Produto? = lista_prod.get(position)

        if(bus_index == null) run{
            holder.nome_loja.text = nomeFarma
            holder.distancia.text = "<"+distance+" m"
        }else run {
            val aux: Int = lista_prod.get(position)!!.getId_buca()
            holder.nome_loja.text = bus_index.get(aux)!!.getNome_loja()
            holder.distancia.text = "<"+ bus_index.get(aux)!!.getDistancia_loja()+" m"
        }
        holder.carac_prod.text = prodaux!!.getCarac()+" "+ prodaux.getVia()+" "+ prodaux.getOriginal()
        holder.preco_prod.text = "R$"+ dec.format(prodaux.getValor()).replace(".",",")
        holder.nome_prod.text = prodaux.getPop()
    }

    override fun getItemCount(): Int {
        return lista_prod.size
    }


    class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nome_loja: TextView = itemView.findViewById(R.id.show_loja_name)
        var carac_prod: TextView =  itemView.findViewById(R.id.show_info_prod)
        var distancia: TextView = itemView.findViewById(R.id.show_distance)
        var preco_prod: TextView = itemView.findViewById(R.id.show_price)
        var nome_prod: TextView = itemView.findViewById(R.id.show_prod)

    }
}




