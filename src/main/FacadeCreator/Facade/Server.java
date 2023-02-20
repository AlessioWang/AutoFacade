//package java;
//
//import archijson.ArchiJSON;
//import archijson.ArchiServer;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//
//import java.manager.G_FacadeTest;
//
//
//
//public class Server implements ArchiServer {
//
//    G_FacadeTest generator;
//
//    public Server(G_FacadeTest generator) {
//        this.generator = generator;
//
//        String URL = "https://web.archialgo.com";
//        String TOKEN = "de692bb0-b196-4129-9318-2b23f0e29226";
//        String IDENTITY = "facade-generator";
//        ArchiServer.super.setup(URL, TOKEN, IDENTITY);
//    }
//
//    @Override
//    public void onReceive(Socket socket, String id, JsonObject body) {
//        ArchiServer.super.onReceive(socket, id, body);
//
//        Gson gson = new Gson();
//
//        ArchiJSON archijson = gson.fromJson(body, ArchiJSON.class);
//        System.out.println(body);
//        archijson.parseGeometryElements(gson);
//
//        generator.receive(archijson);
//        ArchiJSON ret = generator.toArchiJSON();
//        if (ret != null)
//            ArchiServer.super.send(socket, "client", id, gson.toJson(ret));
//    }
//}
