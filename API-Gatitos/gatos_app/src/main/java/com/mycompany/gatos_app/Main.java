package com.mycompany.gatos_app;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        int opcionMenu = -1;
        String[] botones = {"1. Ver gatos", "2. Salir"};
        do {
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos java", "Menu principal", JOptionPane.INFORMATION_MESSAGE,
                    null, botones, botones[0]);
//            Validamos que opcion selecciona el usuario.
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcionMenu = i;
                }
            }
            
            switch (opcionMenu) {
                case 0:
                    GatosService.verGatitos();
                    break;
                case 1:
                    
                    break;
                default:
                    break;
            }
            
        } while (opcionMenu != 1);
    }
}
