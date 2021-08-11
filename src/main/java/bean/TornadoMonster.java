package bean;

public class TornadoMonster extends Monster {
    private int stair;

    public int getStair() {
        return stair;
    }

    public void setStair(int stair) {
        this.stair = stair;
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() -1)
                .concat(", stair="+stair+"}");
    }
}
