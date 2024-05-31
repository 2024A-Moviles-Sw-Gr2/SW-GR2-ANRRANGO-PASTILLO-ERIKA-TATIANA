import tablas.Categoria
import tablas.Producto
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class Crud {
    private val productos: ArrayList<Producto> = arrayListOf()
    private val categorias: ArrayList<Categoria> = arrayListOf()
    private val format = SimpleDateFormat("dd/MM/yyyy")

    // Registra un nuevo producto
    fun registrarProducto(producto: Producto) {
        val categoriaExiste = categorias.any { it.obtenerID() == producto.obtenerIDCategoria() }
        if (categoriaExiste) {
            productos.add(producto)
            println("Producto registrado con éxito!\n")
        } else {
            println("No existe la categoria, producto no registrado!\n")
        }
    }

    // Consulta todos los productos
    fun consultarProductos() {
        productos.forEach { println(it) }
    }

    // Actualiza el precio y en enStock de un producto según su ID
    fun actualizarProducto(idProducto: Int, input: Scanner) {
        val producto = productos.find { it.obtenerID() == idProducto }
        if (producto == null) {
            println("El producto no está registrado. Revise los datos proporcionados!!\n")
            return
        }

        val datosPorActualizar = """
            1. Precio
            2. En stock
        """.trimIndent()
        println(datosPorActualizar)
        print("¿Qué desea actualizar?: ")
        val opcionDatoActualizar = input.nextInt()

        when (opcionDatoActualizar) {
            1 -> {
                print("Ingrese el nuevo precio: ")
                val nuevoPrecio = input.nextDouble()
                producto.modificarPrecio(nuevoPrecio)
                println("Precio del producto con ID: $idProducto actualizado!\n")
            }
            2 -> {
                print("¿Está disponible? (true/false): ")
                val enStock = input.nextBoolean()
                producto.modificarEnStock(enStock)
                println("Disponibilidad del producto con ID: $idProducto actualizada!\n")
            }
            else -> {
                println("Opción no válida. Inténtelo de nuevo.\n")
            }
        }
    }

    // Elimina un producto según su ID
    fun eliminarProducto(idProducto: Int) {
        val eliminado = productos.removeIf { it.obtenerID() == idProducto }
        if (eliminado) {
            println("Producto eliminado con éxito!\n")
        } else {
            println("El producto no está registrado. Revise los datos proporcionados!!\n")
        }
    }

    // Registra una nueva categoria
    fun registrarCategoria(categoria: Categoria) {
        categorias.add(categoria)
        println("Categoria registrado con éxito!\n")
    }

    // Consulta todas las categorias
    fun consultarCategorias() {
        categorias.forEach { println(it) }
    }

    // Actualiza categoria: descripcion, fecha y visibilidad
    fun actualizarCategoria(idCategoria: Int, input: Scanner) {
        val categoria = categorias.find { it.obtenerID() == idCategoria }
        if (categoria == null) {
            println("La categoria no está registrado. Revise los datos proporcionados!!\n")
            return
        }

        val datosPorActualizar = """
            1. Descripcion
            2. Visibilidad
        """.trimIndent()
        println(datosPorActualizar)
        print("¿Qué desea actualizar?: ")
        val opcionDatoActualizar = input.nextInt()

        when (opcionDatoActualizar) {
            1 -> {
                print("Descripcion de la categoria: ")
                val nuevaDescripcion = input.next()
                categoria.modificarDescripcion(nuevaDescripcion)
                categoria.modificarFechaActual(Date.from(Instant.now()))
                println("Descripcion de categoria con ID: $idCategoria actualizado!\n")
            }
            2 -> {
                print("¿Está visible? (true/false): ")
                val visible = input.nextBoolean()
                categoria.modificarVisibilidad(visible)
                categoria.modificarFechaActual(Date.from(Instant.now()))
                println("Disponibilidad del producto con ID: $idCategoria actualizada!\n")
            }
            else -> {
                println("Opción no válida. Inténtelo de nuevo.\n")
            }
        }
    }

    fun eliminarCategoria(idCategoria: Int) {
        val eliminado = categorias.removeIf { it.obtenerID() == idCategoria }
        if (eliminado) {
            println("Categoria eliminada con éxito!\n")
        } else {
            println("La categoria no está registrada. Revise los datos proporcionados!!\n")
        }
    }

    fun cargarArchivos() {
        cargarProductos()
        cargarCategorias()
    }

    private fun cargarCategorias() {
        val file =  File("C:\\Users\\escritorio.virtual17\\Documents\\GitHub\\2024A\\SW-GR2-ANRRANGO-PASTILLO-ERIKA-TATIANA\\02-deberes\\deber01\\src\\main\\kotlin\\archivos\\categorias.txt")
        if (file.exists()) {
            file.forEachLine { line ->
                val data = line.split(",")
                val categoria = Categoria(
                    data[0].toInt(),
                    data[1],
                    data[2],
                    data[3].toInt(),
                    data[5].toBoolean(),
                    format.parse(data[4])
                )
                categorias.add(categoria)
            }
        }
    }

    private fun cargarProductos() {
        val file = File("C:\\Users\\escritorio.virtual17\\Documents\\GitHub\\2024A\\SW-GR2-ANRRANGO-PASTILLO-ERIKA-TATIANA\\02-deberes\\deber01\\src\\main\\kotlin\\archivos\\productos.txt")
        if (file.exists()) {
            file.forEachLine { line ->
                val data = line.split(",")
                val producto = Producto(
                    data[0].toInt(),
                    data[1],
                    data[2].toDouble(),
                    format.parse(data[3]),
                    data[4].toBoolean(),
                    data[5].toInt()
                )
                productos.add(producto)
            }
        }
    }

    fun guardarArchivos() {
        guardarProductos()
        guardarCategorias()
    }

    private fun guardarProductos() {
        val file = File("C:\\Users\\escritorio.virtual17\\Documents\\GitHub\\2024A\\SW-GR2-ANRRANGO-PASTILLO-ERIKA-TATIANA\\02-deberes\\deber01\\src\\main\\kotlin\\archivos\\productos.txt")
        file.printWriter().use { out ->
            productos.forEach { producto ->
                out.println(
                    "${producto.idProducto},${producto.nombreProducto},${producto.precio}," +
                            "${format.format(producto.fechaCreacion)},${producto.enStock},${producto.idCategoria}"
                )
            }
        }
    }

    private fun guardarCategorias() {
        val file = File("C:\\Users\\escritorio.virtual17\\Documents\\GitHub\\2024A\\SW-GR2-ANRRANGO-PASTILLO-ERIKA-TATIANA\\02-deberes\\deber01\\src\\main\\kotlin\\archivos\\categorias.txt")
        file.printWriter().use { out ->
            categorias.forEach { categoria ->
                out.println(
                    "${categoria.idCategoria},${categoria.nombreCategoria},${categoria.descripcion}," +
                            "${categoria.cantidadProductos},${format.format(categoria.fechaActual)},${categoria.visibilidad}"
                )
            }
        }
    }
}