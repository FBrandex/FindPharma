package com.felipebrand.findpharma.mapas

class Buscador {

    private var nomeLoja: String? = null

    private var distanciaLoja = 0

    private var posicaoLoja = 0

    fun getNome_loja():String?{
        return nomeLoja
    }

    fun setNome_loja(nloja: String) {
        this.nomeLoja = nloja
    }

    fun getDistancia_loja():Int{
        return distanciaLoja
    }

    fun setDistancia_loja(dloja: Int) {
        this.distanciaLoja = dloja
    }

    fun getPosicao_loja(): Int{
        return posicaoLoja
    }

    fun setPosicao_loja(ploja: Int) {
        this.posicaoLoja = ploja
    }
}