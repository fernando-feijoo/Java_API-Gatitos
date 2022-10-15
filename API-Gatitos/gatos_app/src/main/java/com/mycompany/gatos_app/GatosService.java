package com.mycompany.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sun.management.Agent;

public class GatosService {

    public static void verGatitos() {
//        1. Vamos a traer los datos de la API 
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
            Response response = client.newCall(request).execute();

            String elJson = response.body().string();
//            Cortamos el texto que viene el primer caracter y restamos el ultimo.
            elJson = elJson.substring(1, elJson.length() - 1);
//            Creamos un objeto de la clase Gson.
            Gson gson = new Gson();
            Gatos gatitos = gson.fromJson(elJson, Gatos.class);
//            Redimencion en caso de necesitar.
            Image image = null;
            try {
                URL url = new URL(gatitos.getUrl());
//                image = ImageIO.read(url);
//                ImageIcon fondoGato = new ImageIcon(image);
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
//                image = ImageIO.read(httpcon);
                httpcon.addRequestProperty("User - Agent", "");
                BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                ImageIcon fondoGato = new ImageIcon(bufferedImage);

                if (fondoGato.getIconWidth() > 800) {
//                    Hacemos la redimencion de la imagen.
                    Image fondo = fondoGato.getImage();
                    Image modifcada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modifcada);
                }
                String menu = "Opciones: \n"
                        + " 1. Ver otra imagen \n"
                        + " 2. Favorito \n"
                        + " 3. Volver \n";

                String[] botones = {"Ver otra imgen", "Favorito", "Volver"};
                String id_gato = gatitos.getId();
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
//                        favoritoGato(gatitos);
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

    }
}
