package tm;

public class SummaryTM {
    private String item_code;
    private String item_name;
    private String date;
    private int d_out;
    private int d_return;
    private int damage;
    @Override
    public String toString() {
        return "SummaryTM{" +
                "item_code='" + item_code + '\'' +
                ", item_name='" + item_name + '\'' +
                ", date='" + date + '\'' +
                ", d_out=" + d_out +
                ", d_return=" + d_return +
                ", damage=" + damage +
                '}';
    }



    public SummaryTM(String item_code, String item_name, String date, int d_out, int d_return, int damage) {
        this.setItem_code(item_code);
        this.setItem_name(item_name);
        this.setDate(date);
        this.setD_out(d_out);
        this.setD_return(d_return);
        this.setDamage(damage);
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getD_out() {
        return d_out;
    }

    public void setD_out(int d_out) {
        this.d_out = d_out;
    }

    public int getD_return() {
        return d_return;
    }

    public void setD_return(int d_return) {
        this.d_return = d_return;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
