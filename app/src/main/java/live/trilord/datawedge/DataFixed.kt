package live.trilord.datawedge

object DataFixed {
    private val store: Map<String, Any> = mapOf(
        "vainilla" to "17591164000048",
        "chocolate" to "17591164000062",
        "fresa" to "17591164000055",
         "moka" to "175911640031",
        "naranja" to "17591164000079",
        "cachapa" to "37591002002894",
        "anis" to "17591002200203",
        "chia" to "17591002200197",)


    private val description : Map<String,String> = mapOf(
        "37591002002894" to "Maiz amarillo RS A-153.925 CPE0822546511",
        "17591002200203" to "Maiz Blacno con anis dulce RS A-153.925 CPE0822546511",
        "17591002200197" to "Maiz Blanco con chia negro, ajonjoli Y quinoa Roja RS A-153.925 CPE0822546511",
        "17591164000048" to "Galleta de vainlla",
        "17591164000062" to "Galleta de chocolate Charmy rica",
        "17591164000055" to "Galleta de fresa",
        "175911640031" to "Galleta de moka ",
        "17591164000079" to "Halleta de naranja",

        )





    fun getDesc(key:String?): Any?{
        return description[key]
    }




    fun get(key: String): Any? {
        return store[key]
    }

    fun getAll(): Map<String, Any> {
        return store
    }

}