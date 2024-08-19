package dev.sunrintana;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

//@Path("/")
public class AnATree {

    private static final String name = "AnA";
    private static final String password = "abcd";
    private static final List<Gift> gifts = new ArrayList<>();

//    @GET
//    @Path("/info")
//    @Produces(MediaType.APPLICATION_JSON)
    public String info() {
        return """
                {
                    "name": "%s",
                    "count": %d
                }
                """.formatted(name, gifts.size());
    }

//    @POST
//    @Path("/items")
    public Response items(String body) {
        try {
            JSONArray a = new JSONArray();
            JSONObject o = body!=null ? new JSONObject(body) : null;
            boolean isPass = o != null && o.has("pass") && o.getString("pass").equals(password);
            boolean isAfter = new LocalDate().withMonthOfYear(12).withDayOfMonth(24).isAfter(LocalDate.now());
            for(Gift gift : gifts) {
                JSONObject json = new JSONObject();
                if(isPass) json.put("author", gift.author());
                if(isAfter) json.put("message", gift.message());
                json.put("type", gift.type());
                a.put(json);
            }
            return Response.ok(new JSONObject().put("items", a).toString(), MediaType.APPLICATION_JSON).build();
        } catch (JSONException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

//    @POST
//    @Path("/item")
    public Response item(String body) {
        if(body != null) {
            try {
                JSONObject o = new JSONObject(body);
                if(o.has("author") && o.has("message") && o.has("type")) {
                    gifts.add(new Gift(o.getString("author"), o.getString("message"), o.getInt("type")));
                    return Response.status(201).build();
                }
            }catch (JSONException ignored) {}
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private record Gift(String author, String message, int type) {}
}
