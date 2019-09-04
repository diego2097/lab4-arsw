/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.AuthorNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices service = null;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprints() {
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

    @RequestMapping(value="/{author}",method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable(name="author") String author) {
        try {
            //obtener datos que se enviarán a través del API
            Set<Blueprint> set = service.getBlueprintsByAuthor(author);
            
            Gson gson = new Gson();
            String JSON = gson.toJson(set);
            
            return new ResponseEntity<>(JSON, HttpStatus.ACCEPTED);
            
            
        }catch (AuthorNotFoundException ex) {
            
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("HTTP 404 Author not found", HttpStatus.NOT_FOUND);
        }
    }
    
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
    

}













