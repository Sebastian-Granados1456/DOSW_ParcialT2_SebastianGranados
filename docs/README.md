## Punto 1
### [Asignación el Equipo de Producto - Definición del contrato de la API.xlsx](Asignaci%F3n%20el%20Equipo%20de%20Producto%20-%20Definici%F3n%20del%20contrato%20de%20la%20API.xlsx)
- a. Para una validación de entrada por ejemplo se revisa la completitud de los datos, una de negocio puede ser por ejemplo que el barbero exista, otra de negocio es que se tenga 10 digitos en un número telefónico, que no haya más reservas en la misma hora también cumple como de negocio.
- b. La API se detiene en la primer validación que falla, solo respondiendo con error con el código y su mensaje, si falla no se crea la reserva, nos e asigna barbero ni mucho menos se cambia a OCUPADO. 

## Punto 2
- a. El problema que aparece es que se genera acoplamiento, no se respetan las responsabilidades de las clases y pueden ocurrir errores de seguridad, ya que un controlador nunca debería saber qué es una BD ni manejar persistencia. Por ejemplo en reservas: el usuario interactúa con la API en el Controller, luego el Mapper IN recibe esa info y la pasa a modelo de dominio hacia el Service, ahí en Service se valida si todo está bien, y si es así pasa al Mapper OUT, que se entiende con el Repository para guardar dicha reserva en la BD.
- b. Un validador verifica que se cumpla alguna regla de negocio o entrada, un ejemplo es ValidadorReserva con la regla de pasar las 4 validaciones en cadena. Una utilidad es una clase de apoyo sin estado que puede reutilizarse en varias partes, ejemplo una clase de horarios para validar formatos de fechas y horas de los turnos. El servicio contiene la lógica de negocio detrás del controlador, un ejemplo es el servicio de crear reserva o el de ver mis reservas.
- c. El Mapper In es el encargado de trasnformar los datos que reciba el controller hacia el service. El mapper Out recibe de service los datos del dominio y los trasnforma para darselos a repository y luego ponerlos en la BD
- d. Tomando las 3 reglas del punto anterior

    1. Teléfono de 10 dígitos y correo @gmail.com: Se ejecuta al inicio en el Controller, porque si el dato viene mal escrito se rechaza de una vez.
    2. El barbero debe existir: Se ejecuta dentro del Service apoyándose en el Repository, porque necesita cruzar el Controller -> Mapper IN -> Service y consultar en MongoDB si el barbero está registrado ya que si no existe, lanza error.
    3. No se duplica barbero + fecha + hora: También se ejecuta en el Service junto con el Validator antes de mandar al Mapper OUT, porque debe preguntarle al Repository si ya existe ese turno en la BD. Si ya existe, detiene el proceso y arroja error.

## Punto 3
- Ventaja: Todo esta compacto y respeta las responsabilidades
- Desventaja: Es posible acceder a información importante por medio de otras clases.

## Punto 4
### Asignación del Equipo de Seguridad - Proteger el acceso y la información

a. 
  - Autenticación: Sirve para logins autenticación de usuarios en general, se utiliza al hacer un POST en login y es donde se utiliza el token generado al hacer la autenticación arriba para el id, uno JWT. 
  - Autorización: Sirve para establecer permisos de acceso a los usuarios, es decir, que puede hacer cada usuario según su rol. Permitiendo o denegando el acceso a recursos e información sensible, podemos aplicarlo en turnos ya que ADMIN y CLIENTE necesitan de este para saber que es lo que puede acceder cada usuario, se limita a que el cliente solo puede ver y observar las suyas propias 
  - Integridad: Sirve para establecer que los datos no se modificaron o alteraron al momento de transmitirlos o almacenarlos, si usamos tokens JWT garantizamos que los datos como el Id de un Turno no se altera y que su contenido no fue alterado, puede que hayan personas que intenten cambiarlo, lo que hace que la propia firma deje de servir y se rechace.

b. Dos posibles riesgos según OWASP TOP 10 son los siguientes:
 - Identification and Authentication Failures: Es posible que dejar muy ambigua la identificación y Login de un usuario como lo es admin@gmail.com o cliente@gmail.com puede facilitar ataques de fuerza bruta, esto se puede mitigar creando diferentes usuarios al implementar más seguridad en el login y autenticación, como usando tokens que expiren, pero con el token se mitigan varios problemas. Si creamos cuentas independientes y personalizadas verificamos que no haya debilidad en esta confirmación del usuario cuando generamos el token y la contraseña especifica.
 - Broken Access Control: Puede ser posible que haya usuarios como Clientes puedan acceder a recursos solo para Admin, se mitiga directamente con la autenticación por token, algo presente en JWT, así verificamos que la autenticación para cada usuario tiene el token correspondiente con los permisos de su tipo.

c. Para la autenticación el más adecuado para garantizar seguridad es el de JWT o JSON Web Token en autenticación es el más adecuado ya que se tienen dos tipos de usuario con permisos diferentes, Cliente y Admin, cuando se tienen varios tipos de usuario es necesario que en cada petición se sepa quien es y que puede hacer, así es más sencillo y suficiente.

## Punto 5
### Asignación del Equipo de Calidad - Definir cómo comprobar el comportamiento.

- Cada vez que se crea un nuevo objeto como clase con atributos, se debe crear un test por método que primero falle (RED), se implemente directamente el funcionamiento básico (GREEN), y por último se refactorice para funcionar en casos generales (REFACTOR). En este caso si un Cliente pide un turno es necesario verificar que genere un id, que pueda cambiar el estado a realizado

## Punto 6
### Por realizar

