import java.util.Date

fun main() {
    println("Hola Mundo")
    // Inmutables (no se pueden RE ASIGNAR "=")
    var inmutable: String = "Tatiana";
    //inmutable= "Erika" //¡ERROR!

    // Mutables
    val mutable: String = "Erika"
    mutable= "Tatiana"

    // VAL > VAR

    // Duck Typing
    var ejemploVariable = "Erika Anrrango"
    val edadEjemplo: Int = 12

    ejemploVariable.trim() //Sirve para borrar espacios de la cadena
    //ejemploVariable = edadEjemplo //¡ERROR! tipo incorrecto

    //Variables primitivas
    val nombre: String = "Tatiana";
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    // Clases en Java
    val fechaNacimiento: Date = Date()

    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") ->{
            println("Casado")
        }
        "S" ->{
            println("Soltero")
        }
        else ->{
            println("No sabemos")
        }
    }

    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No"

    calcularSueldo(10.00)
    calcularSueldo(10.00,15.00,20.00)
    // Named parameters
    // calcularSueldo(sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=14.00)

    calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=14.00)
    // Uso de clases
    val sumaUno = Suma(1,1) // new Suma(1,1)
    val sumaDos = Suma(null,1)
    val sumaTres = Suma(1,null)
    val sumaCuatro = Suma(null,null)

    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    // Arreglos Estaticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico);
    // Arreglos Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    )
    println(arregloDinamico);
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico);

    // FOR EACH = > Unit
    // Iterar un arreglo
    //val respuestaForEach: Unit =
        arregloDinamico
        .forEach { valorActual: Int ->
            println("Valor actual: ${valorActual}");
        }
        // "it" (en ingles "eso") significa el elemento iterado
        arregloDinamico. forEach{ println("Valor Actual (it): ${it}")}

    // MAP -> MUTA(Modifica cambia) el arreglo
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // Filter - > Filtrar el ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
        .filter{ valorActual: Int ->
            //Expresion o CONDICION
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }
    val respuestaFilterDos =arregloDinamico.filter{ it <= 5 } // < =
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno Cumple?)
    // AND -> ALL (Todos cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual: Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) // True
    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual: Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) // False

    // REDUCE -> Valor acumulado
    // Valor acumulado = 0 (Siempre empieza en 0 en Kotlin)
    // [1,2,3,4,5] -> Acumular "SUMAR" estos valores del arreglo
    // valorIteracion1 = valorEmpieza + 1 = 0 +1 = 1 -> Iteracion1
    // valorIteracion2 = valorAcumuladoIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // valorIteracion3 = valorAcumuladoIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    // valorIteracion4 = valorAcumuladoIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4

    // valorIteracion5 = valorAcumuladoIteracion4 + 5 = 10 + 5 = 15 -> Iteracion4

    val respuestaReduce: Int = arregloDinamico
        .reduce { acumulado: Int, valorAcual: Int ->
            return@reduce (acumulado + valorAcual) // -> Cambiar o usar la Logica de negoci
        }
    println(respuestaReduce)
    // return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)

    //return@filter mayoresACinco






}

// void -> Unit
fun imprimirNombre(nombre:String): Unit{
    println("Nombre: ${nombre}") // Template Strings
}

fun calcularSueldo(
    sueldo:Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial:Double? = null // Opcional (nullable)
    // Variable? -> "?" Es Nullable (osea que puede en algun momento ser nulo)
):Double {
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    if(bonoEspecial == null){
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) * bonoEspecial
    }
}

abstract class Numeros(
    // Constructor Primorio
    // Caso 1) Parametro normal
    // uno:Int , (parametro (sin modificador acceso))

    // Caso 2) Parametro y propiedad (atributo) (private)
    // private var uno: Int (propiedad "instancia.uno")

    protected val numeroUno: Int, // instancia.numeroUno
    protected val numeroDos: Int, // instancia.numeroDos

) {
    init { // bloque constructor primario (OPCIONAL )
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma( // Constructor primario
    unoParametro: Int, // Parametro
    dosParametro: Int, // Parametros
): Numeros( // Clase papa, Numeros (extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito:String = "Explicito" // Publicos
    val soyPublicoImplicito:String = "Implicito" // Publicas (propiedades, metodos)

    init{ // Bloque Codigo Constructor primario
        this.numeroUno
        this.numeroDos
        numeroUno // this. OPCIONAL (propiedodes, metodos)
        numeroDos // this. OPCIONAL (propiedades, metodos)
        this.soyPublicoExplicito
        soyPublicoImplicito // this. OPCIONAL (propiedades, metodos)
    }

    constructor( // Constructor secundario
        uno: Int?,
        dos: Int
    ):this(
        if(uno == null) 0 else uno,
        dos
    )
    constructor( // Constructor tercero
        uno: Int,
        dos: Int?
    ): this(
        uno,
        if(dos == null) 0 else dos,
    )
    constructor( // Constructor cuarto
        uno: Int?,
        dos: Int?
    ): this(
        if(uno == null) 0 else uno,
        if(dos == null) 0 else dos,
    )

    fun sumar():Int { // public fun sumar() (OPCIONAL)
        val total = numeroUno + numeroDos
        // Suma.agregarHistorial(total) ("Suma." o "NombreClase." es OPCIONAL)
        agregarHistorial(total)
        return total
    }
    companion object { // Comparte entre todas las instancias, similar ol Static
        // funciones y variables
        val pi = 3.14
        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorTotalSuma: Int) {
                historialSumas.add(valorTotalSuma)
            }
        }
    }


