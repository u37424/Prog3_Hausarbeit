package de.medieninformatik.client.model;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class Request {
    private final Client client;
    private final String baseURI;

    public Request(String baseURI) {
        this.baseURI = baseURI;
        this.client = ClientBuilder.newClient();
    }

    public void get(String uri) {
        WebTarget target = getTarget("GET", uri);
        Response response =
                target.request().accept(MediaType.TEXT_PLAIN).get();
        if (status(response) == 200) {
            String student = response.readEntity(String.class);
            System.out.println(student);
        }
    }

    private WebTarget getTarget(String crud, String uri) {
        System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
        return client.target(baseURI + uri);
    }

    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }

}
