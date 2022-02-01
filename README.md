# CHALLENGE_CAPTURER_APP-NOKIA

Aplicación Android para capturar imágenes, vídeos, sonidos, u otros datos que aprovechan los sensores del dispositivo móvil, y subirlos a una carpeta de google drive (u otro repositorio similar) para que sean procesados por challenges desde el programa securemirror.exe.



### Para desarrollar y escribir código es necesario instalar:
 - Android Studio


### Instalar aplicación en el móvil
Para hacer pruebas es necesario habilitar:
 - **Modo desarrollador** (buscar el número de compilación y hacer tap repetidas veces, entre 5 y 10, hasta que salga un toast que ponga que se han activado las opciones de desarrollo).
 - **Instalación mediante USB** (en el "nuevo" menú de opciones de desarrollo).
 - **Depuración USB** (en el "nuevo" menú de opciones de desarrollo).


### Archivos importantes dentro de un proyecto de Android Studio
 - **App > manifest > AndroidManifest.xml**: en él se describe la aplicación. Piden los permisos
 - **App > java > MainActivity.kt**: (u otro **xxx.kt**) es el código (Kotlin) que se ejecuta en una actividad (una pantalla de la aplicación).
 - **App > res > layout > activity_main.xml**: es un código XML de descripción visual de una actividad (una pantalla de la aplicación). Se puede editar como texto manipulando el XML directamente, o gráficamente arrastrando elementos gráficos como cajas de texto o botones.
 - **Gradle Scripts > build.gradle (Module:\<APP_NAME\>.app)**: contiene los plugins de la aplicación e "imports" externos necesarios.