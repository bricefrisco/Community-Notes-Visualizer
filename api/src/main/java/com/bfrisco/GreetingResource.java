package com.bfrisco;

import com.bfrisco.models.NoteDTO;
import com.bfrisco.services.DataImporter;
import com.bfrisco.services.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/")
public class GreetingResource {
    @Inject
    DataService dataService;

    @Inject
    DataImporter dataImporter;

    @GET
    @Path("/notes")
    public List<NoteDTO> getNotes() {
        return dataService.fetchNotes(0, 10);
    }

    @POST
    @Path("/import")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Boolean users() throws IOException {
        dataImporter.run();
        return true;
    }


}