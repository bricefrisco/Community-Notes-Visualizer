package com.bfrisco;

import com.bfrisco.models.NoteResponse;
import com.bfrisco.services.DataImporter;
import com.bfrisco.services.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
public class GreetingResource {
    @Inject
    DataService dataService;

    @Inject
    DataImporter dataImporter;

    @GET
    @Path("/notes")
    public NoteResponse getNotes(
            @QueryParam("finalRatingStatus")
            String status,
            @QueryParam("page")
            @DefaultValue("1")
            Integer page,
            @QueryParam("searchQuery")
            @DefaultValue("")
            String searchQuery
    ) {
        DataService.FinalRatingStatus finalRatingStatus;
        try {
            finalRatingStatus = DataService.FinalRatingStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            finalRatingStatus = null;
        }

        return dataService.fetchNotes(finalRatingStatus, searchQuery, page - 1, 6);
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