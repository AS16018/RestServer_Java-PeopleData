package ues.occ.edu.sv.ingenieria.prn335.serverpeople.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ues.occ.edu.sv.ingenieria.prn335.serverpeople.entity.Datos;
import ues.occ.edu.sv.ingenieria.prn335.serverpeople.entity.DatosFacade;
import ues.occ.edu.sv.ingenieria.prn335.serverpeople.entity.Login;

/**
 *
 * @author 
 */
@Path("datos")
@ApplicationScoped
public class DatosResource implements Serializable{
    
    public DatosResource(){
    }
    
    @Inject
    DatosFacade datosFacade;
    
    @GET
    @Path("findAll")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findAll() {
        List salida = null;
        try {
            if (datosFacade != null) {
                salida = datosFacade.findAll();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (salida == null) {
                salida = new ArrayList();
            }
        }
        return Response.ok(salida).build();
    }

    @GET
    @Path("buscarId/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Integer Id) {

        try {
            if (datosFacade != null && Id != null) {
                if (datosFacade.find(Id) == null) {
                    return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
                } else {
                    return Response.ok(datosFacade.find(Id)).build();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("range")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findRange(
            @DefaultValue("0") @QueryParam("primero") int primero,
            @DefaultValue("5") @QueryParam("ultimo") int ultimo) {
        List salida = null;
        try {
            if (datosFacade != null) {
                salida = datosFacade.findRange(primero, ultimo);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (salida == null) {
                salida = new ArrayList();
            }
        }

        return Response.ok(salida).build();
    }

    @GET
    @Path("count")
    @Produces(value = MediaType.TEXT_PLAIN)
    public Response count() {
        Integer total = 0;
        try {
            if (datosFacade != null) {
                total = datosFacade.count();
            }
            if (total != 0 && !total.equals(null)) {
                return Response.ok(total).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    @Path("crear")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response crearUser(Datos datos) {
        
        try {
            if (datosFacade != null && datos != null) {
                String comproEmail = this.validarEmail(datos.getEmail());
                
                if (comproEmail.equals("valido")) {
                    datos.setId(this.obtenerUltimoId());
                    datosFacade.create(datos);
                }
                else{
                    return Response.status(Response.Status.BAD_REQUEST).entity("El nombre del email ya se encuentra en uso").build();
                }
                
            }
            if (datos == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Datos nulos").build();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return Response.ok().build();
    }

    @PUT
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response modificarDatos(Datos dato) {

        try {
            if (datosFacade != null && dato != null) {
                datosFacade.edit(dato);
            }
            if (dato == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Datos nulos").build();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(value = MediaType.TEXT_PLAIN)
    public Response deleteDatos(@PathParam("id") Integer id) {
        List<Datos> datoEliminar = null;
        try {
            if (datosFacade != null) {
                if (id != null) {
                    datoEliminar = datosFacade.findAll().stream().filter(t -> id == t.getId().intValue()).collect(Collectors.toList());
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("El id va nulo").build();
                }
            }

            if (datoEliminar.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            }
            Datos datoE = datoEliminar.get(0);
            datosFacade.remove(datoE);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return Response.ok().build();
    }
    
    @POST
    @Path("login")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response validarLogin(Login login) {
        
        List<Datos> user = null;
        login.setErrorEmail("");
        login.setExito("");
        login.setErrorPassword("");
        
        try {
            if (datosFacade != null && login != null) {
                user = datosFacade.findAll().stream().filter(e -> e.getEmail().equals(login.getEmail())).collect(Collectors.toList());
                
            }
            if (login == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Datos nulos").build();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        if (user==null || user.size()<1) {
            login.setErrorEmail("El email "+login.getEmail()+" es invalido, por favor intente nuevamente");
            return Response.status(Response.Status.UNAUTHORIZED).entity(login).build();
            
        }else if (!user.get(0).getPassword().equals(login.getPassword())) {
            login.setErrorPassword("La contraseña que intenta ingresar es inválida, por favor intente nuevamente");
           return Response.status(Response.Status.UNAUTHORIZED).entity(login).build();
        }
        login.setEmail(user.get(0).getNombre());
        login.setPassword(user.get(0).getApellido());
        login.setExito("Inicio de sesión exitosa");
        return Response.status(Response.Status.ACCEPTED).entity(login).build();
    }
    
    public String validarEmail(String email){
        String validarEmail = "";
        String result = "";
        try {
            if (email !=null) {
               validarEmail = datosFacade.findAll().stream().filter(e -> e.getEmail().equals(email)).collect(Collectors.toList()).get(0).getEmail();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (validarEmail.equals("")) {
            result = "valido";
        }else{
            result = "invalido";
        }
        return result;
    }
    
    public Integer obtenerUltimoId(){
        Integer idMayor = 0;
        try {
            if (datosFacade != null) {
                idMayor = datosFacade.findAll().stream().max((id1, id2) -> id1.getId() - id2.getId()).get().getId();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (idMayor == 0 || idMayor == null) {
            idMayor = 1;
        }
        else{
            idMayor = idMayor+1;
        }
        return idMayor;
    }
    
    

    public DatosFacade getDatosFacade() {
        return datosFacade;
    }

    public void setDatosFacade(DatosFacade datosFacade) {
        this.datosFacade = datosFacade;
    }
}
