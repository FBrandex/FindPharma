package com.felipebrand.findpharma.mapas

class Farmacia {
    private var nome: String? = null
    private var lat = 0.0
    private var longi = 0.0
    private var end: String? = null
    private var Id: String? = null
    private var produto = arrayListOf<Produto>()

    fun getProduto(): List<Produto?>? {
        return produto
    }

    fun setProduto(produto: List<Produto?>?) {
        this.produto = produto as ArrayList<Produto>
    }


    fun addProduto(obj: Produto?) {
        if (obj != null) {
            produto.add(obj)
        }
    }

    fun getNome():String?{
        return nome
    }
    fun setNome(nome:String){
        this.nome = nome
    }
    fun getLat():Double{
        return lat
    }
    fun setLat(lat:Double){
        this.lat = lat
    }
    fun getLong():Double{
        return longi
    }
    fun setLong(longi:Double){
        this.longi = longi
    }

    fun getEnd(): String? {
        return end
    }
    fun setEnd(end:String){
        this.end = end
    }
    fun getId(): String? {
        return Id
    }
    fun setId(id:String){
        this.Id = id
    }


}

