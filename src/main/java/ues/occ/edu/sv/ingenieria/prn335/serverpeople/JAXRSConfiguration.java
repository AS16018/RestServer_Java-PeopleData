package ues.occ.edu.sv.ingenieria.prn335.serverpeople;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
@ApplicationPath("resources")
public class JAXRSConfiguration extends Application{
    
    public Set<Class<?>> getClases(){
        Set<Class<?>> recursos = new HashSet<>();
        agregarLosRecursos(recursos);
        return recursos;
    }
    
    private void agregarLosRecursos(Set<Class<?>> recursos){
        recursos.add(ues.occ.edu.sv.ingenieria.prn335.serverpeople.resources.DatosResource.class);
    }
    
}