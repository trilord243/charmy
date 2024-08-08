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
        "chia" to "17591002200197",
        "37591002002894" to "Maiz amarillo RS A-153.925 CPE0822546511",
        "17591002200203" to "Maiz Blacno con anis dulce RS A-153.925 CPE0822546511",
        "17591002200197" to "Maiz Blanco con chia negro, ajonjoli Y quinoa Roja RS A-153.925 CPE0822546511",

    )








    fun get(key: String): Any? {
        return store[key]
    }

    fun getAll(): Map<String, Any> {
        return store
    }

}