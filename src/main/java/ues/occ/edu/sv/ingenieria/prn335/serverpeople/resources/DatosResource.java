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
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response crearUser(Datos datos) {

        try {
            if (datosFacade != null && datos != null) {
                datosFacade.create(datos);
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

    public DatosFacade getDatosFacade() {
        return datosFacade;
    }

    public void setDatosFacade(DatosFacade datosFacade) {
        this.datosFacade = datosFacade;
    }
}
