package com.mycompany.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class GatosService {

    public static void verGatitos() {
//        1. Vamos a traer los datos de la API 
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").method("GET", null).build();
            Response response = client.newCall(request).execute();

            String elJson = response.body().string();
//            Cortamos el texto que viene el primer caracter y restamos el ultimo.
            elJson = elJson.substring(1, elJson.length() - 1);
//            Creamos un objeto de la clase Gson.
            Gson gson = new Gson();
            Gatos gatos = gson.fromJson(elJson, Gatos.class);
//            Redimencion en caso de necesitar.
            try {
                URL url = new URL(gatos.getUrl());
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent", "");
                BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                ImageIcon fondoGato = new ImageIcon(bufferedImage);

                if (fondoGato.getIconWidth() > 800 || fondoGato.getIconHeight() > 600) {
//                    Hacemos la redimencion de la imagen.
                    Image fondo = fondoGato.getImage();
                    Image modifcada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modifcada);
                }
                String menu = "Opciones: \n"
                        + " 1. Ver otra imagen \n"
                        + " 2. Favorito \n"
                        + " 3. Volver \n";

                String[] botones = {"Ver otra imagen", "Favorito", "Volver"};
                String id_gato = gatos.getId();
                String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE,
                        fondoGato, botones, botones[0]);

                int seleccion = -1;
                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])) {
                        seleccion = i;
                    }
                }

                switch (seleccion) {
                    case 0:
                        verGatitos();
                        break;
                    case 1:
                        favoritoGato(gatos);
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("Error de imagen: " + e);
            }

        } catch (IOException ex) {
            System.out.println("Error Api Gatitos: " + ex);
        }
    }

    public static void favoritoGato(Gatos gatito) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\"" + gatito.getId() + "\"\n}");
            Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites").post(body).addHeader("Content-Type", "application/json").addHeader("x-api-key", gatito.getApikey()).build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("Erro en favoritos: " + e);
        }
    }

    public static void verFavorito(String apiKey) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", apiKey)
                    .build();
            Response response = client.newCall(request).execute();
//            Guardamos el string con la resspuesta.
            String elJson = response.body().string();

//            Creamos el objeto gson.
            Gson gson = new Gson();

            GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);

            if (gatosArray.length > 0) {
                int min = 1;
                int max = gatosArray.length;
                int aleatorio = (int) (Math.random() * ((max - min) + 1) + min);
                int indice = aleatorio - 1;

                GatosFav gatosFav = gatosArray[indice];
//            Redimencion en caso de necesitar.
                try {
                    URL url = new URL(gatosFav.getImage().getUrl());
                    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                    httpcon.addRequestProperty("User-Agent", "");
                    BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                    ImageIcon fondoGato = new ImageIcon(bufferedImage);

                    if (fondoGato.getIconWidth() > 800 || fondoGato.getIconHeight() > 600) {
//                    Hacemos la redimencion de la imagen.
                        Image fondo = fondoGato.getImage();
                        Image modifcada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                        fondoGato = new ImageIcon(modifcada);
                    }
                    String menu = "Opciones: \n"
                            + " 1. Ver otra imagen \n"
                            + " 2. Eliminar favorito \n"
                            + " 3. Volver \n";

                    String[] botones = {"Ver otra imagen", "Eliminar favorito", "Volver"};
                    String id_gato = gatosFav.getId();
                    String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE,
                            fondoGato, botones, botones[0]);

                    int seleccion = -1;
                    for (int i = 0; i < botones.length; i++) {
                        if (opcion.equals(botones[i])) {
                            seleccion = i;
                        }
                    }

                    switch (seleccion) {
                        case 0:
                            verFavorito(apiKey);
                            break;
                        case 1:
                            borrarFavorito(gatosFav);
                            break;
                        default:
                            break;
                    }

                } catch (IOException e) {
                    System.out.println("Error de imagen: " + e);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro en favoritos: " + e);
        }
    }

    public static void borrarFavorito(GatosFav gatoFav) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatoFav.getId() + "")
                    .delete(null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatoFav.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("Error eliminar favorito: " + e);
        }
    }
}
