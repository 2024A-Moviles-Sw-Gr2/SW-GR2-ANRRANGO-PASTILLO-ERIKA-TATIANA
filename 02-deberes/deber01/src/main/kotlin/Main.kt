import tablas.Categoria
import tablas.Producto
import java.util.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate

fun main() {
    val input = Scanner(System.`in`)
    val crud = Crud()

    // Cargar datos desde archivos
    crud.cargarArchivos()

    var opcionEntidad: Int
    do {
        mostrarEntidades()
        print("Ingrese la opción de la entidad seleccionada: ")
        opcionEntidad = input.nextInt()
        println()
        seleccionarEntidad(opcionEntidad, input, crud)
    } while (opcionEntidad != 0)

    println("Saliendo del sistema...")
}

// Muestra las entidades disponibles en el sistema
private fun mostrarEntidades() {
    val menuEntidades = """
        === ENTIDADES DISPONIBLES ===
        1. Categoria
        2. Producto
        0. Salir
    """.trimIndent()
    println(menuEntidades)
}

// Selecciona la entidad a operar en función de la opción ingresada
private fun seleccionarEntidad(opcionEntidad: Int, input: Scanner, crud: Crud) {
    when (opcionEntidad) {
        1, 2 -> mostrarMenu(input, crud, opcionEntidad)
    }
}

// Muestra el menú principal de operaciones CRUD para productos
private fun mostrarMenu(input: Scanner, crud: Crud, opcionEntidad: Int) {
    var opcionCrud: Int
    do {
        val menu = """
            === VENTANA PRINCIPAL ===
             1. Registrar
             2. Consultar
             3. Actualizar
             4. Eliminar
             5. Volver
        """.trimIndent()
        println(menu)
        print("Ingrese su opción: ")
        opcionCrud = input.nextInt()

        when (opcionCrud) {
            1 -> {
                if (opcionEntidad.equals(1)) {
                    val categoria = insertarDatosCategoria(input)
                    crud.registrarCategoria(categoria)
                    crud.guardarArchivos()
                }
                if (opcionEntidad.equals(2)) {
                    val producto = insertarDatosProducto(input)
                    crud.registrarProducto(producto)
                    crud.guardarArchivos()
                }
            }
            2 -> {
                if (opcionEntidad.equals(1)) crud.consultarCategorias() else crud.consultarProductos()
            }
            3 -> {
                if (opcionEntidad.equals(1)) {
                    print("Ingrese el ID de la categoria a actualizar: ")
                    val idCategoria = input.nextInt()
                    crud.actualizarCategoria(idCategoria, input)
                    crud.guardarArchivos()
                }
                if (opcionEntidad.equals(2)) {
                    print("Ingrese el ID del producto a actualizar: ")
                    val idProducto = input.nextInt()
                    crud.actualizarProducto(idProducto, input)
                    crud.guardarArchivos()
                }
            }
            4 -> {
                if (opcionEntidad.equals(1)) {
                    print("Ingrese el ID de la categoria a eliminar: ")
                    val idCategoria = input.nextInt()
                    crud.eliminarCategoria(idCategoria)
                    crud.guardarArchivos()
                }
                if (opcionEntidad.equals(2)) {
                    print("Ingrese el ID del producto a eliminar: ")
                    val idProducto = input.nextInt()
                    crud.eliminarProducto(idProducto)
                    crud.guardarArchivos()
                }
            }
            5 -> {
                println("Regresando al menú de entidades...")
            }
            else -> {
                println("Opción no válida. Inténtelo de nuevo.")
            }
        }
    } while (opcionCrud != 5)
}

// Registra los datos de una nueva categoria
fun insertarDatosCategoria(input: Scanner): Categoria {
    print("Ingrese el ID de la categoria: ")
    val idCategoria = input.nextInt()
    print("Ingrese el nombre de la categoria: ")
    val nombreCategoria = input.next()
    print("Descripcion de la categoria: ")
    val descripcion: String = input.next()
    val cantidadProductos = 0 //Productos asociados automatizado
    val fechaActual: Date = Date.from(Instant.now()) //Por cada modificacion
    print("¿Está visible la categoria (true/false)?: ")
    val visible = input.nextBoolean()

    return Categoria(idCategoria, nombreCategoria, descripcion, cantidadProductos, visible, fechaActual)
}

// Registra los datos de un producto nuevo
private fun insertarDatosProducto(input: Scanner): Producto {
    print("Ingrese el ID del producto: ")
    val idProducto = input.nextInt()
    print("Ingrese el nombre del producto: ")
    val nombreProducto = input.next()
    print("Ingrese el precio del producto: ")
    val precio = input.nextDouble()
    print("Ingrese la fecha de creacion (dd/MM/aaaa) del producto: ")
    val fechaCreacion = input.next()
    print("¿Está en stock el producto (true/false)?: ")
    val enStock = input.nextBoolean()
    print("Ingrese el ID de la categoria asociada: ")
    val idCategoria = input.nextInt()

    val format = SimpleDateFormat("dd/MM/yyyy")
    val fechaFabricacion = format.parse(fechaCreacion) as Date
    return Producto(idProducto, nombreProducto, precio, fechaFabricacion, enStock, idCategoria)
}


