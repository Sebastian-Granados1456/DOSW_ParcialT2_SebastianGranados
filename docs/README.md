## Punto 1
### [Asignación el Equipo de Producto - Definición del contrato de la API.xlsx](Asignaci%F3n%20el%20Equipo%20de%20Producto%20-%20Definici%F3n%20del%20contrato%20de%20la%20API.xlsx)
- a. El barbero debe existir es una regla de negocio ya que no depende del ADMIN que exista para validar si se crea el turno o no, eso quiere decir que es una validación de negocio, algo que no se verifica al crear el post de turno por parte del admin. Para una validación de entrada El turno nace DISPONIBLE es un buen ejemplo ya que no debe conocer al sistema completo ni interactuar con el para que el turno al crearse este en DISPONIBLE.
- b. La API debe detener el proceso y debe retornar el motivo específico, se debe informar un error conforme lo que pase con el cliente.

## Punto 2
- a. El problema que aparece es que no se respentan las responsabilidades de las clases y pueden ocurrir errores de seguridad ya que por ejemplo un controlador nunca deberia saber que es una BD.Por ejemplo en reservas: el usuario interactua con la app en controlador, luego el mapper in recibe esa info y la lleva a service, ahi en service se valida si todo esta bien, si es asi se va a mapper out que este se entiende con la BD y guarda dicha reserva.
- b. Un validador valida alguna regla o caso de uso, un ejemplo es la regla de que debe pasar 5 validaciones. Una utilidad es aquella que puede reutilizarce, ejemplo como la estructura del turno. El servicio es lo que esta detras de la interfaz del usuario, un ejemplo es el servicio de ver mis reservas.
- c. El Mapper In es el encargado de llevar los datos que reciba en controller hacia el service. El mapper Out recibe de service los datos y los trasnforma para darselos a repository y luego ponerlos en la BD
- d. Las 3 reglas deben pasar por el validator luego de haber cruzado el controlador - MapperIn - Service. Si alguna no se cumple no pasa y arroja un error http con su mensaje.

## Punto 3
- Ventaja: Todo esta compacto y respeta las responsabilidades
- Desventaja: Es posible acceder a información importante por medio de otras clases.

## Punto 4
### Asignación del Equipo de Seguridad - Proteger el acceso y la información

a. 
  - Autenticación: Sirve para logins autenticación de usuarios en general, se utiliza al hacer un POST para login y es donde se utiliza el token generado al hacer la autenticación arriba para el id. 
  - Autorización: Sirve para establecer permisos de acceso a los usuarios, permitiendo o denegando el acceso a recursos e información sensible, podemos aplicarlo en turnos ya que ADMIN y CLIENTE necesitan de este para saber que es lo que puede acceder a ver cada usuario, aplicandolo a GET es una forma de establecer los roles y sus accesos que es lo que pide Autorización.
  - Integridad: Sirve para establecer que los datos no se modificaron o alteraron al momento de transmitirlos o almacenarlos, si usamos tokens JWT garantizamos que los datos como el Id de un Turno no se altera y que su contenido no fue alterado, al igual que podemos utilizar paginas que reserven esos datos como lo es platafromas de servicios web como Azure.

b. Dos posibles riesgos según OWASP TOP 10 son los siguientes:
 - Identification and Authentication Failures: Es posible que dejar muy ambigua la identificación y Login de un usuario como lo es admin@gmail.com o cliente@gmail.com puede facilitar ataques de fuerza bruta, esto se puede mitigar creando diferentes usuarios al implementar más saguridad en el login y autenticación, pero con el token se mitigan varios problemas. Si creamos cuentas independientes y personalizadas verificamos que no haya debilidad en esta confirmación del usuario cuando generamos el token y la contraseña especifica.
 - Broken Access Control: Puede ser posible que haya usuarios como Clientes puedan acceder a recursos solo para Admin, se mitiga directamente con la autenticación por token, algo presente en JWT, así verificamos que la autenticación para cada usuario tiene el token correspondiente con los permisos de su tipo.

c. Para la autenticación el más adecuado para garantizar seguridad es el de JWT o JSON Web Token en autenticación es el más adecuado ya que se tienen dos tipos de usuario con permisos diferentes, Cliente y Admin, cuando se tienen varios tipos de usuario es necesario darle diferentes permisos y es el token el que otorga y hace posible esto. Así se garantiza seguridad de manera sencilla.

## Punto 5
### Asignación del Equipo de Calidad - Definir cómo comprobar el comportamiento.

- Cada vez que se crea un nuevo objeto se debe crear un test que primero falle por falta de implementación (RED), se implemente el código mínimo con el funcionamiento básico para que pase (GREEN), y por último se refactorice el código sin cambiar su comportamiento así verificando que las pruebas sigan pasando (REFACTOR).
- En Pedir un Turno se tienen varios escenarios, reserva exitosa CONFIRMADA (donde el turno pasa a OCUPADO), sin barbero elegido (donde se le asigna uno por especialidad) y un rechazo por cada error.
- Se comprueba el orden de la siguiente forma, se crean pruebas que fallen en dos validaciones a la vez, por ejemplo, franja llena y nombre vacío, en el cual responde con un rechazo de error E01 y no E03 por ejemplo.
- La forma de comprobar que una falla no deja cambios es revisar que no se guarda la reserva y el turno se mantiene DISPONIBLE.

a. Se puede hacer que cada prueba sea una regla del caso, ahí directamente se agrega la caracteristica de que si pasa despues de implementar Refactor es porque esa regla efectivamente se cumple. Con pruebas que son unitarias, hay que verificar cada validador por separado, en general la lógica de servicio, el cálculo del servicio total y la asignación final del barbero.

b. No, no van a mostrar directamente por si solas la ausencia de errores, lo único que garantizan es que no haya errores en los casos específicos que se prueban, también puede pasar que haya casos que no pensamos como pruebas. Claramente ayuda a que los implementados como pruebas tengan menos errores al recorrer todo el esquema TDD, pero estos no garantizan a la final que los errores no existan en otras partes. 
## Punto 6
### Por realizar
