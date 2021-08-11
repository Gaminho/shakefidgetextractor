import bean.TornadoMonster;
import extract.ImgDownloader;
import extract.MonsterExtractor;
import firebase.error.FirebaseException;
import firebase.service.FirebaseServiceTornadoService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FirebaseException, UnsupportedEncodingException {

        final List<TornadoMonster> monsters = MonsterExtractor.extractTornadoMonster("input");

        final Scanner scanner = new Scanner(System.in);

        int input;
        do {
            System.out.println("Enter a level: ");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                input = 1;
            }

            if (input < 1 || input > 1000) {
                scanner.close();
                System.exit(0);
                break;
            }

            final TornadoMonster monster = monsters.get(input - 1);
            System.out.println("From file: " + monster);
            System.out.println("Remote: " + FirebaseServiceTornadoService.getStair(input));

            System.out.println("Would you like to download img? (y/n)");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                ImgDownloader.downloadImg(monster.getImgLink(), String.valueOf(monster.getStair()));
            }

            System.out.println("Would you like to save it? (y/n)");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Saving: " + FirebaseServiceTornadoService.saveNewMonster(monster));
            }
        }
        while (true);
    }
}
