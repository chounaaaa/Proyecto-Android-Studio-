package com.example.inicioactivity

import android.app.Application
import com.example.inicioactivity.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        // Lanzamos una corutina para hacer la comprobación en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            // 1. Obtenemos el Dao de recetas
            val recetaDao = database.recetaDao()

            // Ya no comprobamos si la tabla está vacía.
            // Simplemente llamamos a la función que se encarga de todo.
            rellenarDatosIniciales(database.recetaDao(), database.ingredienteDao())

            // Si no está vacía, no hacemos nada. Los datos de usuario se conservan.
        }
    }

    // --- LÓGICA MOVIDA AQUÍ ---
    private suspend fun rellenarDatosIniciales(recetaDao: RecetaDao, ingredienteDao: IngredienteDao) {
        // --- Comprobación para Receta 1: Chorizo a la Pomarola ---
        if (recetaDao.buscarPorNombre("Chorizo a la Pomarola") == null) {
            // Si no existe, la creamos
            val chorizoALaPomarola = Receta(
                id_receta = 0,
                nombre = "Chorizo a la Pomarola",
                descripcion = "Un guiso que contiene chorizo y un rico tuco.",
                tiempoPreparacion = 50,
                dificultad = "Difícil",
                pasos = "1. En una sartén grande(u olla), calienta el aceite de oliva a fuego medio.\n" +
                        "2. Agregá los chorizos doralos por todos lados, aproximadamente 5-7 minutos, luego sacalos de la sarten y reservamos.\n" +
                        "3. En la misma sartén,(para tomar los jugos y/o restos de chorizo) agrega la cebolla picada y el pimiento rojo. Deben permanecer allí hasta que la cebolla esté transparente y el pimiento esté tierno, unos 5 minutos.\n" +
                        "4. Sumale ajo picado y cocina por un minuto más, teniendo cuidado de no quemarlo.\n" +
                        "5. Incorpora el tomate triturado y la hoja de laurel. Añade una cucharada de azúcar para balancear la acidez del tomate, luego echas sal y salpimienta al gusto.\n" +
                        "6. Cocina a fuego medio-bajo durante unos 10 minutos, removiendo de vez en cuando, hasta que la salsa se haya espesado un poco.\n" +
                        "7. Vuelve a colocar los chorizos en la sartén y asegúrate de que estén bien cubiertos con la salsa. Cocina a fuego lento durante unos 5 minutos más para que los sabores se integren.\n" +
                        "8. Retira la hoja de laurel antes de servir y, si lo deseas, espolvorea con perejil fresco picado.\n", // (tu texto completo de los pasos del chorizo)
                imagenResId = R.drawable.chorizo_a_la_pomarola
            )
            val idRecetaChorizo = recetaDao.insertarReceta(chorizoALaPomarola).toInt()
            // Nos aseguramos de que CADA ingrediente de esta lista use 'idRecetaChorizo'
            val ingredientesChorizo = listOf(
                Ingrediente(nombre = "Chorizos de cerdo", cantidad = "4 unidades", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Cebolla", cantidad = "1 grande", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Morrón rojo", cantidad = "1/2", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Salsa de tomate", cantidad = "500g", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Caldo de verduras", cantidad = "1 taza", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Orégano y ají molido", cantidad = "a gusto", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Aceite", cantidad = "cantidad necesaria", idReceta = idRecetaChorizo),
                Ingrediente(nombre = "Sal y pimienta", cantidad = "a gusto", idReceta = idRecetaChorizo)
            )
            ingredienteDao.insertarIngredientes(ingredientesChorizo)
        }

        // --- Comprobación para Receta 2: Empanadas de matambre criollas ---
        if (recetaDao.buscarPorNombre("Empanadas de matambre criollas") == null) {
            // Si no existe, la creamos
            val empanadasDeMatambre = Receta(
                id_receta = 0,
                nombre = "Empanadas de matambre criollas",
                descripcion = "Clásicas empanadas argentinas con un relleno jugoso y sabroso de matambre.",
                tiempoPreparacion = 150, // Sumando cocción de matambre, relleno y horno
                dificultad = "Difícil",
                pasos = "1. Colocamos el matambre en una olla donde quepa cómodamente. Cubrimos con agua y agregamos la leche. Salpimentamos y cocinamos a fuego bajo por una hora.\n" +
                        "2. Transcurrido del tiempo de cocción retiramos de la olla y dejamos a enfriar. Desgrasamos sin dejar totalmente magro y cortamos en cubitos de 1 cm de lado. Reservamos.\n" +
                        "3. En otra olla colocamos apenas un hilo de aceite y calentamos por unos segundos. Añadimos la cebolla y el morrón cortado en cubitos. Cocinamos hasta transparentar.\n" +
                        "4. Añadimos el matambre cortado y las pasas de uva. Sumamos el ají molido, el pimentón y cocinamos por unos 30 minutos a fuego bajo a olla destapada, para que se consuman los líquidos.\n" +
                        "5. Retiramos del fuego y sumamos los huevos duros picados y las aceitunas también picadas. Mezclamos y dejamos enfriar.\n" +
                        "6. Una vez frío el relleno, colocamos una porción en el centro de cada tapa. Humedecemos el borde, cerramos y hacemos el repulgue.\n" +
                        "7. Cocinamos en horno precalentado fuerte (200°C) hasta que estén doradas, aproximadamente unos 40 minutos.",
                imagenResId = R.drawable.empanadas_de_matambre_criollas
            )
            val idRecetaEmpanadas = recetaDao.insertarReceta(empanadasDeMatambre).toInt()
            val ingredientesEmpanadas = listOf(
                Ingrediente(nombre = "Matambre vacuno", cantidad = "700g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Leche (para cocción)", cantidad = "100ml", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Cebolla", cantidad = "500g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Morrón rojo", cantidad = "200g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Pasas de uva", cantidad = "50g", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Huevos duros", cantidad = "5 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Aceitunas verdes", cantidad = "7 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Tapas de empanadas hojaldradas", cantidad = "12 unidades", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Ají molido y pimentón", cantidad = "a gusto", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Sal y pimienta", cantidad = "a gusto", idReceta = idRecetaEmpanadas),
                Ingrediente(nombre = "Aceite", cantidad = "cantidad necesaria", idReceta = idRecetaEmpanadas)
            )
            ingredienteDao.insertarIngredientes(ingredientesEmpanadas)
        }

        // --- Comprobación para Receta 3: Humita ---
        if (recetaDao.buscarPorNombre("Humita") == null) {
            // Si no existe, la creamos
            val humita = Receta(
                id_receta = 0,
                nombre = "Humita",
                descripcion = "Un plato tradicional andino a base de maíz fresco, cremoso y lleno de sabor.",
                tiempoPreparacion = 60, // Tiempo estimado
                dificultad = "Media",
                pasos = "1. Desgrana los choclos y tritura los granos en una procesadora o licuadora hasta formar una pasta espesa.\n" +
                        "2. En una sartén grande, fríe la cebolla (y el pimiento si usas) en manteca y aceite hasta que quede transparente.\n" +
                        "3. Agrega la pasta de maíz, mezcla bien y añade la leche poco a poco.\n" +
                        "4. Condimenta con sal, pimienta y comino. Cocina unos 10 minutos hasta que espese ligeramente. Si el maíz no es muy dulce, puedes añadir una pizca de azúcar.\n" +
                        "5. Coloca dos chalas (hojas del choclo) cruzadas, pon una porción de la mezcla en el centro y agrega un trozo de queso.\n" +
                        "6. Cierra con las hojas y ata con tiras finas de chala o hilo de cocina.\n" +
                        "7. Cocina las humitas en agua hirviendo con sal durante 30-40 minutos o al vapor.",
                imagenResId = R.drawable.humitas
            )
            val idRecetaHumita = recetaDao.insertarReceta(humita).toInt()
            val ingredientesHumita = listOf(
                Ingrediente(nombre = "Choclos frescos", cantidad = "8 unidades", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Cebolla grande", cantidad = "1 unidad", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Pimiento rojo pequeño", cantidad = "1 (opcional)", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Manteca (mantequilla)", cantidad = "2 cucharadas", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Aceite", cantidad = "1 chorrito", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Leche", cantidad = "150 ml", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Queso fresco o mozzarella", cantidad = "a gusto", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Sal, pimienta y comino", cantidad = "a gusto", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Azúcar", cantidad = "1 pizca (opcional)", idReceta = idRecetaHumita),
                Ingrediente(nombre = "Chalas (hojas del choclo)", cantidad = "necesarias", idReceta = idRecetaHumita)
            )
            ingredienteDao.insertarIngredientes(ingredientesHumita)
        }

        // --- Comprobación para Receta 4: Fugazzeta ---
        if (recetaDao.buscarPorNombre("Fugazzeta") == null) {
            // Si no existe, la creamos
            val fugazzeta = Receta(
                id_receta = 0,
                nombre = "Fugazzeta",
                descripcion = "Una pizza argentina rellena de queso y cubierta con una generosa cantidad de cebolla.",
                tiempoPreparacion = 90, // Incluyendo tiempo de leudado
                dificultad = "Media",
                pasos = "1. Prepara la masa: disuelve la levadura en agua tibia con azúcar. En un bol, mezcla harina y sal. Haz un hueco, vierte la levadura y el aceite. Amasa hasta obtener una masa suave. Deja leudar por 1 hora.\n" +
                        "2. Divide la masa en dos partes (una un poco más grande para la base). Estira la base y colócala en una pizzera aceitada.\n" +
                        "3. Cubre la base con abundante queso mozzarella, dejando un borde libre.\n" +
                        "4. Estira la otra parte de la masa y úsala para tapar, sellando bien los bordes para que el queso no se escape.\n" +
                        "5. Mezcla la cebolla cortada en plumas con sal, pimienta y un chorrito de aceite de oliva. Distribúyela generosamente sobre la masa superior.\n" +
                        "6. Opcional: espolvorea con orégano o queso rallado.\n" +
                        "7. Hornea a 220-250°C por 20-25 minutos, hasta que la masa esté dorada y la cebolla caramelizada.",
                imagenResId = R.drawable.fugazzeta
            )
            val idRecetaFugazzeta = recetaDao.insertarReceta(fugazzeta).toInt()
            val ingredientesFugazzeta = listOf(
                Ingrediente(nombre = "Harina de trigo (000)", cantidad = "500 g", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Sal", cantidad = "10 g", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Azúcar", cantidad = "10 g", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Levadura fresca", cantidad = "25 g", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Agua tibia", cantidad = "300 ml", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Aceite de oliva", cantidad = "4 cucharadas", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Queso mozzarella", cantidad = "400-500 g", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Cebollas grandes", cantidad = "2 o 3", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Pimienta", cantidad = "a gusto", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Orégano seco", cantidad = "opcional", idReceta = idRecetaFugazzeta),
                Ingrediente(nombre = "Queso rallado", cantidad = "opcional", idReceta = idRecetaFugazzeta)
            )
            ingredienteDao.insertarIngredientes(ingredientesFugazzeta)
        }

        // --- Comprobación para Receta 5: Locro Argentino ---
        if (recetaDao.buscarPorNombre("Locro Argentino") == null) {
            // Si no existe, la creamos
            val locro = Receta(
            id_receta = 0,
            nombre = "Locro Argentino",
            descripcion = "Un guiso tradicional y contundente, perfecto para celebraciones patrias.",
            tiempoPreparacion = 240, // Aprox. 4 horas más el remojo
            dificultad = "Muy Difícil",
            pasos = "1. El día anterior, poner en remojo por separado el maíz pisado y los porotos (8-12 hs).\n" +
                    "2. En una olla grande, sofreír la cebolla en cubos con aceite. Añadir sal y pimienta.\n" +
                    "3. Sumar los porotos y el maíz escurridos. Cubrir con 1.6 litros de agua, llevar a hervor y cocinar tapado a fuego mínimo por 1 hora.\n" +
                    "4. Aparte, hervir los cueritos de cerdo por 5 minutos, enfriar y cortar en tiras. Hacer lo mismo con los chorizos y cortarlos en rodajas.\n" +
                    "5. Cortar el zapallo y las carnes en trozos, desgranar los choclos. Reservar.\n" +
                    "6. Pasada la hora, añadir a la olla todas las carnes, el zapallo y el choclo. Salar y cubrir con 1.8 litros de agua.\n" +
                    "7. Llevar a hervor, bajar a mínimo y cocinar tapado por 2 horas más, mezclando ocasionalmente.\n" +
                    "8. 5 minutos antes de terminar, rectificar la sal y añadir pimentón y ají molido. Mezclar bien y servir.",
            imagenResId = R.drawable.locro_argentino
            )
            val idRecetaLocro = recetaDao.insertarReceta(locro).toInt()
            val ingredientesLocro = listOf(
                Ingrediente(nombre = "Maíz pisado blanco", cantidad = "400 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Porotos alubia", cantidad = "300 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Cebolla blanca grande", cantidad = "2 unidades", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Agua mineral", cantidad = "3.4 litros (en total)", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Cueritos de cerdo", cantidad = "200-250 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Pechito de cerdo", cantidad = "1 kg", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Falda", cantidad = "800 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Panceta ahumada", cantidad = "250 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Chorizo colorado", cantidad = "200 g", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Chorizos de cerdo", cantidad = "2 unidades", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Zapallo cabutiá", cantidad = "1.2 kg (pulpa)", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Choclos frescos", cantidad = "3 unidades", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Ají molido", cantidad = "1 cda", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Pimentón", cantidad = "2 cdas", idReceta = idRecetaLocro),
                Ingrediente(nombre = "Sal y pimienta", cantidad = "a gusto", idReceta = idRecetaLocro)
            )
            ingredienteDao.insertarIngredientes(ingredientesLocro)
        }

        // --- Comprobación para Receta 6: Medialunas ---
        if (recetaDao.buscarPorNombre("Medialunas") == null) {
            // Si no existe, la creamos
            val medialunas = Receta(
                id_receta = 0,
                nombre = "Medialunas",
                descripcion = "Clásicas medialunas de manteca, perfectas para el desayuno o la merienda.",
                tiempoPreparacion = 150, // Incluyendo tiempos de leudado
                dificultad = "Difícil",
                pasos = "1. Fermentar la levadura: desgranarla con agua tibia, una cucharada de azúcar y una de harina. Dejar reposar.\n" +
                        "2. En un bol, batir la manteca pomada con el azúcar hasta obtener una crema. Agregar el huevo, una pizca de sal y esencia de vainilla.\n" +
                        "3. Integrar la leche y la levadura fermentada a la mezcla anterior. Batir ligeramente.\n" +
                        "4. Agregar la harina de a poco, amasando hasta obtener una masa homogénea y suave. Dejar leudar tapada por 45 minutos.\n" +
                        "5. Estirar la masa, cortar triángulos y enrollarlos desde la base para darles la forma de medialuna.\n" +
                        "6. Colocar en una placa y dejar leudar por 30 minutos más.\n" +
                        "7. Pincelear con huevo batido y hornear a 160°C por 40-50 minutos, hasta que estén doradas.\n" +
                        "8. Mientras se hornean, preparar un almíbar simple (partes iguales de agua y azúcar). Pincelar las medialunas calientes al salir del horno.",
                imagenResId = R.drawable.medialunas // Reemplaza con el nombre de tu imagen
            )
            val idRecetaMedialunas = recetaDao.insertarReceta(medialunas).toInt()
            val ingredientesMedialunas = listOf(
                Ingrediente(nombre = "Harina 000", cantidad = "1 kg", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Levadura fresca", cantidad = "50 gr", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Manteca o margarina", cantidad = "200 gr", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Azúcar", cantidad = "200 gr", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Huevo", cantidad = "1", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Esencia de vainilla", cantidad = "a gusto", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Leche", cantidad = "200 ml", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Almíbar", cantidad = "necesario", idReceta = idRecetaMedialunas),
                Ingrediente(nombre = "Huevo para pincelear", cantidad = "1 (opcional)", idReceta = idRecetaMedialunas)
            )
            ingredienteDao.insertarIngredientes(ingredientesMedialunas)
        }

        // --- Comprobación para Receta 7: Vitel Toné ---
        if (recetaDao.buscarPorNombre("Vitel tone") == null) {
            // Si no existe, la creamos
            val vitelTone = Receta(
                id_receta = 0,
                nombre = "Vitel tone",
                descripcion = "Un clásico plato frío de la cocina argentina, ideal para las fiestas.",
                tiempoPreparacion = 540, // Incluyendo marinado nocturno
                dificultad = "Media",
                pasos = "1. Marinar el peceto en vino blanco toda la noche en la heladera.\n" +
                        "2. Atar el peceto para que conserve su forma. Hervirlo en caldo con una hoja de laurel por 1 hora (40 min en olla a presión).\n" +
                        "3. Dejar enfriar la carne dentro de su propio caldo.\n" +
                        "4. Para la salsa: licuar un cucharón del caldo de cocción, yemas de huevo duro, atún, anchoas, aceite de oliva, vinagre, jugo de limón, mostaza, mayonesa y una cucharada de alcaparras. Licuar hasta obtener una crema fina.\n" +
                        "5. Cortar el peceto frío en rodajas finas.\n" +
                        "6. Acomodar las rodajas en una fuente, bañar con la salsa y decorar con más alcaparras por encima.",
                imagenResId = R.drawable.vitel_tone // Reemplaza con el nombre de tu imagen
            )
            val idRecetaVitelTone = recetaDao.insertarReceta(vitelTone).toInt()
            val ingredientesVitelTone = listOf(
                Ingrediente(nombre = "Peceto", cantidad = "1.5 kg", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Vino blanco", cantidad = "2 vasos", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Caldo de carne", cantidad = "necesario", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Hoja de laurel", cantidad = "1", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Lata de atún", cantidad = "1", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Yemas de huevo duro", cantidad = "2", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Aceite de oliva", cantidad = "2 cdas.", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Anchoas", cantidad = "3-4 filetes", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Vinagre", cantidad = "1 cda.", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Jugo de limón", cantidad = "1/2 unidad", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Mostaza", cantidad = "1 cda.", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Mayonesa", cantidad = "2-3 cdas.", idReceta = idRecetaVitelTone),
                Ingrediente(nombre = "Alcaparras", cantidad = "1 cda. + extra", idReceta = idRecetaVitelTone)
            )
            ingredienteDao.insertarIngredientes(ingredientesVitelTone)
        }

        // --- Comprobación para Receta 8: Bizcochuelo ---
        if (recetaDao.buscarPorNombre("Bizcochuelo (de vainilla)") == null) {
            // Si no existe, la creamos
            val bizcochuelo = Receta(
                id_receta = 0,
                nombre = "Bizcochuelo (de vainilla)",
                descripcion = "Un bizcochuelo clásico, esponjoso y fácil de preparar, ideal para cualquier ocasión.",
                tiempoPreparacion = 60,
                dificultad = "Fácil",
                pasos = "1. Mezclar los huevos, la esencia de vainilla y la ralladura de naranja o limón.\n" +
                        "2. Incorporar el aceite, el azúcar y la leche. Mezclar bien.\n" +
                        "3. Añadir las dos tazas de harina leudante y batir hasta que no queden grumos.\n" +
                        "4. Verter la preparación en un molde previamente enmantecado y enharinado.\n" +
                        "5. Hornear a 160°C por 45 minutos o hasta que al pinchar con un palillo, este salga seco. ¡Importante no abrir el horno durante la cocción!\n" +
                        "6. Retirar del horno, dejar enfriar y luego desmoldar.",
                imagenResId = R.drawable.bizcochuelo // Reemplaza con el nombre de tu imagen
            )
            val idRecetaBizcochuelo = recetaDao.insertarReceta(bizcochuelo).toInt()
            val ingredientesBizcochuelo = listOf(
                Ingrediente(nombre = "Harina leudante", cantidad = "2 tazas", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Azúcar", cantidad = "1 taza", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Leche", cantidad = "1 taza", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Aceite", cantidad = "150 cc", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Huevos", cantidad = "2", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Esencia de vainilla", cantidad = "a gusto", idReceta = idRecetaBizcochuelo),
                Ingrediente(nombre = "Ralladura de naranja o limón", cantidad = "a gusto", idReceta = idRecetaBizcochuelo)
            )
            ingredienteDao.insertarIngredientes(ingredientesBizcochuelo)
        }

        // --- Comprobación para Receta 9: Chocotorta ---
        if (recetaDao.buscarPorNombre("Chocotorta") == null) {
            // Si no existe, la creamos
            val chocotorta = Receta(
                id_receta = 0,
                nombre = "Chocotorta",
                descripcion = "La torta argentina más fácil y popular, sin horno y deliciosa.",
                tiempoPreparacion = 75, // Incluyendo enfriado
                dificultad = "Fácil",
                pasos = "1. Batir el dulce de leche hasta que aclare su color. Agregar la crema de leche y batir a velocidad baja hasta obtener una consistencia firme (punto letra). ¡Cuidado de no cortar la crema!\n" +
                        "2. Remojar rápidamente cada galletita en leche o café.\n" +
                        "3. En una fuente, colocar una capa de galletitas humedecidas, una al lado de la otra.\n" +
                        "4. Cubrir con una capa de la crema de dulce de leche.\n" +
                        "5. Repetir el proceso, alternando capas de galletitas y crema, hasta terminar con una capa de crema (usualmente 4 capas de cada una).\n" +
                        "6. Llevar al refrigerador por al menos 1 hora.\n" +
                        "7. Antes de servir, espolvorear con cacao en polvo y rallar chocolate semiamargo por encima.",
                imagenResId = R.drawable.chocotorta // Reemplaza con el nombre de tu imagen
            )
            val idRecetaChocotorta = recetaDao.insertarReceta(chocotorta).toInt()
            val ingredientesChocotorta = listOf(
                Ingrediente(nombre = "Dulce de leche (repostero)", cantidad = "400 gr", idReceta = idRecetaChocotorta),
                Ingrediente(nombre = "Crema de leche (natilla)", cantidad = "400 gr", idReceta = idRecetaChocotorta),
                Ingrediente(nombre = "Galletitas de chocolate", cantidad = "750 gr", idReceta = idRecetaChocotorta),
                Ingrediente(nombre = "Leche o café", cantidad = "necesario", idReceta = idRecetaChocotorta),
                Ingrediente(nombre = "Chocolate semiamargo", cantidad = "50 gr", idReceta = idRecetaChocotorta),
                Ingrediente(nombre = "Cacao en polvo", cantidad = "50 gr", idReceta = idRecetaChocotorta)
            )
            ingredienteDao.insertarIngredientes(ingredientesChocotorta)
        }

        // --- Comprobación para Receta 10: Milanesas a caballo ---
        if (recetaDao.buscarPorNombre("Milanesas a caballo") == null) {
        // Si no existe, la creamos
        val milanesas = Receta(
            id_receta = 0,
            nombre = "Milanesas a caballo",
            descripcion = "Un plato icónico argentino: milanesa de carne con papas fritas y huevos fritos.",
            tiempoPreparacion = 30,
            dificultad = "Fácil",
            pasos = "1. Preparar dos platos: uno con pan rallado y otro con huevos batidos, orégano, sal y especias.\n" +
                    "2. Pasar cada filete de carne primero por pan rallado para secarlo, luego por la mezcla de huevo y finalmente de nuevo por pan rallado, presionando bien.\n" +
                    "3. Freír las milanesas en una sartén con aceite caliente hasta que estén doradas por ambos lados.\n" +
                    "4. A la par, freír las papas cortadas en bastones hasta que estén doradas y crujientes. Sazonar con sal o provenzal.\n" +
                    "5. Freír dos huevos por porción.\n" +
                    "6. Servir la milanesa caliente con las papas fritas al lado y los dos huevos fritos montados \"a caballo\" sobre la carne.",
            imagenResId = R.drawable.milanesas_a_caballo // Reemplaza con el nombre de tu imagen
        )
        val idRecetaMilanesas = recetaDao.insertarReceta(milanesas).toInt()
        val ingredientesMilanesas = listOf(
            Ingrediente(nombre = "Carne para milanesas (nalga, bola de lomo, etc.)", cantidad = "a gusto", idReceta = idRecetaMilanesas),
            Ingrediente(nombre = "Pan rallado", cantidad = "necesario", idReceta = idRecetaMilanesas),
            Ingrediente(nombre = "Huevos", cantidad = "necesario", idReceta = idRecetaMilanesas),
            Ingrediente(nombre = "Orégano, sal y especias", cantidad = "a gusto", idReceta = idRecetaMilanesas),
            Ingrediente(nombre = "Papas", cantidad = "a gusto", idReceta = idRecetaMilanesas),
            Ingrediente(nombre = "Aceite para freír", cantidad = "necesario", idReceta = idRecetaMilanesas)
        )
        ingredienteDao.insertarIngredientes(ingredientesMilanesas)
    }


}
}
