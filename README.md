The project is based on normal CRUD operations on a employee database and the used technologies are Springboot with Java and MySQL database.
Redis is used for caching and also fallback mechanism is handled.
Along with it multithreading is also implemented in Service file.
Flow of project:
When the rest controller  is hit by using approprite rest api it calls the specific service meant for it which in turn is responsible for performing the operation on database and in turn returns response back to controller.
The controller sends the response back to the client side.
