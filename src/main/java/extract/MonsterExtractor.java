package extract;

import bean.TornadoMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class MonsterExtractor {

    public static List<TornadoMonster> extractTornadoMonster(final String filePath) {
        final String fileContent = FileReader.readFileAsString(filePath);
        return Arrays.stream(fileContent.split("<tr\\s* id="))
                .filter(s -> !s.contains("tbody"))
                .map(s -> s.substring(s.indexOf("<td"), s.indexOf("</tr>")))
                .map(MonsterExtractor::mapRowContentToMonster)
                .collect(Collectors.toList());
    }

    private static TornadoMonster mapRowContentToMonster(final String tableRow) {
        final TornadoMonster monster = new TornadoMonster();
        final List<String> columnContents = Arrays.stream(tableRow.split("<td"))
                .filter(StringUtils::isNotBlank)
                .map(s -> s.substring(s.indexOf(">") + 1, s.indexOf("</td>")))
                .collect(Collectors.toList());

        monster.setStair(Integer.parseInt(columnContents.get(1)));
        monster.setName(extractNameFromCellNameContent(columnContents.get(2)));
        monster.setImgLink(extractImgLinkFromCellContent(columnContents.get(2)));
        monster.setLevel(Integer.parseInt(columnContents.get(3)));
        monster.setStrength(Long.parseLong(columnContents.get(5).replace(".", "")));
        monster.setSkill(Long.parseLong(columnContents.get(6).replace(".", "")));
        monster.setIntelligence(Long.parseLong(columnContents.get(7).replace(".", "")));
        monster.setStamina(Long.parseLong(columnContents.get(8).replace(".", "")));
        monster.setLuck(Long.parseLong(columnContents.get(9).replace(".", "")));

        return monster;
    }

    private static String extractNameFromCellNameContent(final String cellNameContent) {
        return cellNameContent.contains("</a>") ?
                cellNameContent.substring(cellNameContent.indexOf("\">") + 2, cellNameContent.indexOf("</a>"))
                : cellNameContent;
    }

    private static String extractImgLinkFromCellContent(final String cellNameContent) {
        return cellNameContent.contains("</a>") ?
                cellNameContent.substring(cellNameContent.indexOf("href=\"") + 6, cellNameContent.indexOf("\" class"))
                : null;
    }

    private MonsterExtractor() {
    }
}
