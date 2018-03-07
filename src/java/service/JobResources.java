/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Models.Jobs;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Hillo
 */
@Stateless
@Path("jobs")
public class JobResources extends AbstractFacade<Jobs> {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public JobResources() {
        super(Jobs.class);
    }

    @GET
    @Path("byDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Jobs> findJobsByDepartmentId(@QueryParam("dptId") int dptId) {
        return (List<Jobs>) em.createNamedQuery("Jobs.findByDepartmentId").setParameter("departmentId", dptId).getResultList();
    }
    
    @GET
    @Path("get/all")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Jobs> findAll() {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
