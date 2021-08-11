package bean;

public class Monster {

    private String name;
    private int level;
    private long skill;
    private long strength;
    private long intelligence;
    private long stamina;
    private long luck;
    private String imgLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getSkill() {
        return skill;
    }

    public void setSkill(long skill) {
        this.skill = skill;
    }

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public long getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(long intelligence) {
        this.intelligence = intelligence;
    }

    public long getStamina() {
        return stamina;
    }

    public void setStamina(long stamina) {
        this.stamina = stamina;
    }

    public long getLuck() {
        return luck;
    }

    public void setLuck(long luck) {
        this.luck = luck;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", skill=" + skill +
                ", strength=" + strength +
                ", intelligence=" + intelligence +
                ", stamina=" + stamina +
                ", luck=" + luck +
                ", imgLink='" + imgLink + '\'' +
                '}';
    }
}
