package client;

import static java.lang.System.out;

public class Menus {
    public static void printLoggedOutMenu(){

        out.print("REGISTER <Username> <Password> <Email> \n");
        out.print("LOGIN <Username> <Password> \n");
        out.print("QUIT \n");
        out.print("HELP \n");

    }

    public static void printLoggedInMenu(){

        out.print("\nCREATE <GameName> \n");
        out.print("LIST \n");
        out.print("JOIN <ID> [White|Black]\n");
        out.print("SPECTATE <ID>\n");
        out.print("LOGOUT\n");
        out.print("QUIT\n");
        out.print("HELP \n");

    }
    public static void printGameMenu(){
        out.print("Redraw \n");
        out.print("Move <Current Location> <New location> <PromotionType>\n");
        out.print("Highlight <Piece Location> \n");
        out.print("Leave\n");
        out.print("Resign\n");
        out.print("Help\n");

    }
}

