package pattern.memento;

public class RoleStateMemento {
        
    private int vit;
    private int atk;
    
    public RoleStateMemento(int vit, int atk){
        this.vit=vit;
        this.atk=atk;
    }

    public int getVit() {
        return vit;
    }

    public void setVit(int vit) {
        this.vit = vit;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }
}