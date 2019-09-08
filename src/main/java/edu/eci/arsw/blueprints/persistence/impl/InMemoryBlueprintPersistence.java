/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.AuthorNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

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
    
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        if (blueprints.get(new Tuple<>(author, bprintname)) == null ) throw new BlueprintNotFoundException("Blueprint no existe");
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws AuthorNotFoundException  {
       Set<Blueprint> retornar=new HashSet<>();
       for(Tuple<String,String> e : blueprints.keySet()){
          if( e.getElem1().equals(author)){
              retornar.add( blueprints.get(e));
          }
       }
       if (retornar.isEmpty()) throw new AuthorNotFoundException("El autor no existe");
       return retornar;
    }
    
     public Set<Blueprint> getAllBlueprints(){
       Set<Blueprint> retornar=new HashSet<>();
       for(Tuple<String,String> e : blueprints.keySet()){
              retornar.add( blueprints.get(e));
       };
       return retornar;
    }

    @Override
    public void uptade(String author, String bname, Blueprint blueprint)throws BlueprintNotFoundException {
        getBlueprint(author,bname);//esto es para checkar que exista el blueprint
        Tuple<String,String> llave=new Tuple(author,bname);
        blueprints.remove(llave);
        llave=new Tuple(blueprint.getAuthor(),blueprint.getName());
        blueprints.put(llave, blueprint);
    }
}
