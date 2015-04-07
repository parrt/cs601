import org.stringtemplate.v4.ST;

class Simple {
    public static void main(String[] args) {
        ST query = new ST("SELECT $column$ FROM $table$;");
        query.add("column", "name");
        query.add("table", "User");
        System.out.println("QUERY: "+query.toString());
    }
}
