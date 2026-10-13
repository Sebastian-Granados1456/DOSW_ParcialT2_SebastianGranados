## Punto 1
### [Asignación el Equipo de Producto - Definición del contrato de la API.xlsx](Asignaci%F3n%20el%20Equipo%20de%20Producto%20-%20Definici%F3n%20del%20contrato%20de%20la%20API.xlsx)
- a. El barbero debe existir es una regla de negocio ya que no depende del ADMIN que exista para validar si se crea el turno o no, eso quiere decir que es una validación de negocio, algo que no se verifica al crear el post de turno por parte del admin. Para una validación de entrada El turno nace DISPONIBLE es un buen ejemplo ya que no debe conocer al sistema completo ni interactuar con el para que el turno al crearse este en DISPONIBLE.
- b. La API debe detener el proceso y debe retornar el motivo específico, se debe informar un error conforme lo que pase con el cliente.
