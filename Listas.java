import java.util.ArrayList;

public class Listas{
    public static void main(String[] args){
        System.out.println("Initilazing system...");

        ArrayList<String> songs = new ArrayList<>();
        songs.add(0, "Gaslight - Softcult");
        songs.add(1, "House of mirrors - Softcult");
        songs.add(2, "Bird song - Softcult");
        songs.add(3, "Spit it out - Softcult");
        songs.add(4, "Shortest fuse - Softcult");

        System.out.println("\nLista de canciones en la biblioteca:");
        for (String showSongs : songs){
        System.out.println(showSongs);
        }

        System.out.println("\nNúmero total de canciones: " + songs.size());

        System.out.println("\nSystem stoped.");
    }
}