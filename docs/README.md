## Punto 1
### [Asignación el Equipo de Producto - Definición del contrato de la API.xlsx](Asignaci%F3n%20el%20Equipo%20de%20Producto%20-%20Definici%F3n%20del%20contrato%20de%20la%20API.xlsx)
- a. El barbero debe existir es una regla de negocio ya que no depende del ADMIN que exista para validar si se crea el turno o no, eso quiere decir que es una validación de negocio, algo que no se verifica al crear el post de turno por parte del admin. Para una validación de entrada El turno nace DISPONIBLE es un buen ejemplo ya que no debe conocer al sistema completo ni interactuar con el para que el turno al crearse este en DISPONIBLE.
- b. La API debe detener el proceso y debe retornar el motivo específico, se debe informar un error conforme lo que pase con el cliente.


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

