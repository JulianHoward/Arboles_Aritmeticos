package aritmetico;



public abstract class ElementoAritmetico implements Identificable {

    @Override
    public String getId() {
        //return String.valueOf(this.getClass().getSimpleName());
        return String.valueOf(this.hashCode());
    }
}
