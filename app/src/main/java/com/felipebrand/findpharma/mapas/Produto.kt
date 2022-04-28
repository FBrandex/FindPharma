package com.felipebrand.findpharma.mapas

class Produto {
    private var nome_pop:String? = null
    private var carac:String? = null
    private var via: String? = null
    private var gen_ori:String? = null
    private var valor = 0.0
    private var id_Busca = 0


    fun getPop():String?{
        return nome_pop
    }
    fun setPop(pop:String){
        this.nome_pop = pop
    }


    fun getCarac():String?{
        return carac
    }
    fun setCarac(carac:String){
        this.carac = carac
    }

    fun getVia():String?{
        return via
    }
    fun setVia(via:String){
        this.via = via
    }

    fun getValor():Double{
        return valor
    }
    fun setValor(valor:Double){
        this.valor = valor
    }

    fun getOriginal():String?{
        return gen_ori
    }
    fun setOriginal(gen:String){
        this.gen_ori = gen
    }

    fun getId_buca():Int{
        return id_Busca
    }
    fun setId_busca(id:Int){
        this.id_Busca = id
    }

}


