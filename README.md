# Part I

- Se integraron las clases del proyecto anterior como se puede observar en la imagen

![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/carpetas.PNG "Directorio")

- Modificamos la clase InMemoryBlueprintPersistence para ingresar por defecto 4 blueprints con dos de ellos con el mismo autor

```java 
   public InMemoryBlueprintPersistence() {
        //load default data
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("carlos", "thepaint",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        
        Point[] pts1=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp1=new Blueprint("rafael", "thepaint2",pts1);
        blueprints.put(new Tuple<>(bp1.getAuthor(),bp1.getName()), bp1);
        
        Point[] pts2=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp2=new Blueprint("carlos", "thepaint3",pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
        
        Point[] pts3=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp3=new Blueprint("mick", "thepaint4",pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        
    }    
```

- Configuramos la clase BlueprintAPIController para crear el recurso /blueprints y que envie un json 

```java
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices service = null;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX() {
        try {
            //obtener datos que se enviarán a través del API
            Set<Blueprint> set = service.getAllBlueprints();
            Gson gson = new Gson();
            String JSON = gson.toJson(set);

            return new ResponseEntity<>(JSON, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.NOT_FOUND);
        }
    }

}
```

- Al compilar el proyecto obtenemos lo siguiente 


![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/Spring-run.PNG "Directorio")

- La lista en formato de json 

![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/blueprints_lista.PNG "Directorio")


- Despues agregamos la funcionalidad para poder ver los blueprints de un autor en especifico. agregando el recurso /blueprints/{author}, con el error indicado, si no encuentra el autor:


```java
  @RequestMapping(value="/{author}",method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable(name="author") String author) {
        try {
            //obtener datos que se enviarán a través del API
            Set<Blueprint> set = service.getBlueprintsByAuthor(author);
            
            Gson gson = new Gson();
            String JSON = gson.toJson(set);
            
            return new ResponseEntity<>(JSON, HttpStatus.ACCEPTED);
            
            
        }catch (AuthorNotFoundException | BlueprintNotFoundException ex) {
            
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Author not found", HttpStatus.NOT_FOUND);
        }
    }
```

- Despues agregamos la funcionalidad para poder ver el blueprint de un autor en especifico. agregando el recurso /blueprints/{author}/{bpname}, con el error indicado, si no encuentra el autor o el blueprint:


```java
 @RequestMapping(value="/{author}/{bpname}",method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthorAndBname(@PathVariable(name="author") String author,@PathVariable(name="bpname") String bname) {
        try {
            //obtener datos que se enviarán a través del API
            Blueprint bp = service.getBlueprint(author,bname);
            
            Gson gson = new Gson();
            String JSON = gson.toJson(bp);
            
            return new ResponseEntity<>(JSON, HttpStatus.ACCEPTED);
            
            
        }catch (AuthorNotFoundException ex ) {
            
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Author not found", HttpStatus.NOT_FOUND);
        }
        catch(BlueprintNotFoundException ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Blueprint not found", HttpStatus.NOT_FOUND);
        }
    }
```
## Parte 2
implementado el metodo post: 
```java
@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> agregarBlueprint(@RequestBody Blueprint blueprint) {
        try {
            service.addNewBlueprint(blueprint);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            //Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla", HttpStatus.FORBIDDEN);
        }

    }
```
realizando el post con curl: 
![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/curl.PNG "CURL")

![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/curlrestultado.PNG "curl Respuesta")

comprobando que se haya agregado en blueprints:
![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/blueprints.PNG "blueprints")


comprobado que se haya agregado consultando el autor y el plano: 

![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/analtasio.PNG "consultando autor y plano")


### Put 

realizando el metodo en la clase BlueprintAPIController: 
```java
    @RequestMapping(path =  "{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Blueprint blueprint,@PathVariable(name = "author") String author, @PathVariable(name = "bpname") String bname) {
        try {
            //obtener datos que se enviarán a través del API
            
            service.uptade(author, bname,blueprint);
           
            
            
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
```
se realiza la transicion y la imprementacion de la clase InMemoryBlueprintPersistence
```java
@Override
    public void uptade(String author, String bname, Blueprint blueprint) {
        Tuple<String,String> llave=new Tuple(author,bname);
        blueprints.remove(llave);
        llave=new Tuple(blueprint.getAuthor(),blueprint.getName());
        blueprints.put(llave, blueprint);
    }
}
```
realizando el curl con la peticion PUT: 
![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/CurlPut.PNG "CURL PUT")


restultado en el navegador: 
![alt text](https://github.com/diego2097/lab4-arsw/blob/master/img/Put.PNG " Resultado")

# Part III

Region critica:  esta se ecuentra en los serivicios de update para la solucionar este se coloco en el InMemoryBlueprintPersistence el Map blueprints volatile, adicionalmente  se sincronizo el acceso al elemento en el metodo update de la misma clase: 

```java
private volatile Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();
 public void uptade(String author, String bname, Blueprint blueprint) throws BlueprintNotFoundException {
        getBlueprint(author, bname);//esto es para checkar que exista el blueprint
        Tuple<String, String> llave = new Tuple(author, bname);
        synchronized (blueprints.get(llave)) {
            blueprints.remove(llave);
            llave = new Tuple(blueprint.getAuthor(), blueprint.getName());
            blueprints.put(llave, blueprint);
        }

    }
```
