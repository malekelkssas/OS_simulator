package storage;

public class Word {
    private Storable data;
    public Word(Storable data){this.data = data;}

    public Storable getData() {return data;}
    public void setData(Storable data) {this.data = data;}
}
